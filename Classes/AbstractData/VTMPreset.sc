VTMPreset : VTMAbstractData {

	*managerClass{ ^VTMPresetManager; }

	*new{arg name, declaration, manager;
		^super.new(name, declaration, manager).initPreset;
	}

	initPreset{

	}

	*parameterDefinitions{
		^super.parameterDescriptions ++ [
			(name: \values, \type: \dictionary)
		];
	}

	*interpolate{arg aPreset, bPreset, fraction = 0.5, transitionPoints;
		//
	}
}
