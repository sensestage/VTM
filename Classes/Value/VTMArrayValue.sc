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

	*new{arg properties;
		^super.new(properties).initArrayParameter;
	}

	initArrayParameter{
		if(properties.notEmpty, {
			if(properties.includesKey(\size), {
				size = properties[\size];
			});
			if(properties.includesKey(\fixedSize), {
				fixedSize = properties[\fixedSize];
			});
			if(properties.includesKey(\itemType), {
				itemType = properties[\itemType];
			});
		});
	}


}
