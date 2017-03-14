//children may be Parameter, Module, ModuleProxy, and Scene
VTMScene : VTMComposableContext {

	*new{arg name, attributes, manager, definition;
		^super.new(name, definition, attributes, manager).initScene;
	}

	initScene{
	}

}
