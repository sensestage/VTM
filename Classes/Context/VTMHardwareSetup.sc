VTMHardwareSetup : VTMNodeContext {

	//can add any type of child to this context
	*isCorrectChildContextType{arg child; ^true; }

	*new{arg node;
		^super.new('hardware', node).initHardwareSetup;
	}

	initHardwareSetup{
	}

	hardware{ ^children; }
	isLeafContext{ ^true; }
}
