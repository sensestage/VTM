VTMCue : VTMAbstractData {

	*new{arg name, attributes, manager;
		^super.new(name, attributes, manager).initCue;
	}

	initCue{}
}
