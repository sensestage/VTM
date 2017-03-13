VTMContextDefinition {
	var definition;
	var context;

	*new{arg def, context;
		^super.new.initContextDefinition(def, context);
	}

	initContextDefinition{arg def, context;
	}

	makeEnvir{
		^definition.deepCopy.put(\self, context);
	}

	parameters{}
	presets{}
	cues{}
	mappings{}
	scores{}
}
