VTMHardwareDeviceProxy : VTMContextProxy {

	*new{arg name, parent, description, defintion;
		^super.new(name, parent, description, defintion).initHardwareDeviceProxy;
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
