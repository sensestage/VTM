//children may be Parameter, Module, ModuleProxy, and Scene
VTMScene : VTMComposableContext {

	*new{arg name, definition, attributes, parent;
		^super.new(name, definition, attributes, parent).initScene;
	}

	initScene{
	}

}
