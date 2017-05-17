VTMMapping : VTMElement {
	*managerClass{ ^VTMMappingManager; }

	*new{arg name, declaration, manager;
		^super.new(name, declaration, manager).initMapping;
	}

	initMapping{}

	*parameterDescriptions{
		^super.parameterDescriptions.putAll(
			VTMOrderedIdentityDictionary[
				(name: \source, type: \string),
			   	(name: \destination, type: \string),
				(name: \enabled, type: \boolean)
			]
		); 
	}
}
