//children may be Parameter, Module, ModuleProxy, and Scene
VTMScene : VTMComposableContext {

	*managerClass{ ^VTMSceneOwner; }

	*new{arg name, attributes, manager, definition;
		^super.new(name, attributes, manager, definition).initScene;
	}

	initScene{
	}

}
