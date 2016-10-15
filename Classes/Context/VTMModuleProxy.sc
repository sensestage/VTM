VTMModuleProxy : VTMContextProxy {

	*new{arg name, parent, declaration, definition;
		^super.new(name, parent, declaration, definition).initModuleProxy;
	}

	initModuleProxy {
	}

	scene{ ^parent;	}

	subscenes {
		^children.select(_.isKindOf(VTMScene));
	}

	parameters {
		^children.select(_.isKindOf(VTMParameterProxy));
	}
}
