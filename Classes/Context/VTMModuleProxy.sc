VTMModuleProxy : VTMContext {

	*isCorrectParentContext{arg parent;
		^parent.isKindOf(VTMScene);
	}

	*isCorrectChildContext{arg child;
		^(
			child.isKindOf(VTMModuleProxy)
			|| child.isKindOf(VTMParameterProxy)
		);
	}

	*new{arg name, scene;
		^super.new(name, scene).initModuleProxy;
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
