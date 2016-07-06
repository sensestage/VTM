VTMComposableContext : VTMContextManager {

	*new{arg name, parent, description, defintion;
		^super.new(name, parent, description, defintion).initComposableContext;
	}

	initComposableContext{
		"VTMComposableContext initialized".postln;
	}

}
