VTMModuleHost : VTMComposableContextManager {
	var <factory;

	*dataClass{ ^VTMModule; }
	name{ ^\modules; }

	initModuleHost {
		factory = VTMModuleFactory.new(this);
	}

	free{
		factory.free;
		super.free;
	}

	//A attributes is a dictonary of parameter key/values.
	//The attributes may define the name of a module definition.
	//This definition may be overriden by the optional moduleDefinition argument
	//that expects an Environment where the prepare, start, and free methods are defined.
	//The definition can also define functions for building parameters (~buildParameters)
	//
//	loadModuleAttributes{arg moduleDefinition, attributes;
//		var newModule;
//		try{
//			newModule = factory.build(moduleDefinition, attributes);
//
//			//The factory may throw error when building the module, but
//			//added an extra check here
//			if(newModule.isNil, {
//				Error("Module % failed to build".format(this.name)).throw;
//			}, {
//				this.loadModule(newModule);
//			});
//		} {|err|
//			"Module build error for module name: '%'".format(attributes[\name]).warn;
//			err.throw;
//		};
//		^newModule;
//	}
//
//	loadModuleJSONCue{arg moduleDefinition, attributesJSONString;
//		var moduleAttributes;
//		//parse JSON string
//		try{
//			moduleAttributes = attributesJSONString.parseYAML.changeScalarValuesToDataTypes.asIdentityDictionaryWithSymbolKeys;
//		} {|err|
//			"Module JSON cue parser error".warn;
//			err.postln;
//		};
//		this.loadModuleAttributes(moduleDefinition, moduleAttributes);
//	}
//
//	//can be either a .json file or a .scd file
//	loadModuleFile{arg path;
//		var pathName = PathName(path);
//		if(File.exists(path), {
//			var file;
//			try{
//				var fileData;
//				file = File.new(pathName.absolutePath);
//				if(file.isOpen.not, {
//					Error("Failed to open file: %".format(pathName)).throw;
//				});
//				switch(pathName.extension,
//					\json, {
//						fileData = pathName.absolutePath.parseYAMLFile;
//					},
//					\scd, {
//						var cueFunc;
//						cueFunc = thisProcess.interpreter.compileFile(pathName.asAbsolutePath);
//						if(cueFunc.isNil, { Error("Failed compiling cue file: %".format(pathName)).throw; });
//						Environment.make(cueFunc);
//					},
//					{ Error("Wrong file type: %".format(pathName)).throw; }
//				);
//				file.close;
//			} {|err|
//				"Error reading file".warn;
//				err.postln;
//			};
//		}, {
//			"Module file not found: %".format(pathName).warn;
//		});
//	}
//
//	loadModule{arg module;
//		if(module.isKindOf(VTMModule), {
//			this.addChild(module);
//		}, {|err|
//			"Tried to load object of wrong type: %".format(module).warn;
//			err.postln;
//		});
//	}
//
//	prepare{arg condition;
//		forkIfNeeded{
//			if(attributes.includesKey(\modules), {
//				attributes[\modules].do({arg modDec;
//					var mod;
//					mod = this.loadModuleAttributes(modDec);
//					if(mod.notNil, {
//						mod.prepare(condition);
//						mod.run(condition);
//					});
//				});
//			});
//		};
//	}
//

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
