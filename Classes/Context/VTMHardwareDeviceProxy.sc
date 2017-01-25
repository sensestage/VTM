VTMHardwareDeviceProxy : VTMContextProxy {

	*new{arg name, definition, attributes, parent;
		^super.new(name, definition, attributes, parent).initHardwareDeviceProxy;
	}

	initHardwareDeviceProxy {
	}

	scene{ ^parent;	}

	subscenes {
		^children.select(_.isKindOf(VTMScene));
	}

	parameters {
		^children.select(_.isKindOf(VTMParameterProxy));
	}
}
