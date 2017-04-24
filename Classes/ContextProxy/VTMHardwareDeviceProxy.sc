VTMHardwareDeviceProxy : VTMContextProxy {

	*new{arg name, definition, attributes, manager;
		^super.new(name, definition, attributes, manager).initHardwareDeviceProxy;
	}

	initHardwareDeviceProxy {
	}

}
