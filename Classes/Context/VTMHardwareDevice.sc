VTMHardwareDevice : VTMComposableContext {

	*new{arg name, parent, description, defintion;
		^super.new(name, parent, description, defintion).initHardwareDevice;
	}

	initHardwareDevice{
		"VTMHardwareDevice initialized".postln;
	}
}