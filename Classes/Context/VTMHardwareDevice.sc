//children may be Parameter and HardwareDevice
VTMHardwareDevice : VTMComposableContext {

	*new{arg name, definition, attributes, manager;
		^super.new(name, definition, attributes, manager).initHardwareDevice;
	}

	initHardwareDevice{
		"VTMHardwareDevice initialized".postln;
	}
}
