//a Singleton class that communicates with the network and manages Applications
VTMLocalNetworkNode : VTMAbstractDataManager {
	classvar <singleton;
	classvar <hostname;
	classvar <discoveryBroadcastPort = 57200;
	classvar <remoteNetworkNodes;
	var discoveryReplyResponder;
	var <networkNodeManager;
	var <hardwareSetup;
	var <moduleHost;
	var <sceneOwner;
	var <scoreManager;

	*dataClass{ ^VTMApplication; }

	*initClass{
		Class.initClassTree(VTMAbstractData);
		Class.initClassTree(VTMNetworkNodeManager);
		hostname = Pipe("hostname", "r").getLine();
		singleton = super.new.initLocalNetworkNode;
	}

	*new{
		^singleton;
	}

	initLocalNetworkNode{
		networkNodeManager = VTMNetworkNodeManager.new(this);
		hardwareSetup = VTMHardwareSetup.new(this);
		moduleHost = VTMModuleHost.new(this);
		sceneOwner = VTMSceneOwner.new(this);
		scoreManager = VTMScoreManager.new(this);

		NetAddr.broadcastFlag = true;
	}

	activate{arg val;
		if(discoveryReplyResponder.isNil, {
			discoveryReplyResponder = OSCFunc({arg msg, time, resp, addr;
				var jsonData = VTMJSON.parse(msg[1]);
				var senderHostName, netAddr, registered = false;
				senderHostName = jsonData["hostname"];
				topEnvironment[\jsonData] = jsonData;
				netAddr = NetAddr.newFromIPString(jsonData["addr"].asString);
				"We got a discovery message: % %".format(senderHostName, netAddr).postln;
				"The local Addr: %".format(this.getLocalAddr).postln;

				if(netAddr != this.getLocalAddr, {
					var registered;
					registered = remoteNetworkNodes.includesKey(senderHostName);
					if(registered.not)
					{
						"Registering new network node: %".format([senderHostName, netAddr]).postln;
						networkNodeManager.addItemsFromItemDeclarations([
							netAddr.generateIPString.asSymbol -> (hostname: hostname)
						]);
						this.discover(netAddr);
					};
				}, {
					"Got broadcastfrom local network node: %".format(this.getLocalAddr).postln;
				});
			}, '/discovery', recvPort: this.class.discoveryBroadcastPort);
		});
	}

	deactivate{
		discoveryReplyResponder !? {discoveryReplyResponder.free;};
	}

	applications{ ^items; }

	getBroadcastIp {

		var res = Pipe("ifconfig | grep broadcast | awk '{print $NF}'", "r").getLine();

		// alternative check for raspi??
		// TODO: get proper safety configurations for different BSDs...

		res ??
		{
			res = Pipe("ifconfig | egrep broadcast\|Bcast | awk '{print $NF}'", "r").getLine();
		}

		^res;
	}

	getLocalIp {

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

		//if(lnet.not) { Error("VTM - could not find localnetwork..").throw(); };
		//^lnet_ip;
		^"127.0.0.1";
	}

	getLocalAddr{
		^NetAddr(this.getLocalIp, NetAddr.localAddr.port);
	}

	name{
		^this.getLocalAddr.generateIPString;
	}

	fullPath{
		^'/';
	}

	discover {arg destinationAddr;

		var data, targetAddr;

		data = (
			hostname: hostname,
			addr: NetAddr(this.getLocalIp, NetAddr.localAddr.port).generateIPString
		);

		// if the method argument is nil, the message is broadcasted

		if(destinationAddr, {
			targetAddr = NetAddr(
				this.getBroadcastIp,
				this.class.discoveryBroadcastPort
			);
		}, {
			targetAddr = destinationAddr;
		});

		//Makes the responder if not already made
		discoveryReplyResponder.value;
		this.class.sendMsg(
			targetAddr.hostname, this.class.discoveryBroadcastPort, '/discovery', data
		);
		postln([targetAddr.hostname, targetAddr.port, '/discovery', data]);
	}

	*leadingSeparator { ^$/; }

	*sendMsg{arg hostname, port, path ...data;
		//sending eeeeverything as typed YAML for now.
		NetAddr(hostname, port).sendMsg(path, VTMJSON.stringify(data.unbubble));
	}

	findManagerForContextClass{arg class;
		var managerObj;
		managerObj = switch(class,
			VTMModule, { moduleHost;},
			VTMHardwareDevice, { hardwareSetup; },
			VTMScene, { sceneOwner; },
			VTMScore, { scoreManager; }
		);
		"DID I Find: % \n\t%".format(managerObj, class).postln;
		^managerObj;
	}

	registerUnmanagedContext{arg context;
		var managerObj;
		managerObj = this.findManagerForContextClass(context.class);
		managerObj.addItem(context);
		"registering unmanaged: %".format(context).postln;
	}
}

