VTMNetwork : VTMDynamicContextManager {

	*new{arg name, parent, description, defintion;
		^super.new(name, parent, description, defintion).initNetwork;
	}

	initNetwork{
		"VTMNetwork initialized".postln;
	}

	discover{
		//Broadcast network discovery message:
		//  /? <name> <ip:port>
	}

	makeOSCResponders{
		[
			OSCFunc({arg msg, time, addr, port;//network discover responder
				//> get the name and the address for the app that queries
				//> reply with this name, addr:ip
				//  > (to the querier) /! <name> <addr:ip>
				}, '/?'),
			OSCFunc({arg msg, time, addr, port;//network discover reply
				//> get the name and the address of the responding app
				//> Make a ApplicationProxy for this responding app
			})
		];
	}

	localApplication { ^parent; }
	
	remoteApplications{ ^children; }

	applications {
		var result;
		result = this.remoteApplications.copy;
		result.put(this.localApplication.name, this.localApplication);
		^result;
	}
}
