VTMScene : VTMContext {

	*isCorrectChildContextType{arg child;
		^( child.isKindOf(VTMScene) or: { child.isKindOf(VTMParameterProxy) } );
	}

	*isCorrectParentContextType{arg parent;
		^parent.isKindOf(VTMSceneOwner);
	}

	*new{arg name, owner, description;
		^super.new(name, owner).initScene(description);
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
