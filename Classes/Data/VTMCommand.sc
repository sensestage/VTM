VTMCommand : VTMElement {
	var <>action;

	*new{arg name, attributes, manager;
		^super.new(name, attributes, manager).initCommand;
	}

	initCommand{}

	free{
		action = nil;
		super.free;
	}
}
