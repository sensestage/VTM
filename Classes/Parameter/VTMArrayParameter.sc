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

	*new{arg name, attributes;
		^super.new(name, attributes).initArrayParameter;
	}

	initArrayParameter{
		if(attributes.notEmpty, {
			if(attributes.includesKey(\size), {
				size = attributes[\size];
			});
			if(attributes.includesKey(\fixedSize), {
				fixedSize = attributes[\fixedSize];
			});
			if(attributes.includesKey(\itemType), {
				itemType = attributes[\itemType];
			});
		});
	}


}
