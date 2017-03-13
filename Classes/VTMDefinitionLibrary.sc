VTMDefinitionLibrary : VTMElement {

	*new{arg name, attributes, manager;
		^super.new(name, attributes, manager).initDefinitionLibrary;
	}

	initDefinitionLibrary{}
}

