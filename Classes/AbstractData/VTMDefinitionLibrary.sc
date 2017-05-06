VTMDefinitionLibrary : VTMElement {

	*managerClass{ ^VTMDefinitionLibraryManager; }

	*new{arg name, attributes, manager;
		^super.new(name, attributes, manager).initDefinitionLibrary;
	}

	initDefinitionLibrary{}

	*attributeKeys{
		^super.attributeKeys ++ [\includedPaths, \excludedPaths];
	}

	*queryNames{
		^super.queryNames ++ [\hasDefinition];
	}

	//Attribute getters and setters
	includedPaths{ ^this.get(\includedPaths); }
	includedPaths_{arg val; this.set(\includedPaths, val); }

	excludedPaths{ ^this.get(\excludedPaths); }
	excludedPaths_{arg val; this.set(\excludedPaths, val); }
}

