/*
ArrayParameters has items with a defined type.
Differing from ListParameter in that it doesn't make the actual internal
sub-parameters, but rather constructs a subparameter interface
to its internal items(i.e. array elements).
*/
VTMArrayValue : VTMCollectionValue {
	var <size = 0;
	var <fixedSize = false;
	var <itemType;

	isValidType{arg val;
		^(val.isString.not and: {val.isArray});
	}

	*type{ ^\array; }

	*prDefaultValueForType{
		^[];
	}

	*new{arg declaration;
		^super.new(declaration).initArrayParameter;
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
