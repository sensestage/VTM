VTMMapping : VTMElement {
	*managerClass{ ^VTMMappingManager; }

	*new{arg name, attributes, manager;
		^super.new(name, attributes, manager).initMapping;
	}

	initMapping{}

	when{arg what, action;
		//e.g. when parameter \freq > 900, action.value
	}

	*attributeKeys{
		^super.attributeKeys ++ [\source, \destination, \when, \settings];
	}

	*commandNames{
		^super.commandNames ++ [\enable, \disable];
	}

	*queryNames{
		^super.queryNames ++ [\isEnabled];
	}
}
