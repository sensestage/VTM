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
	*new{arg name, description, definition;
		^super.new.initApplication(name, description, definition);
	}

	initApplication{arg name_, description_, definition_;
		var networkDesc, networkDef;
		var moduleDesc, moduleDef;
		var sceneDesc, sceneDef;
		var hardwareDesc, hardwareDef;
		if(description.notNil, {
			if(description.includesKey(\network), {
				networkDesc = description[\network][\description];
				networkDef = description[\network][\definition];
			});
			if(description.includesKey(\network), {
				moduleDesc = description[\module][\description];
				moduleDef = description[\module][\definition];
			});
			if(description.includesKey(\network), {
				sceneDesc = description[\scene][\description];
				sceneDef = description[\scene][\definition];
			});
			if(description.includesKey(\network), {
				hardwareDesc = description[\hardware][\description];
				hardwareDef = description[\hardware][\definition];
			});
		});
		this.prInitFilePaths;
		description = description_;
		definition = definition_;
		network = VTMNetwork(name_, this, networkDesc, networkDef);
		moduleHost = VTMModuleHost(network, moduleDesc, moduleDef);
		sceneOwner = VTMSceneOwner(network, sceneDesc, sceneDef);
		hardwareSetup = VTMHardwareSetup(network, hardwareDesc, hardwareDef);

		this.makeOSCResponders;

		//Discover other application on the network
		network.discover;
		if(description.notNil, {
			if(description.includeKey(\openView), {
				if(description[\openView], {
					var viewDesc, viewDef;
					this.makeView(
						viewDescription: description[\viewDescription],
						viewDefinition: description[\viewDefinition]
					);
				});
			});
		});
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

	makeView{arg parent, bounds, viewDescription, viewDefinition;
		^VTMApplicationView.new(
			parent, bounds, this, viewDescription, viewDefinition
		);
	}
}
