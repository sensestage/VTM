VTMModuleFactory{
	var <host;

	*new{arg host;
		^super.new.init(host);
	}

	init{arg host_;
		host = host_;
	}

	build{arg name, description, def;
		var moduleDefinition;
		var newModule;

		//Load module definition from file if not defined in arg
		if(def.isNil, {
			var defName, defPath;
			//Module description must define a def name, if not defined in arg
			if(description.includesKey(\def).not, {
				Error("Module % missing module definition").throw;
				^nil;
			});

			//Load def from file
			defName = description[\def];
			//Search for mod def file
			defPath = PathName(this.moduleDefinitionsFolder).deepFiles.detect {|path|
				path.fileNameWithoutExtension == defName;
			};
			if(defPath.isNil) {
				Error(
					"Could not find module definition for '%'".format(
						defName
					)
				).throw;
				^nil;
			};

			//Check if module definition compiles
			try{
				var defFunc;
				defFunc = thisProcess.interpreter.compileFile(defPath.asAbsolutePath);
				if(defFunc.isNil, { Error("").throw; });
				moduleDefinition = Environment.make(defFunc);
			} {|err|
				Error(
					"Could not compile module definition in '%'".format(
						defPath.asAbsolutePath
					)
				).throw;
				^nil;
			};

		}, {
			moduleDefinition = def;
		});

		newModule = VTMModule.new(name, host, description, moduleDefinition);
		^newModule;

	}

	loadModuleDefinitionFromFile{arg filepath;

	}

	moduleDefinitionsFolder{
		^host.node.getFilePathFor(\moduleDefinition);
	}
}