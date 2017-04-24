VTMDefinitionLibrary : VTMElement {

	*managerClass{ ^VTMDefinitionLibraryManager; }

	*new{arg name, attributes, manager;
		^super.new(name, attributes, manager).initDefinitionLibrary;
	}

	initDefinitionLibrary{}
}

