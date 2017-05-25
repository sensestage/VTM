VTMCommand : VTMValueElement {

	*managerClass{ ^VTMCommandManager; }

	*new{arg name, declaration, manager;
		^super.new(name, declaration, manager).initCommand;
	}

	initCommand{}

	action_{arg func;
		valueObj.action = func;
	}
}
