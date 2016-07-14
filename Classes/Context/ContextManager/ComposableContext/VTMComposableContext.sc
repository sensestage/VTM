VTMComposableContext : VTMContextManager {

	*new{arg name, parent, declaration, defintion;
		^super.new(name, parent, declaration, defintion).initComposableContext;
	}

	initComposableContext{
		"VTMComposableContext initialized".postln;
	}

}
