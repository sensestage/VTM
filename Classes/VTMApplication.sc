VTMApplication {
	var <network;
	var <sceneOwner;
	var <moduleHost;
	var <hardwareSetup;
	var <filePaths;
	var <declaration;
	var <definition;
	var <projectFolder;
	var <applicationFolder;

	//The network[declaration\definition] is admittedly strange here, but keeping it for now.
	*new{arg name, declaration, definition, projectFolder, applicationFolder;
		^super.new.initApplication(name, declaration, definition, projectFolder, applicationFolder);
	}

	*loadApplication{arg appFolder, projectFolder;
		if(File.exists(appFolder), {
			var appName;
			var appStartupFilePath;
			appName = appFolder.split.last;
			appStartupFilePath = appFolder +/+ appName ++ "_Startup.scd";
			if(File.exists(appStartupFilePath), {
				var decl, def, env, name;
				env = appStartupFilePath.standardizePath.load;
				decl = env[\declaration];
				def = env[\definition];
				name = env[\name];
				^this.new(appName, decl, def,
					applicationFolder: appFolder,
					projectFolder: projectFolder
				);
			}, {
				"loadApplication - Can't find application startup file '%'!".format(appStartupFilePath);
			});
		}, {
			"loadApplication - Can't find application folder '%'!".format(appFolder);
		});
	}

	initApplication{arg name_, declaration_, definition_, projectFolder_, appFolder_;
		var networkDesc, networkDef;
		var moduleDesc, moduleDef;
		var sceneDesc, sceneDef;
		var hardwareDesc, hardwareDef;
		projectFolder = projectFolder_;
		applicationFolder = appFolder_;
		if(definition_.notNil, {
			definition = Environment.newFrom(definition_.deepCopy);
		}, {
			definition = Environment.new;
		});
		if(declaration_.notNil, {
			declaration = Environment.newFrom(declaration_.deepCopy);
			if(declaration.includesKey(\network), {
				networkDesc = declaration[\network][\declaration];
				networkDef = declaration[\network][\definition];
			});
			if(declaration.includesKey(\module), {
				moduleDesc = declaration[\module][\declaration];
				moduleDef = declaration[\module][\definition];
			});
			if(declaration.includesKey(\scene), {
				sceneDesc = declaration[\scene][\declaration];
				sceneDef = declaration[\scene][\definition];
			});
			if(declaration.includesKey(\hardware), {
				hardwareDesc = declaration[\hardware][\declaration];
				hardwareDef = declaration[\hardware][\definition];
			});
		}, {
			declaration = Environment.new;
		});
		this.prInitFilePaths;
		network = VTMNetwork(name_, this, networkDesc, networkDef);
		hardwareSetup = VTMHardwareSetup(network, hardwareDesc, hardwareDef);
		moduleHost = VTMModuleHost(network, moduleDesc, moduleDef);
		sceneOwner = VTMSceneOwner(network, sceneDesc, sceneDef);

		//Discover other application on the network
		network.discover;
		if(declaration.includesKey(\openView), {
			if(declaration[\openView], {
				var viewDesc, viewDef;
				this.makeView(
					viewDeclaration: declaration[\viewDeclaration],
					viewDefinition: declaration[\viewDefinition]
				);
			});
		});
	}

	quit{
		sceneOwner.free;
		moduleHost.free;
		hardwareSetup.free;
		network.free;
	}

	prLoadProjectFolder{arg pathName;
		if(pathName.isKindOf(PathName) and: pathName.isFolder, {
			projectFolder = pathName;
			this.loadDeclarations;
			this.loadDefinitions;
		}, {
			Error("%:% - Error loading project folder: [%]".format(this.class.name, thisMethod.name, pathName)).throw;
		});
	}

	loadLibrary{
		[hardwareSetup, moduleHost, sceneOwner].do({arg item;
			item.loadLibrary;
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

	name{
		^network.name;
	}

	makeView{arg parent, bounds, viewDeclaration, viewDefinition;
		^VTMApplicationView.new(
			parent, bounds, this, viewDeclaration, viewDefinition
		);
	}

	*makeProjectFolder{arg name, path;
		var projectFolder,appFolder;
		projectFolder = path +/+ name;
		if(File.exists(projectFolder).not, {
			File.mkdir(projectFolder);
			appFolder = projectFolder +/+ "Applications"; 
			File.mkdir(appFolder);
			["Definitions", "Declarations"].do({arg item;
				var folder;
				folder = projectFolder +/+ item;
				File.mkdir(folder);

				["Modules", "Devices", "Scenes"].do({arg jtem;
					var subfolder = folder +/+ jtem;
					File.mkdir(subfolder);
				});
			});
		}, {
			"Project folder '%' already exists".format(projectFolder).warn;
		});
	}

	*makeApplicationFolder{arg name, projectFolder;
		var appFolder = projectFolder +/+ "Applications" +/+ name;
		if(File.exists(appFolder).not, {
			var appStartupFilePath = appFolder +/+ "%_Startup.scd".format(name);
			var appStartupFile;
			//Make the application folder
			File.mkdir(appFolder);

			//Make the application startup file
			appStartupFile = File.new(appStartupFilePath, "w");
			if(appStartupFile.isOpen, {
				appStartupFile.putString("(" ++ Char.nl);
				appStartupFile.putString("\t" ++ "declaration: ( )," ++ Char.nl);
				appStartupFile.putString("\t" ++ "definition: (" ++ Char.nl);
				appStartupFile.putString("\t\t" ++ "prepare: {arg app;" ++ Char.nl);
				appStartupFile.putString("\t\t\t" ++ "\"Application prepare\".postln;" ++ Char.nl);
				appStartupFile.putString("\t\t" ++ "}," ++ Char.nl);
				appStartupFile.putString("\t\t" ++ "run: {arg app;" ++ Char.nl);
				appStartupFile.putString("\t\t\t" ++ "\"Application run\".postln;" ++ Char.nl);
				appStartupFile.putString("\t\t" ++ "}," ++ Char.nl);
				appStartupFile.putString("\t\t" ++ "free: {arg app;" ++ Char.nl);
				appStartupFile.putString("\t\t\t" ++ "\"Application free\".postln;" ++ Char.nl);
				appStartupFile.putString("\t\t" ++ "}," ++ Char.nl);
				appStartupFile.putString("\t" ++ ")" ++ Char.nl);
				appStartupFile.putString(");");
				appStartupFile.close;
			}, {
				"Making new application startup file failed!".warn;
			});

			//Make folders for declarations and definitions
			["Definitions", "Declarations"].do({arg item;
				var folder;
				folder = appFolder +/+ item;
				File.mkdir(folder);

				["Modules", "Devices", "Scenes"].do({arg jtem;
					var subfolder = folder +/+ jtem;
					File.mkdir(subfolder);
				});
			});
		}, {
			"Application folder '%' already existst".format(appFolder).warn;
		});
	}

	*vtmPath{
	   	^PathName(PathName( VTMApplication.filenameSymbol.asString ).parentPath).parentPath;
	}

	applicationPath{
	}
}
