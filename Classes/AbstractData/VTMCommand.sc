VTMCommand : VTMElement {
	var <>action;

	*managerClass{ ^VTMCommandManager; }

	*new{arg name, attributes, manager;
		^super.new(name, attributes, manager).initCommand;
	}

	initCommand{}

	*attributeKeys{
		^super.attributeKeys ++ [\function];
	}

	free{
		action = nil;
		super.free;
	}

	//attribute setters and getters
	function{ ^this.get(\function); }
	function_{arg func; this.set(\function, func); }
}
