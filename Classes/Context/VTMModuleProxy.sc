VTMModuleProxy : VTMContext {

	*new{arg scene;
		^super.new(scene).initModuleProxy;
	}

	initModuleProxy {
	}

	scene{
		^namespaceElement.parent;
	}
}
