VTMSceneFactory{
	var <sceneOwner;

	*new{arg sceneOwner;
		^super.new.init(sceneOwner);
	}

	init{arg sceneOwner_;
		sceneOwner = sceneOwner_;
	}

	build{arg sceneCue;
		var newScene;
		//When scene owner creates a Module Proxy it queries the network for the orginal module.
		//First create as response function for the query, oneShot so its removed after response
		newScene = VTMScene.new(sceneOwner);
		newScene.moduleNames.do({arg modName;
			OSCFunc({arg msg, time, addr, port;
				var modulePath = this.node.network.getNodeNameForAddr(addr);
				this.addModuleProxy(
					VTMModuleProxy.new(newScene, modulePath);
				);
			}, "/module/%!".format(modName).asSymbol).oneShot;
			//Then send the query
			NetAddr("1.2.3.255", 57120).sendMsg("/module/%?".format(modName).asSymbol);
		});
	}


}