VTMPreset : VTMAbstractData {

	*new{arg name, attributes, manager;
		^super.new(name, attributes, manager).initPreset;
	}

	initPreset{}
}
