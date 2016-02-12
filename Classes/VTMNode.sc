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
		this.loadModule(newModule);
	}

	loadModuleJSONCue{arg cueString;
		var moduleCue;
		//parse JSON string
		try{
			moduleCue = cueString.parseYAML.changeScalarValuesToDataTypes.asIdentityDictionaryWithSymbolKeys;
		} {|err|
			"Module JSON cue parser error".warn;
			err.postln;
		};
		this.loadModuleCue(moduleCue);
	}

	//can be either a .json file or a .scd file
	loadModuleFile{arg path;
		var pathName = PathName(path);
		if(File.exists(path), {
			var file;
			try{
				var fileData;
				file = File.new(pathName.absolutePath);
				if(file.isOpen.not, {
					Error("Failed to open file: %".format(pathName)).throw;
				});
				switch(pathName.extension,
					\json, {
						fileData = pathName.absolutePath.parseYAMLFile;
					},
					\scd, {
						var cueFunc;
						cueFunc = thisProcess.interpreter.compileFile(pathName.asAbsolutePath);
						if(cueFunc.isNil, { Error("Failed compiling cue file: %".format(pathName)).throw; });
						Environment.make(cueFunc);
					},
					{ Error("Wrong file type: %".format(pathName)).throw; }
				);
				file.close;
			} {|err|
				"Error reading file".warn;
				err.postln;
			};
		}, {
			"Module file not found: %".format(pathName).warn;
		});
	}

	loadModule{arg module;
		if(module.isKindOf(VTMModule), {
			moduleHost.addModule(module);
		}, {|err|
			"Tried to load object of wrong type: %".format(module).warn;
			err.postln;
		});
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