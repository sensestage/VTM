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

	*new{arg description;
		^super.new(description).initArrayParameter;
	}

	initArrayParameter{
		if(description.notEmpty, {
			if(description.includesKey(\size), {
				size = description[\size];
			});
			if(description.includesKey(\fixedSize), {
				fixedSize = description[\fixedSize];
			});
			if(description.includesKey(\itemType), {
				itemType = description[\itemType];
			});
		});
	}


}
