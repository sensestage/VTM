VTMHardwareSetup : VTMStaticContextManager {

	//a hardware setups parent will be an Application
	*new{arg name, parent, description, defintion;
		^super.new(name, parent, description, defintion).initHardwareSetup;
	}

	initHardwareSetup{
		//Load hardware setup script/definition, or similar.
		//Hosts arbitrary number of HardwareDevice contexts
	}

	devices{ ^children; }
}
