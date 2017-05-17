VTMValueElement : VTMAbstractData {
	var <valueObj;//TEMP getter

	*new{arg name, declaration, manager;
		^super.new(name, declaration, manager).initValueElement;
	}

	initValueElement{
	}

	prInitValueObject{
		try{
			var type, decl;
			decl = declaration.deepCopy;
			type = decl.at(\type);
			valueObj = VTMValue.makeFromType(type, decl);
		} {
			Error("[%] - Unknown value type: '%'".format(this.fullPath, this.type)).throw;
		};
	}

	*attributeDescriptions{
		^super.attributeDescriptions.putAll(
			VTMOrderedIdentityDictionary[
				\type -> (type: \string, optional: true)
			]
		);
	}

	get{arg key;
		var result;
		result = valueObj.get(key);
		if(result.isNil, {
			result = super.get(key);
		});
		^result;
	}

	value{
		^valueObj.value;
	}

	free{
		valueObj = nil;
	}

	type{
		^this.get(\type);
	}
	
}
