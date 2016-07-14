VTMContextManager : VTMContext {

	*new{arg name, parent, declaration, definition;
		^super.new(name, parent, declaration, definition).initContextManager;
	}

	initContextManager {

	}

}
