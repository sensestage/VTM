VTMModuleProxy : VTMContextProxy {

	*new{arg name, definition, declaration, manager;
		^super.new(name, definition, declaration, manager).initModuleProxy;
	}

	initModuleProxy {
	}
}
