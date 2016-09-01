/*
ArrayParameters has items with a defined type.
Differing from ListParameter in that it doesn't make the actual internal
sub-parameters, but rather constructs a subparameter interface
to its internal items(i.e. array elements).
*/
VTMArrayParameter : VTMCollectionParameter {
	var <size = 0;
	var <fixedSize = false;
	var <itemType;

	isValidType{arg val;
		^(val.isArray and: {val.isString.not});
	}

	*type{ ^\array; }

	prDefaultValueForType{
		^[];
	}

	*new{arg name, declaration;
		^super.new(name, declaration).initArrayParameter;
	}

	initArrayParameter{
		if(declaration.notEmpty, {
			if(declaration.includesKey(\size), {
				size = declaration[\size];
			});
			if(declaration.includesKey(\fixedSize), {
				fixedSize = declaration[\fixedSize];
			});
			if(declaration.includesKey(\itemType), {
				itemType = declaration[\itemType];
			});
		});
	}


}
