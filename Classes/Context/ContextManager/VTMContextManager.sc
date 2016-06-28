VTMContextManager : VTMContext {

	*new{arg name, parent, description, defintion;
		^super.new(name, parent, description, defintion).initContextManager;
	}

	initContextManager {

	}

}