//a Singleton class that communicates with the network and manages Applications
VTMLocalNetworkNode : VTMAbstractDataManager {

	classvar <hostname;
	classvar <applications;
	classvar discoveryReplyResponder;
	classvar <discoveryBroadcastPort = 57200;
	classvar remoteNetworkNodes;

	*dataClass{ ^VTMApplication; }

	*initClass{

		Class.initClassTree(VTMAbstractData);
		Class.initClassTree(VTMNetworkNodeManager);

		NetAddr.broadcastFlag = true;
		hostname = Pipe("hostname", "r").getLine();
		remoteNetworkNodes = Dictionary.new;
		discoveryReplyResponder = Thunk {

			OSCFunc({arg msg, time, resp, addr;

				var jsonData = VTMJSON.parse(msg[1]);
				var senderHostName, netAddr, registered = false;
				senderHostName = jsonData["hostname"];
				topEnvironment[\jsonData] = jsonData;
				netAddr = NetAddr.newFromIPString(jsonData["addr"].asString);
				"We got a discovery message: % %".format(senderHostName, netAddr).postln;
				"The local Addr: %".format(this.getLocalAddr).postln;

				registered = remoteNetworkNodes.includesKey(senderHostName);
				if(registered)
				{
					"Got broadcastfrom local network node: %".format(this.getLocalAddr).postln;
				}
				// else register new node
				{
					"Registering new network node: %".format([senderHostName, netAddr]).postln;
					remoteNetworkNodes.put(senderHostName, netAddr);
					VTMLocalNetworkNode.discover(netAddr);
				};
			}, '/discovery', recvPort: VTMLocalNetworkNode.discoveryBroadcastPort);
		};

		StartUp.add { discoveryReplyResponder.value }
	}

	*getBroadcastIp {
		^Pipe("ifconfig | grep broadcast | awk '{print $NF}'", "r").getLine();
	}

	*getLocalIp {

		// check BSD, may vary ...
		var line, lnet = false, lnet_ip;
		var addr_list = Pipe("ifconfig | grep \"inet \" | awk '{print $2}'","r");
		var data;
		var targetAddr;
		line = addr_list.getLine();

		while({line.notNil()})
		{
			lnet = "[0-9]{3}\.[0-9]{3}\.[0-9]{1,}\.[1-9]{1,}"
			.matchRegexp(line);

			if(lnet)
			{
				lnet_ip = line;
				if ( lnet_ip.notNil, {
					// fix for linux
					lnet_ip = lnet_ip.replace("addr:","");
				});

				lnet_ip;
			};

			line = addr_list.getLine();
		};

		if(lnet.not) { Error("VTM - could not find localnetwork..").throw(); };
		^lnet_ip;
	}

	*getLocalAddr{
		^NetAddr(this.getLocalIp, NetAddr.localAddr.port);
	}

	*discover {arg destinationAddr;

		var data, targetAddr;

		data = (
			hostname: hostname,
			addr: NetAddr(this.getLocalIp, NetAddr.localAddr.port).generateIPString
		);

		// if the method argument is nil, the message is broadcasted

		destinationAddr ??
		{
			targetAddr = NetAddr(VTMLocalNetworkNode.getBroadcastIp,
				this.discoveryBroadcastPort);
		};

		destinationAddr !?
		{
			targetAddr = destinationAddr;
		};

		//Makes the responder if not already made
		discoveryReplyResponder.value;
		this.sendMsg(targetAddr.hostname, discoveryBroadcastPort, '/discovery', data);
		postln([targetAddr.hostname, targetAddr.port, '/discovery', data]);
	}

	*leadingSeparator { ^$/; }

	*sendMsg{arg hostname, port, path ...data;
		//sending eeeeverything as typed YAML for now.
		NetAddr(hostname, port).sendMsg(path, VTMJSON.stringify(data.unbubble));
	}
}

