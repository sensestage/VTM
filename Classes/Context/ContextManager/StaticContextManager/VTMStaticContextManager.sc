VTMStaticContextManager : VTMContextManager {

	*new{arg name, parent, description, defintion;
		^super.new(name, parent, description, defintion).initStaticContextManager;
	}

	initStaticContextManager{
	}
}