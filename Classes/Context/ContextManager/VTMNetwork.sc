VTMNetwork : VTMContextManager {
	var <application;
	classvar <defaultPort = 57120;

	*new{arg name, application, declaration, definition;
		^super.new(name, nil, declaration, definition).initNetwork(application);
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
					"Registering new application: %".format([remoteName, remoteAddr]).postln;
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
				var remoteName, remoteAddr;
				//> get the name and the address of the responding app
				remoteName = msg[1];
				remoteAddr = NetAddr.newFromIPString(msg[2].asString);
				"Got response from: %".format([remoteName, remoteAddr]).postln;
				if(remoteName != this.name, {
					//register this application
					this.addApplicationProxy(remoteName, remoteAddr);
					//> Make a ApplicationProxy for this responding app
				});
			}, "/%!".format(this.name).asSymbol)
		];
	}

	addApplicationProxy{arg name, addr;
		"Network - addApplicationProxy".format([name, addr]).postln;
		if(this.applicationProxies.includesKey(name).not and: {name != this.name}, {
			var newAppProxy = VTMApplicationProxy(name, this, (targetAddr: addr));
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
