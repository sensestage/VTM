//children may be Parameter and HardwareDevice
VTMHardwareDevice : VTMComposableContext {
	*managerClass{ ^VTMHardwareSetup; }

	*new{arg name, attributes, manager, definition;
		^super.new(name, attributes, manager, definition).initHardwareDevice;
	}

	initHardwareDevice{
		"VTMHardwareDevice initialized".postln;
	}
}
