VTMPreset : VTMAbstractData {
	var values;

	*managerClass{ ^VTMPresetManager; }

	*new{arg name, declaration, manager;
		^super.new(name, declaration, manager).initPreset;
	}

	initPreset{

	}

	values{
		^this.declaration;
	}

	values_{arg vals;
		vals.pairsDo({arg key, val;
			this.set(key, val);
		});
	}

	*declarationKeys{
		^super.declarationKeys ++ [\values];
	}

	//check if preset covers all parameters in the arg context
	isComplete{arg context;
		^(context.parameters.names.asSet == declaration.keys);
	}

	interpolate{arg anotherPreset, fraction = 0.5, transitionPoints;
		//
	}
}
