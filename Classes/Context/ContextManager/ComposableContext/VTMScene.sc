VTMScene : VTMComposableContext {

	*new{arg name, parent, declaration, defintion;
		^super.new(name, parent, declaration, defintion).initScene;
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

	owner{ ^parent;	}
	modules{ ^children.select({arg item; item.isKindOf(VTMModuleProxy)}); }
	subscenes{ ^children.select({arg item; item.isKindOf(VTMScene)}); }
}
