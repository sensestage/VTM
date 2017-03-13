/*
VTMNetwork : VTMContext {
	var <application;
	var oscResponders;
	classvar <defaultPort = 57120;

	classvar >sendToAllAction;

	*new{arg name, definition, attributes, application;
		^super.new(name, definition, attributes).initNetwork(application);
	}

	initNetwork{arg application_;
		application = application_;
		NetAddr.broadcastFlag = true;
		oscResponders = this.makeOSCResponders;
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

	free{
		//When the network instance is freed we notify the other applications about what is happening.
		// "Application proxies: %".format(this.applicationProxies).postln;
		this.applicationProxies.do({arg item;
			item.sendMsg('/applicationQuitting', this.name, this.addr.generateIPString);
		});
		oscResponders.do(_.free);
		super.free;
	}

	makeOSCResponders{
		^[
			OSCFunc({arg msg, time, addr, port;//network discover responder
				var remoteName, remoteAddr;
				// "[%] - Got network query: \n\t%".format(this.application.name, [msg, time, addr, port]).postln;
				//> get the name and the address for the app that queries
				// "1: %\n\t%".format(this.name, msg).postln;
				remoteName = msg[1].asSymbol;
				remoteAddr = NetAddr.newFromIPString(msg[2].asString);
				if(remoteName != this.name, {
					// "Registering new application: %".format([remoteName, remoteAddr]).postln;
					//register this application
					this.addApplicationProxy(remoteName, remoteAddr);

					//> reply with this name, addr:ip
					//<to the querier> /! <name> <addr:ip>
					remoteAddr.sendMsg(
						"/%!".format(remoteName).asSymbol,
						this.name,
						this.addr.generateIPString
					);
				});
			}, '/?'),
			OSCFunc({arg msg, time, addr, port;//network discover reply
				var remoteName, remoteAddr;
				//> get the name and the address of the responding app
				// "2: %\n\t%".format(this.name, msg).postln;
				remoteName = msg[1];
				remoteAddr = NetAddr.newFromIPString(msg[2].asString);
				// "Got response from: %".format([remoteName, remoteAddr]).postln;
				if(remoteName != this.name, {
					//register this application
					this.addApplicationProxy(remoteName, remoteAddr);
					//> Make a ApplicationProxy for this responding app
				});
			}, "/%!".format(this.name).asSymbol),
			OSCFunc({arg msg, time, addr, port;
				var quittingApp;
				// "3: %\n\t%".format(this.name, msg).postln;
				// "[%] - Notified that app: % at addr: % is quitting.".format(this.name, msg[1], msg[2]).postln;
				quittingApp = this.applicationProxies[msg[1].asSymbol];
				if(quittingApp.notNil, {
					this.removeChild(msg[1].asSymbol);
					// "\tRemoving quitting app: '%'".format(msg[1]).postln;
				}, {
					// "\tQuitting app '%' not found, ignoring notification.".format(msg[1]).postln;
				});
			}, "%/applicationQuitting".format(this.fullPath).asSymbol)
		];
	}

	addApplicationProxy{arg name, addr;
		// "[%] - addApplicationProxy: name: % addr: %".format(this.application.name, name, addr).postln;
		if(this.applicationProxies.includesKey(name).not and: {name != this.name}, {
			var newAppProxy = VTMApplicationProxy(name, nil, (targetAddr: addr), this);
			// "\tAdding app proxy: % - %".format(name, addr).postln;
		}, {
			// "\tApp proxy already registered: % - %".format(name, addr).postln;
		});
	}

	localApplication { ^parent; }

	applicationProxies{
		^children.select({arg it; it.isKindOf(VTMApplicationProxy); });
	}

	applications {
		var result;
		result = this.remoteApplications.copy;
		result.put(this.localApplication.name, this.localApplication);
		^result;
	}

	*sendToAll{arg ...args;
		sendToAllAction.value(*args);
	}
}
*/
