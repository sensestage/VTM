VTMNetwork : VTMDynamicContextManager {

	*new{arg name, parent, description, defintion;
		^super.new(name, parent, description, defintion).initNetwork;
	}

	initNetwork{
		"VTMNetwork initialized".postln;
	}

	applications{ ^children; }
}
