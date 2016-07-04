VTMApplication {
	var <network;
	var <sceneOwner;
	var <moduleHost;
	var <hardwareSetup;
	var <filePaths;
	var <description;
	var <definition;

	var oscResponders;

	//The network[description\definition] is admittedly strange here, but keeping it for now.
	*new{arg name, description, definition, networkDescription, networkDefinition;
		^super.new.initApplication(name, description, definition, networkDescription, networkDefinition);
	}

	initApplication{arg name_, description_, definition_, networkDescription_, networkDefinition_;
		this.prInitFilePaths;
		description = description_;
		definition = definition_;
		network = VTMNetwork(name_, this, networkDescription_, networkDefinition_);
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

	addPath{arg type, path;

	}

	makeOSCResponders{
	}

	runHardwareSetupScript{arg path;
		hardwareSetup.addHardware(path);//mock code
	}

	getFilePathFor{arg key;
		^filePaths[key];
	}

	name{
		^network.name;
	}

}
