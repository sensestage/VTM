VTMMapping : VTMElement {
	*managerClass{ ^VTMMappingManager; }

	*new{arg name, declaration, manager;
		^super.new(name, declaration, manager).initMapping;
	}

	initMapping{}

	*parameterDescriptions{
		^super.parameterDescriptions.putAll(
			VTMOrderedIdentityDictionary[
				(name: \source, type: \string, optional: false),
			   	(name: \destination, type: \string, optional: false)
			]
		); 
	}

	*attributeDescriptions{
		^super.attributeDescriptions.putAll(
			VTMOrderedIdentityDictionary[
				(name: \enabled, type: \boolean, defaultValue: true)
			]
		); 
	}
}
