//children may be Parameter and HardwareDevice
VTMHardwareDevice : VTMComposableContext {
	*managerClass{ ^VTMHardwareSetup; }

	*new{arg name, declaration, manager, definition;
		^super.new(name, declaration, manager, definition).initHardwareDevice;
	}

	initHardwareDevice{
		"VTMHardwareDevice initialized".postln;
	}
}
