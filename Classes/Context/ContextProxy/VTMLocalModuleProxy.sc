VTMLocalModuleProxy : VTMModuleProxy {

	*new{arg name, parent, description, defintion;
		^super.new(name, parent, description, defintion).initLocalModuleProxy;
	}

	initLocalModuleProxy {
	}

}