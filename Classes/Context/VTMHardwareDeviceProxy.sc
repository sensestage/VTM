VTMHardwareDeviceProxy : VTMContextProxy {

	*new{arg name, parent, declaration, defintion;
		^super.new(name, parent, declaration, defintion).initHardwareDeviceProxy;
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
