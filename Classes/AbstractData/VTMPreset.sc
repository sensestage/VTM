VTMPreset : VTMAbstractData {
	var values;

	*managerClass{ ^VTMPresetManager; }

	*new{arg name, attributes, manager;
		^super.new(name, attributes, manager).initPreset;
	}

	initPreset{

	}

	values{
		^this.attributes;
	}

	values_{arg vals;
		vals.pairsDo({arg key, val;
			this.set(key, val);
		});
	}

	*attributeKeys{
		^super.attributeKeys ++ [\values];
	}

	//check if preset covers all parameters in the arg context
	isComplete{arg context;
		^(context.parameters.names.asSet == attributes.keys);
	}

	interpolate{arg anotherPreset, fraction = 0.5, transitionPoints;
		//
	}
}
