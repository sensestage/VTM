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
}
