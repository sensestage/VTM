VTMApplication {
	//The environment where all Application level things are happening.
	var <envir;

	//The object that handles communication with other applications on the network.
	var <network;

	//Manages scenes
	var <sceneOwner;

	//Host local static modules
	var <moduleHost;

	// All hardware devices are managed by this object.
	var <hardwareSetup;

	//The loaded declaration of setting for the application.
	var <declaration;

	//A prototype of an environment (that will be copied into 'envir', where you e.g. can add special functonality
	//for the application.
	var <definition;

	var <projectFolder;

	//The folder where the application files are stored.
	var <applicationFolder;

	//Used for handling async events.
	var condition;

	//The scsynth for this application
	var <server;

	*new{arg name, definition, declaration, projectFolder, applicationFolder;
		^super.new.initApplication(name, definition, declaration, projectFolder, applicationFolder);
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
				^this.new(appName, def, decl,
					applicationFolder: appFolder,
					projectFolder: projectFolder
				);
			}, {
				"loadApplication - Can't find application startup file '%'!".format(appStartupFilePath).warn;
			});
		}, {
			"loadApplication - Can't find application folder '%'!".format(appFolder).warn;
		});
	}

	initApplication{arg name_, definition_, declaration_, projectFolder_, appFolder_;
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
		envir = definition.deepCopy;
		if(declaration_.notNil, {
			declaration = Environment.newFrom(declaration_.deepCopy);
			if(declaration.includesKey(\network), {
				networkDesc = declaration[\network][\declaration];
				networkDef = declaration[\network][\definition];
			});
			if(declaration.includesKey(\moduleHost), {
				moduleDesc = declaration[\moduleHost][\declaration];
				moduleDef = declaration[\moduleHost][\definition];
			});
			if(declaration.includesKey(\sceneOwner), {
				sceneDesc = declaration[\sceneOwner][\declaration];
				sceneDef = declaration[\sceneOwner][\definition];
			});
			if(declaration.includesKey(\hardwareSetup), {
				hardwareDesc = declaration[\hardwareSetup][\declaration];
				hardwareDef = declaration[\hardwareSetup][\definition];
			});
		}, {
			declaration = Environment.new;
		});

		network = VTMNetwork(name_, this, networkDef, networkDesc);
		hardwareSetup = VTMHardwareSetup(network, hardwareDef, hardwareDesc);
		moduleHost = VTMModuleHost(network, moduleDef, moduleDesc);
		sceneOwner = VTMSceneOwner(network, sceneDef, sceneDesc);

		//Discover other application on the network
		//network.discover;
		if(declaration.includesKey(\openView), {
			if(declaration[\openView], {
				var viewDesc, viewDef;
				this.makeView( declaration[\viewDefinition], declaration[\viewDeclaration] );
			});
		});

		condition = Condition.new;
		fork{
			this.prepare(condition);
			this.run(condition);
		}
	}

	//Call functions in the runtime environment with the application as first arg.
	//The method returns the result from the called function.
	execute{arg selector ...args;
		^envir[selector].value(this, *args);
	}

	prepare{arg cond;
		var condition = cond ? Condition.new;
		forkIfNeeded{
			//boot scsynth if defined
			if(declaration.includesKey(\startServer), {
				var serverOptions;
				if(declaration[\startServer], {

					if(declaration.includesKey(\serverOptions), {
						serverOptions = ServerOptions.new;
						declaration[\serverOptions].keysValuesDo({arg opt, val;
							serverOptions.perform(opt.asSetter, val);
						});
					});
					condition.test = false;
					// server = Server(
					// 	this.name,
					// 	NetAddr(this.addr.hostname, this.addr.port + 10),
					// 	serverOptions
					// );
					//change to this.bootServer later in order to use multiple apps on one computer
					server = Server.default;
					if(serverOptions.notNil, {
						server.options = serverOptions;
					});
					// server.doWhenBooted(
					// 	onComplete: {
					// 		condition.test = true;
					// 		condition.signal;
					// 	}
					// );
					server.waitForBoot(
						onFailure: {
							Error("ScSynth server failed to boot").throw;
						}
					);
				})
			});

			//Start hardware devices

			//Start modules
			//			moduleHost.prepare(condition);

			//Start scenes

			//Prepare its own envir last as it may depend on other things
			//to be initialized first.
			//			this.execute(\prepare, condition);

		};
	}

	run{

	}

	free{
		this.execute(\free, condition);
		sceneOwner.free(condition);
		moduleHost.free(condition);
		hardwareSetup.free(condition);
		network.free(condition);
	}

	quit{
		this.free;
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

	name{
		^network.name;
	}

	addr{ ^network.addr; }

	makeView{arg parent, bounds, viewDefinition, viewDeclaration;
		^VTMApplicationView.new(
			parent, bounds, this, viewDefinition, viewDeclaration
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
				appStartupFile.putString("\t\t\t" ++ "\"Application '%' prepare\".postln;".format(name) ++ Char.nl);
				appStartupFile.putString("\t\t" ++ "}," ++ Char.nl);
				appStartupFile.putString("\t\t" ++ "run: {arg app;" ++ Char.nl);
				appStartupFile.putString("\t\t\t" ++ "\"Application '%' run\".postln;".format(name) ++ Char.nl);
				appStartupFile.putString("\t\t" ++ "}," ++ Char.nl);
				appStartupFile.putString("\t\t" ++ "free: {arg app;" ++ Char.nl);
				appStartupFile.putString("\t\t\t" ++ "\"Application '%' free\".postln;".format(name) ++ Char.nl);
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

	applicationPath{
	}
}
