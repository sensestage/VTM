VTMNetworkedContext : VTMContext {
	var <library;//temp getter
	var <definitionPaths, <isLoadingDefinitions = false;
	var <attributesPaths, <isLoadingAttributes = false;

	*new{arg name, definition, attributes, network;
		^super.new(name, definition, attributes, network).initNetworkedContext;
	}

	initNetworkedContext {
		library = IdentityDictionary[
			\definitions -> IdentityDictionary[
				\project -> IdentityDictionary.new,
				\application -> IdentityDictionary.new,
				\runtime -> IdentityDictionary.new
			],
			\attributes -> IdentityDictionary[
				\project -> IdentityDictionary.new,
				\application -> IdentityDictionary.new,
				\runtime -> IdentityDictionary.new
			]
		];
		definitionPaths = [];
		attributesPaths = [];

		if(attributes.notNil, {
			if(attributes.includesKey(\definitionPaths), {
				var paths = attributes[\definitionPaths].asArray;
				paths.do({arg item; this.addDefinitionPath(item); });
			});

			if(attributes.includesKey(\attributesPaths), {
				var paths = attributes[\attributesPaths].asArray;
				paths.do({arg item; this.addAttributesPath(item); });
			});
		});
		this.loadLibrary;
	}

	network{
		^this.parent;
	}

	application{
		^this.network.application;
	}

	getAttributes{arg key;
		^this.prGetLibraryEntry(\attributes, key);
	}

	getDefinition{arg key;
		^this.prGetLibraryEntry(\definition, key);
	}

	//whatToGet is either \attributes or \definition
	prGetLibraryEntry{arg whatToGet, key;
		var lib, result;
		switch(whatToGet,
			\attributes, {lib = library[\attributes];},
			\definition, {lib = library[\definitions];}
		);
		result = lib[\runtime].atFail(key, {
			lib[\application].atFail(key, {
				lib[\project].atFail(key, {
					VTMLibrary.at(whatToGet, key);
				});
			});
		});
		if(result.notNil, {result = result.deepCopy; });
		^result;
	}

	loadLibrary{
		this.prLoadToLibrary(\attributes);
		this.prLoadToLibrary(\definitions);
	}

	//whatToLoad is either \definitions or \attributes
	prLoadToLibrary{arg whatToLoad;
		var folder = VTMLibrary.vtmPath.asString;
		var subfolder = whatToLoad.asString.capitalize +/+ this.name.asString.capitalize;

		// //Load global stuff
		// folder = folder +/+ subfolder;
		// "Loading global % in '%'".format(whatToLoad, folder).postln;
		// library[whatToLoad][\global].putAll(VTMLibrary.loaderFunc(folder, \global));

		//Load project stuff
		if(this.application.projectFolder.notNil, {
			folder = this.application.projectFolder +/+ subfolder;
			"Loading '%' project for app '%' - % at folder '%'".format(
				this.application.projectFolder.split.last,
				this.application.name,
				whatToLoad,
				folder
			).postln;
			library[whatToLoad][\project].putAll(VTMLibrary.loaderFunc(folder, \project));
		},{
			// "Application '%' is not in a project".format(this.application.name).postln;
		});

		//Load application stuff
		if(this.application.applicationFolder.notNil, {
			folder = this.application.applicationFolder +/+ subfolder;
			"Loading % for application '%' in folder '%'".format(
				whatToLoad,
				this.application.name,
				folder
			).postln;
			library[whatToLoad][\application].putAll(VTMLibrary.loaderFunc(folder, \application));
		}, {
			// "Application '%' is not in a folder".format(this.application.name).postln;
		});
		// "Resulting LIBRARY: %".format(library[whatToLoad]).postln;
	}
}
