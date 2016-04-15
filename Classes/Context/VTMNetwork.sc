VTMNetwork : VTMContextRoot {
	var <localNode;

	*isCorrectChildContextType{arg child;
		^(
			child.isKindOf(VTMNode) || child.isKindOf(VTMNodeProxy)
		);
	}

	*new{arg localNode;
		^super.new('/').initNetwork(localNode);//using slash as name her for now
	}

	initNetwork{arg localNode_;
		localNode = localNode_;
		"VTMNetwork initialized".postln;
	}

	nodes{ ^children; }
}
