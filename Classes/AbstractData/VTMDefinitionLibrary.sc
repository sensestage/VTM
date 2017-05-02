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
}

