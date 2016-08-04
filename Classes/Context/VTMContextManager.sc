VTMNetworkedContext : VTMContext {
	var network;
	var <library;//temp getter
	var <definitionPaths, <isLoadingDefinitions = false;
	var <declarationPaths, <isLoadingDeclarations = false;

	*new{arg name, network, declaration, definition;
		^super.new(name, network, declaration, definition).initContextManager;
	}

	initContextManager {
		library = IdentityDictionary[
			\definitions -> IdentityDictionary[
				\global -> IdentityDictionary.new,
				\project -> IdentityDictionary.new,
				\application -> IdentityDictionary.new,
				\runtime -> IdentityDictionary.new
			],
			\declarations -> IdentityDictionary[
				\global -> IdentityDictionary.new,
				\project -> IdentityDictionary.new,
				\application -> IdentityDictionary.new,
				\runtime -> IdentityDictionary.new
			]
		];
		definitionPaths = [];
		declarationPaths = [];

		if(declaration.notNil, {
			if(declaration.includesKey(\definitionPaths), {
				var paths = declaration[\definitionPaths].asArray;
				paths.do({arg item; this.addDefinitionPath(item); });
			});

			if(declaration.includesKey(\declarationPaths), {
				var paths = declaration[\declarationPaths].asArray;
				paths.do({arg item; this.addDeclarationPath(item); });
			});
		});
	}

	network{
		^this.parent;
	}

	application{
		^this.network.application;
	}

	getDeclaration{arg key;
		^this.prGetLibraryEntry(\declaration, key);
	}

	getDefinition{arg key;
		^this.prGetLibraryEntry(\definition, key);
	}

	//whatToGet is either \declaration or \definition
	prGetLibraryEntry{arg whatToGet, key;
		var lib, result;
		switch(whatToGet, 
			\declaration, {lib = library[\declarations];},
			\definition, {lib = library[\definitions];}
		);
		result = lib[\runtime].atFail(key, {
			lib[\application].atFail(key, {
				lib[\project].atFail(key, {
					lib[\global].at(key)
				});
			});
		});
		^result;
	}

	loadLibrary{
		this.prLoadToLibrary(\declarations);
		this.prLoadToLibrary(\definitions);
	}
	
	//whatToLoad is either \definitions or \declarations
	prLoadToLibrary{arg whatToLoad; 
		var folder = VTMApplication.vtmPath.asString;
		var subfolder = whatToLoad.asString.capitalize +/+ this.name.asString.capitalize;
		var loaderFunc = {arg loadFolder;
			var path = PathName(loadFolder);
			var files = path.entries.select({arg file;
				file.extension == "scd";
			});
			var result = IdentityDictionary.new;
			files.do({arg item;
				var name = item.fileNameWithoutExtension;
				var data = item.fullPath.asString.load;
				result.put(name.asSymbol, data);
			});
			result;
		};
		//Load global stuff
		folder = folder +/+ subfolder;
		"Loading global % in '%'".format(whatToLoad, folder).postln;
		library[whatToLoad][\global].putAll(loaderFunc.value(folder));

		//Load project stuff
		if(this.application.projectFolder.notNil, {
			folder = this.application.projectFolder +/+ subfolder;
			"Loading '%' project for app '%' - % at folder '%'".format(
				this.application.projectFolder.split.last,
				this.application.name,
				whatToLoad,
				folder
			).postln;
			library[whatToLoad][\project].putAll(loaderFunc.value(folder));
		},{
			"Application '%' is not in a project".postln;
		});

		//Load application stuff
		if(this.application.applicationFolder.notNil, {
			folder = this.application.applicationFolder +/+ subfolder;
			"Loading % for application '%' in folder '%'".format(
				whatToLoad,
				this.application.name,
				folder
			).postln;
			library[whatToLoad][\application].putAll(loaderFunc.value(folder));
		}, {
			"Application '%' is not in a folder".postln;
		});
		"Resulting LIBRARY: %".format(library[whatToLoad]).postln;
	}
}
