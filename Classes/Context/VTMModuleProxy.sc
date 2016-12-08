VTMModuleProxy : VTMContextProxy {

	*new{arg name, definition, declaration, parent;
		^super.new(name, definition, declaration, parent).initModuleProxy;
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
