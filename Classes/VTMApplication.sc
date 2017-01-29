VTMApplication {
	//The environment where Application level things are happening.
	var <envir;

	//The object that handles communication with other applications on the network.
	var <network;

	//Manages scenes
	var <sceneOwner;

	//Host local static modules
	var <moduleHost;

	// All hardware devices are managed by this object.
	var <hardwareSetup;

	//The loaded attributes of setting for the application.
	var <attributes;

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

	*new{arg name, definition, attributes, projectFolder, applicationFolder, onPrepared, onRunning;
		^super.new.initApplication(name, definition, attributes, projectFolder, applicationFolder, onPrepared, onRunning);
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
				decl = env[\attributes];
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

	initApplication{arg name_, definition_, attributes_, projectFolder_, appFolder_, onPrepared_, onRunning_;
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
		if(attributes_.notNil, {
			attributes = Environment.newFrom(attributes_.deepCopy);
			if(attributes.includesKey(\network), {
				networkDesc = attributes[\network][\attributes];
				networkDef = attributes[\network][\definition];
			});
			if(attributes.includesKey(\moduleHost), {
				moduleDesc = attributes[\moduleHost][\attributes];
				moduleDef = attributes[\moduleHost][\definition];
			});
			if(attributes.includesKey(\sceneOwner), {
				sceneDesc = attributes[\sceneOwner][\attributes];
				sceneDef = attributes[\sceneOwner][\definition];
			});
			if(attributes.includesKey(\hardwareSetup), {
				hardwareDesc = attributes[\hardwareSetup][\attributes];
				hardwareDef = attributes[\hardwareSetup][\definition];
			});
		}, {
			attributes = Environment.new;
		});

		network = VTMNetwork(name_, networkDef, networkDesc, this);
		hardwareSetup = VTMHardwareSetup(hardwareDef, hardwareDesc, network);
		moduleHost = VTMModuleHost(moduleDef, moduleDesc, network);
		sceneOwner = VTMSceneOwner(sceneDef, sceneDesc, network);

		//Discover other application on the network
		network.discover;
		if(attributes.includesKey(\openView), {
			if(attributes[\openView], {
				var viewDesc, viewDef;
				this.makeView( attributes[\viewDefinition], attributes[\viewAttributes] );
			});
		});

		condition = Condition.new;
		fork{
			this.prepare(condition, onPrepared_);
			this.run(condition, onRunning_);
		}
	}

	//Call functions in the runtime environment with the application as first arg.
	//The method returns the result from the called function.
	execute{arg selector ...args;
		^envir[selector].value(this, *args);
	}

	prepare{arg cond, onPrepared;
		var condition = cond ? Condition.new;
		forkIfNeeded{
			//boot scsynth if defined
			if(attributes.includesKey(\startServer), {
				var serverOptions;
				if(attributes[\startServer], {

					if(attributes.includesKey(\serverOptions), {
						serverOptions = ServerOptions.new;
						attributes[\serverOptions].keysValuesDo({arg opt, val;
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
			onPrepared.value(this);

		};
	}

	run{arg cond, onRunning;
		network.discover;
		onRunning.value(this);
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
			this.loadAttributes;
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

	makeView{arg parent, bounds, viewDefinition, viewAttributes;
		^VTMApplicationView.new(
			parent, bounds, this, viewDefinition, viewAttributes
		);
	}

	*makeProjectFolder{arg name, path;
		var projectFolder,appFolder;
		projectFolder = path +/+ name;
		if(File.exists(projectFolder).not, {
			File.mkdir(projectFolder);
			appFolder = projectFolder +/+ "Applications";
			File.mkdir(appFolder);
			["Definitions", "Attributes"].do({arg item;
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
				appStartupFile.putString("\t" ++ "attributes: ( )," ++ Char.nl);
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

			//Make folders for attributes and definitions
			["Definitions", "Attributes"].do({arg item;
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
