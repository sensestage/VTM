VTMNode {
	var <sceneOwner;
	var <moduleHost;
	var <hardwareSetup;
	var <network;
	var <addr;
	var <name;
	var <namespace;
	var moduleFactory;
	var sceneFactory;

	var oscResponders;

	*new{arg name, addr;
		^super.new.init(name, addr);
	}

	init{arg name_, addr_;
		name = name_.asSymbol;
		addr = addr_;

		namespace = VTMNamespaceElement.new(this, name);
		network = VTMNetwork.new(this);

		moduleHost = VTMModuleHost.new(this);
		sceneOwner = VTMSceneOwner.new(this);
		hardwareSetup = VTMHardwareSetup.new(this);

		sceneFactory = VTMSceneFactory.new(this);
		moduleFactory = VTMModuleFactory.new(this);

		this.makeOSCResponders;
	}

	makeOSCResponders{
		[
			OSCFunc({arg msg, time, addr, port;
				"Got HEI from addr: % [%]".format(addr, port).postln;
				addr.sendMsg('/hallo', name);
			}, '/hei')
		];
	}

	loadModuleCue{arg cue;
		var newModule;
		try{
			newModule = moduleFactory.build(cue);
		} {|err|
			"Module cue build error".warn;
			err.postln;
		};
		moduleHost.addModule(newModule);
	}

	loadSceneCue{arg cue;
		var newScene;
		try{
			newScene = sceneFactory.build(cue);
		} {|err|
			"Scene cue build error".warn;
			err.postln;
		};
		sceneOwner.addScene(newScene);
	}

	runHardwareSetupScript{arg path;
		hardwareSetup.addHardware(path);//mock code
	}

}