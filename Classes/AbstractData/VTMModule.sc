//children may be Module
VTMModule : VTMComposableContext {

	*managerClass{ ^VTMModuleHost; }

	*new{arg name, attributes, manager, definition;
		^super.new(name, attributes, manager, definition).initModule;
	}

	initModule{
	}

	play{arg ...args;
		this.execute(\play, *args);
	} //temp for module definition hackaton

	stop{arg ...args;
		this.execute(\stop, *args);
	} //temp for module definition hackaton

}
