VTMModuleProxy : VTMContextProxy {

	*new{arg name, parent, declaration, defintion;
		^super.new(name, parent, declaration, defintion).initModuleProxy;
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
