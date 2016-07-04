VTMNetwork : VTMDynamicContextManager {
	var <application;
	classvar <defaultPort = 57120;

	*new{arg name, application, description, definition;
		^super.new(name, nil, description, definition).initNetwork(application);
	}

	initNetwork{arg application_;
		application = application_;
		NetAddr.broadcastFlag = true;
		this.makeOSCResponders;
		"VTMNetwork initialized".postln;
	}

	discover{
		//Broadcast network discovery message:
		//  /? <name> <ip:port>
		NetAddr("255.255.255.255", this.class.defaultPort).sendMsg(
			'/?',
			this.name,
			this.addr.generateIPString
		);
	}

	makeOSCResponders{
		[
			OSCFunc({arg msg, time, addr, port;//network discover responder
				var remoteName, remoteAddr;
				"Got network query: %".format([msg, time, addr, port]).postln;
				//> get the name and the address for the app that queries
				remoteName = msg[1];
				remoteAddr = NetAddr.newFromIPString(msg[2].asString);
				if(remoteName != this.name, {
					//register this application
					this.addApplicationProxy(remoteName, remoteAddr);

					//> reply with this name, addr:ip
					//<to the querier> /! <name> <addr:ip>
					this.applicationProxies[remoteName].sendMsg(
						'!',
						this.name,
						this.addr.generateIPString
					);
				});
			}, '/?'),
			OSCFunc({arg msg, time, addr, port;//network discover reply
				//> get the name and the address of the responding app
				var remoteName, remoteAddr;
				//> get the name and the address for the app that queries
				remoteName = msg[1];
				remoteAddr = NetAddr.newFromIPString(msg[2].asString);
				if(remoteName != this.name, {
					//register this application
					this.addApplicationProxy(remoteName, remoteAddr);
					//> Make a ApplicationProxy for this responding app
				});
			}, "/%!".format(this.name).asSymbol)
		];
	}

	addApplicationProxy{arg name, addr;
		if(this.applicationProxies.includesKey(name).not, {
			var newAppProxy = VTMApplicationProxy(name, this, (addr: addr));
			"Adding app proxy: % - %".format(name, addr).postln;
			this.addChild(newAppProxy);
		}, {
			"App proxy already registered: % - %".format(name, addr).postln;
		});
	}

	localApplication { ^parent; }

	applicationProxies{ ^children; }

	applications {
		var result;
		result = this.remoteApplications.copy;
		result.put(this.localApplication.name, this.localApplication);
		^result;
	}
}
