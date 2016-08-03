//children may be Parameter, Module, ModuleProxy, and Scene
VTMScene : VTMComposableContext {

	*new{arg name, parent, declaration, definition;
		^super.new(name, parent, declaration, definition).initScene;
	}

	initScene{
	}

	//The Scene factory will prepare everything with
	//the module proxy, so everything is connected when
	//it comes to this method.
	addModuleProxy{arg newModuleProxy;
		this.addChild(newModuleProxy);
	}

	removeModuleProxy{arg modProxyName;
		this.removeChild(modProxyName);
	}

	addSubscene{arg newSubscene;
		this.addChild(newSubscene);
	}

	removeSubscene{arg subsceneName;
		this.removeChild(subsceneName);
	}

	modules{	^nonSubcontexts.value; }
	subscenes{	^subcontexts.value; }
	isSubscene{ ^this.isSubcontext; }
	isModule{ ^this.isSubcontext.not; }
	owner { ^parent; }
}
