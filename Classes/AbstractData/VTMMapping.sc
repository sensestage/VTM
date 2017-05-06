VTMMapping : VTMElement {
	*managerClass{ ^VTMMappingManager; }

	*new{arg name, attributes, manager;
		^super.new(name, attributes, manager).initMapping;
	}

	initMapping{}

	*attributeKeys{
		^super.attributeKeys ++ [\source, \destination, \when, \settings];
	}

	*commandNames{
		^super.commandNames ++ [\enable, \disable];
	}

	*queryNames{
		^super.queryNames ++ [\isEnabled];
	}

	//Attribute getters and setters
	source{ ^this.get(\source); }
	source_{arg val; this.set(\source, val); }

	destination{ ^this.get(\destination); }
	destination_{arg val; this.set(\destination, val); }

	when{^this.get(\when); }//e.g. when parameter \freq > 900, action.value
	when_{arg val; this.set(\when, val); }

	settings{^this.get(\settings); }
	settings_{arg val; this.set(\settings, val); }
}
