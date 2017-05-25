//a Singleton class that communicates with the network and manages Applications
VTMLocalNetworkNode : VTMAbstractDataManager {
	classvar <hostname;
	classvar <applications;
	classvar discoveryReplyResponder;
	classvar <discoveryPort = 57200;

	*dataClass{ ^VTMApplication; }

	*initClass{
		Class.initClassTree(VTMAbstractData);
		Class.initClassTree(VTMNetworkNodeManager);
		
		NetAddr.broadcastFlag = true;
		//singleton = super.new.initLocalNetworkNode;
		hostname = Pipe("hostname", "r").getLine().postln();

		discoveryReplyResponder = Thunk{ 
			OSCFunc({arg msg, time, resp, addr;
				var jsonData = VTMJSON.parse(msg[1]);
				var hostname, netAddr;
				post("data");
				postln(jsonData);
				hostname = jsonData["hostname"];
				topEnvironment[\jsonData] = jsonData;
				netAddr = NetAddr.newFromIPString(jsonData["addr"].asString);
				"We got a discovery message: % %".format(hostname, netAddr).postln;
				"The local Addr: %".format(this.getLocalAddr).postln;
				if(netAddr != this.getLocalAddr, {
					var notYetRegistered = true;
					//TODO: Check if netAddr not yet registered
					if(notYetRegistered, {
						//TODO: register the new network node
						"Registering new network node: %".format([hostname, netAddr]).postln;

						//Reply with our own discory message.
						VTMLocalNetworkNode.discover(netAddr);
					});
				}, {
					"Got broadcast from local network node: %".format(this.getLocalAddr).postln;
				});
			}, '/discovery', recvPort: VTMLocalNetworkNode.discoveryPort);
		};
		//VTMLocalNetworkNode.discover();
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
				lnet_ip.postln();
			};

			line = addr_list.getLine();
		};

		if(lnet.not) { Error("VTM Error, could not find localnetwork..").throw(); };
		^lnet_ip;
	}

	*getLocalAddr{
		^NetAddr(this.getLocalIp, NetAddr.localAddr.port);
	}

	*discover {arg destinationAddr;
		var data;
		var targetAddr;


//		// check BSD, may vary ...
//		var line, lnet = false, lnet_ip;
//		var addr_list = Pipe("ifconfig | grep \"inet \" | awk '{print $2}'","r");
//		line = addr_list.getLine();
//
//		while({line.notNil()})
//		{
//			lnet = "[0-9]{3}\.[0-9]{3}\.[0-9]{1,}\.[1-9]{1,}"
//			.matchRegexp(line);
//
//			if(lnet)
//			{
//				lnet_ip = line;
//				lnet_ip.postln();
//			};
//
//			line = addr_list.getLine();
//		};
//
//		if(lnet.not) { Error("VTM Error, could not find localnetwork..").throw(); };
		data = (
			hostname: hostname, 
			addr: NetAddr(this.getLocalIp, NetAddr.localAddr.port).generateIPString
		);
		//if the method argument is nil, the message is broadcasted
		if(destinationAddr.isNil, {
			targetAddr = NetAddr(VTMLocalNetworkNode.getBroadcastIp(), this.discoveryPort);
		}, {
			targetAddr = destinationAddr;
		});
		//Makes the responder if not already made
		discoveryReplyResponder.value;
		this.sendMsg(targetAddr.hostname, targetAddr.port, '/discovery', data);
		postln([targetAddr.hostname, targetAddr.port, '/discovery', data]);
	}

	*leadingSeparator { ^$/; }

	*sendMsg{arg hostname, port, path ...data;
		//sending eeeeverything as typed YAML for now.
		NetAddr(hostname, port).sendMsg(path, VTMJSON.stringify(data.unbubble));
	}
}

