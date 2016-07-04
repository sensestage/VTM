VTMContextManager : VTMContext {

	*new{arg name, parent, description, definition;
		^super.new(name, parent, description, definition).initContextManager;
	}

	initContextManager {

	}

}
