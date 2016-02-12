VTMHardwareSetup : VTMNodeContext {

	*new{arg node;
		^super.new(node).initHardwareSetup;
	}

	initHardwareSetup{
	}

	hardware{
		^namespace.children;
	}
}