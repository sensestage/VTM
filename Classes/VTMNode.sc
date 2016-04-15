VTMNode {
	var <sceneOwner;
	var <moduleHost;
	var <hardwareSetup;
	var <network;
	var <addr;
	var <name;
	var <filePaths;
	var path;

	var oscResponders;

	*new{arg name, addr;
		^super.new.init(name, addr);
	}

	init{arg name_, addr_;
		name = name_.asSymbol;
		addr = addr_;

		filePaths = IdentityDictionary.new;
		filePaths[\vtm] = "VTM_FOLDER".getenv ? PathName(
			PathName(this.class.filenameSymbol.asString).parentPath
		).parentPath;
		filePaths[\moduleDefintions] = filePaths[\vtm] +/+ "ModuleDefintions";
		filePaths[\hardwareDefinitions] = filePaths[\vtm] +/+ "HardwareDefinitions";

		network = VTMNetwork(this);
		moduleHost = VTMModuleHost(this);
		sceneOwner = VTMSceneOwner(this);
		hardwareSetup = VTMHardwareSetup(this);

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

	runHardwareSetupScript{arg path;
		hardwareSetup.addHardware(path);//mock code
	}

	getFilePathFor{arg key;
		^filePaths[key];
	}

	//faking it for the moment
	parent{
		^network;
	}

	leadingSeparator{
		^$/;
	}

	path{
		if(path.isNil, {
			path = (this.leadingSeparator ++ this.name).asSymbol;
		});
		^path;
	}

}