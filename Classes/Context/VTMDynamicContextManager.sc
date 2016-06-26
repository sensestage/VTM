VTMDynamicContextManager : VTMContextManager {

	*new{arg name, parent, description, defintion;
		^super.new(name, parent, description, defintion).initDynamicContextManager;
	}

	initDynamicContextManager{
		"VTMDynamicContextManager initialized".postln;
	}

}