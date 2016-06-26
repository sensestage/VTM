VTMModuleHost : VTMDynamicContextManager {
	var <factory;

	*new{arg name, parent, description, defintion;
		^super.new(name, parent, description, defintion).initModuleHost;
	}

	initModuleHost {
		factory = VTMModuleFactory.new(this);
	}

	modules{
		^children;
	}

	//A description is a dictonary of parameter key/values.
	//The description may define the name of a module definition.
	//This defintion may be overriden by the optional moduleDefinition argument
	//that expects an Environment where the prepare, start, and free methods are defined.
	//The definition can also define functions for building parameters (~buildParameters)
	//
	loadModuleDescription{arg name, description, moduleDefinition;
		var newModule;
		try{
			newModule = factory.build(name, description, moduleDefinition);

			//The factory may throw error when building the module, but
			//added an extra check here
			if(newModule.isNil, {
				Error("Module % failed to build".format(name)).throw;
				}, {
					this.loadModule(newModule);
			});
		} {|err|
			"Module cue build error".warn;
			err.throw;
		};
	}

	loadModuleJSONCue{arg name, descriptionJSONString, moduleDefinition;
		var moduleDescription;
		//parse JSON string
		try{
			moduleDescription = descriptionJSONString.parseYAML.changeScalarValuesToDataTypes.asIdentityDictionaryWithSymbolKeys;
		} {|err|
			"Module JSON cue parser error".warn;
			err.postln;
		};
		this.loadModuleDescription(name, moduleDescription, moduleDefinition);
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
			this.addChild(module);
			}, {|err|
				"Tried to load object of wrong type: %".format(module).warn;
				err.postln;
		});
	}


	//TODO:
	//When a module is added, create a query owner responders e.g. '/module/kjeppen?'
	/*	OSCFunc({arg msg, time, addr, port;
	var replyPath;
	//find which child sent the query, ask the network
	//....
	replyPath = "/module/%!".format(module.name).asSymbol;

	addr.sendMsg(replyPath, module.absolutePath);
	}, '/module/kjeppen?')
	*/
}
