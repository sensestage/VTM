VTMArrayParameter : VTMValueParameter {
	var <size = 0;
	var <fixedSize = false;
	var <itemType;

	isValidType{arg val;
		^(val.isArray and: {val.isString.not});
	}

	prDefaultValueForType{
		^[];
	}

	*new{arg name, description;
		^super.new(name, description).initArrayParameter;
	}

	initArrayParameter{
		if(description.notNil, {
			if(description.includesKey(\size), {
				size = description[\size];
			});
			if(description.includesKey(\fixedSize), {
				fixedSize = description[\fixedSize];
			});
			if(description.includesKey(\itemType), {
				itemType = description[\itemType];
			}, {
				Error("ArrayParameter needs to define itemType. [%]".format(this.fullPath)).throw;
			});
		}, {
			Error("ArrayParameter needs description with mandatory attributes: itemType. [%]".format(this.fullPath)).throw;
			^nil;
		});
	}


}