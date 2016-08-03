VTMNetworkedContext : VTMContext {
	var <network;
	var <library;
	var <definitions /*temp getter*/, <definitionPaths, <isLoadingDefinitions = false;
	var <declarations /*temp getter*/, <declarationPaths, <isLoadingDeclarations = false;

	*new{arg name, network, declaration, definition;
		^super.new(name, network, declaration, definition).initContextManager;
	}

	initContextManager {
		library = IdentityDictionary[
			\definitions -> IdentityDictionary.new,
			\declarations -> IdentityDictionary.new
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

	application{
		^this.network.application;
	}

	prAddPath{arg what, pathName, reload = true;
		var whatArray;
		switch(what, 
			\declaration, { whatArray = declarationPaths; },
			\definition, { whatArray = definitionPaths; },
			{ Error("%:% - wrong argument 'what': [%]".format(this.class.name, thisMethod.name, what)).throw; }
		);
		if(pathName.isKindOf(PathName), {
			if(pathName.isFolder, {
				if(pathName.pathMatch.notEmpty, {
					whatArray = declarationPaths.add(pathName.asString);
					if(reload, { this.perform("load%s".format(what.asString.capitalize).asSymbol); });
					this.changed("%Paths".format(what).asSymbol);
				}, {
					Error("%:% - % path is not found: [%]".format(this.class.name, thisMethod.name, what, pathName)).throw;
				});
			}, {
				Error("%:% - % path is not a folder: [%]".format(this.class.name, thisMethod.name, what, pathName)).throw;
			});
		}, {
			Error("%:% - Error loading % folder: [%]".format(this.class.name, thisMethod.name, what, pathName)).throw;
		});
	}

	addDeclarationPath{arg pathName, reload = true;
		this.prAddPath(\declaration, pathName, reload);
	}

	addDefinitionPath{arg pathName, reload = true;
		this.prAddPath(\definition, pathName, reload);
	}

	prRemovePath{arg what, pathString, reload = true;
		var whatArray;
		switch(what, 
			\declaration, { whatArray = declarationPaths; },
			\definition, { whatArray = definitionPaths; },
			{ Error("%:% - wrong argument 'what': [%]".format(this.class.name, thisMethod.name, what)).throw; }
		);
		if(whatArray.includesEqual(pathString), {
			whatArray.removeAt(whatArray.indexOfEqual(pathString));
			if(reload, { this.perform("load%s".format(what.asString.capitalize).asSymbol); });
			this.changed("%Paths".format(what).asSymbol);
		}, {
			"%:% - Did not find path for 'pathString': %".format(this.class.name, thisMethod.name, pathString).warn;
		});
	}

	removeDeclarationsPath{arg pathString, reload = true;
		this.prRemovePath(\declaration, pathString, reload);
	}

	removeDefintionsPath{arg pathString, reload = true;
		this.prRemovePath(\definition, pathString, reload);
	}

	loadDeclarations{
		//set loading state flag to 'true' in case other threads are trying to read declarations
		isLoadingDeclarations = true; 
		"[%] - LOAD DECLARATIONS".format(this.name).postln;
		declarationPaths.do({arg folder;
			var folderPath = PathName(folder);
			folderPath.filesDo({arg aFile;
				"Loading declaration: %".format(aFile).postln;
			});
		});
		"LOADING DECLARATIONS DONE!".postln;
		//unset loading state flag
		isLoadingDeclarations = false;
	}

	loadDefinitions{
		//set loading state flag to 'true' in case other threads are trying to read declarations
		isLoadingDeclarations = true; 
		"[%] - LOAD DEFINITIONS".format(this.name).postln;
		declarationPaths.do({arg folder;
			var folderPath = PathName(folder);
			folderPath.filesDo({arg aFile;
				"Loading declaration: %".format(aFile).postln;
			});
		});
		"LOADING DEFINITIONS DONE!".postln;
		//unset loading state flag
		isLoadingDeclarations = false;
	}
}
