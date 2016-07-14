VTMHardwareDevice : VTMComposableContext {

	*new{arg name, parent, declaration, defintion;
		^super.new(name, parent, declaration, defintion).initHardwareDevice;
	}

	initHardwareDevice{
		"VTMHardwareDevice initialized".postln;
	}
}