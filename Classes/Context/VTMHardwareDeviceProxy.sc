VTMHardwareDeviceProxy : VTMContextProxy {

	*new{arg name, parent, definition, declaration;
		^super.new(name, parent, definition, declaration).initHardwareDeviceProxy;
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
