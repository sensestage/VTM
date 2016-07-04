VTMNetwork : VTMDynamicContextManager {
	var <application;
	classvar <defaultPort = 57120;

	*new{arg name, application, description, definition;
		^super.new(name, nil, description, definition).initNetwork(application);
	}

	initNetwork{arg application_;
		application = application_;
		NetAddr.broadcastFlag = true;
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
				//> get the name and the address for the app that queries
				remoteName = msg[1];
				remoteAddr = NetAddr.newFromIPString(msg[2]);
				//register this application
				this.addApplicationProxy(remoteName, remoteAddr);

				//> reply with this name, addr:ip
				//<to the querier> /! <name> <addr:ip>
				this.applicationProxies[\name].sendMsg(
					'/!',
					this.name,
					this.addr.generateIPString
				);
			}, '/?'),
			OSCFunc({arg msg, time, addr, port;//network discover reply
				//> get the name and the address of the responding app
				//> Make a ApplicationProxy for this responding app
			})
		];
	}

	addApplicationProxy{arg name, addr;
		if(this.applicationProxies.includeKey(name).not, {
			var newAppProxy = VTMApplicationProxy(name, this, (addr: addr));
			this.addChild(newAppProxy);
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
