VTMCommand : VTMValueElement {
	var <>action;

	*managerClass{ ^VTMCommandManager; }

	*new{arg name, declaration, manager;
		^super.new(name, declaration, manager).initCommand;
	}

	initCommand{}

	free{
		action = nil;
		super.free;
	}

}
