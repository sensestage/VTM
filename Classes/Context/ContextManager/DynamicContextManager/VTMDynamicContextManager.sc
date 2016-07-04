VTMDynamicContextManager : VTMContextManager {

	*new{arg name, parent, description, definition;
		^super.new(name, parent, description, definition).initDynamicContextManager;
	}

	initDynamicContextManager{
		"VTMDynamicContextManager initialized".postln;
	}

}
