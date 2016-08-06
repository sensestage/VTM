VTMArrayParameter : VTMCollectionParameter {
	var <size = 0;
	var <fixedSize = false;
	var <itemType;

	isValidType{arg val;
		^(val.isArray and: {val.isString.not});
	}

	prDefaultValueForType{
		^[];
	}

	*new{arg name, declaration;
		^super.new(name, declaration).initArrayParameter;
	}

	initArrayParameter{
		if(declaration.notNil, {
			if(declaration.includesKey(\size), {
				size = declaration[\size];
			});
			if(declaration.includesKey(\fixedSize), {
				fixedSize = declaration[\fixedSize];
			});
			if(declaration.includesKey(\itemType), {
				itemType = declaration[\itemType];
			}, {
				Error("ArrayParameter needs to define itemType. [%]".format(this.fullPath)).throw;
			});
		}, {
			Error("ArrayParameter needs declaration with mandatory attributes: itemType. [%]".format(this.fullPath)).throw;
			^nil;
		});
	}


}
