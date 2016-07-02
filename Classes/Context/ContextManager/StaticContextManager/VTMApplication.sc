VTMApplication : VTMStaticContextManager {
	var <sceneOwner;
	var <moduleHost;
	var <hardwareSetup;
	var <network;
	var <filePaths;
	var path;


	var oscResponders;

	*new{arg name, parent, description, defintion;
		^super.new(name, parent, description, defintion).initApplication;
	}

	initApplication{
		this.prInitFilePaths;

		network = VTMNetwork(this);
		moduleHost = VTMModuleHost(this);
		sceneOwner = VTMSceneOwner(this);
		hardwareSetup = VTMHardwareSetup(this);

		this.makeOSCResponders;

		//Discover other application on the network
		network.discover;
	}

	prInitFilePaths{
		filePaths = IdentityDictionary.new;
		filePaths[\vtm] = PathName(
			PathName(this.class.filenameSymbol.asString).parentPath
		).parentPath;
		filePaths[\moduleDefintions] = filePaths[\vtm] +/+ "ModuleDefintions";
		filePaths[\hardwareDefinitions] = filePaths[\vtm] +/+ "HardwareDefinitions";
	}

	

	makeOSCResponders{
		[
			OSCFunc({arg msg, time, addr, port;
				"Got HEI from addr: % [%]".format(addr, port).postln;
				addr.sendMsg('/hallo', name);
			}, '/hei'),
			OSCFunc({//network discover responder

				}, '/?')
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
