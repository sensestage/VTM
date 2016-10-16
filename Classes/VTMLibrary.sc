//A global library of VTM definitions and declarations for modules, scenes and hardware devices.
VTMLibrary : Library{
	*vtmPath{
		^PathName(PathName( this.filenameSymbol.asString ).parentPath).parentPath;
	}

	*initClass{
		this.prLoadGlobalLibrary;
	}

	*prLoadGlobalLibrary{
		[\definitions, \declarations].do({arg whatToLoad;
			var data;
			data = this.loaderFunc(this.vtmPath, \global, whatToLoad);
			data.keysValuesDo({arg key, val;
				this.put(whatToLoad, key, val);
			});
		});

	}

	*loaderFunc {arg loadFolder, libraryLevel, whatToLoad;
		var path = PathName(loadFolder);
		var files, whatToLoadSingular = whatToLoad.asString.drop(-1);
		var result = IdentityDictionary.new;
		path.filesDo({arg file;
			if(file.extension == "scd", {
				if("^.+_%$".format(whatToLoadSingular).matchRegexp(file.fileNameWithoutExtension), {
					files = files.add(file);
				});
			});
		});
		files.do({arg item;
			var name = item.fileNameWithoutExtension.findRegexp("(.+)_.+$")[1][1];
			var data;
			//FIXME: Add check if it compiles here.
			try{
				data = thisProcess.interpreter.compileFile(item.fullPath.asString);
				if(data.isNil, {Error("").throw;});
				switch(whatToLoad,
					\definitions, {
						data = Environment.make(data);
					},
					\descriptions, {
						data = data.value;
					}
				);
				data.put(\pathName, PathName(item.fullPath.asString));
				data.put(\libraryLevel, libraryLevel);
				result.put(name.asSymbol, data);
			} {|err|
				"Could not compile % in file '%'".format(
					whatToLoadSingular,
					item.fullPath.asString
				).warn;
			};
		});
		^result;
	}
}

