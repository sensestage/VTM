VTMValueElement : VTMAbstractData {
	var <valueObj;//TEMP getter

	*new{arg name, declaration, manager;
		^super.new(name, declaration, manager).initValueElement;
	}

	initValueElement{
		if(declaration.includesKey(\type), {
			try{
				var type, decl;
				decl = declaration.deepCopy;
				type = decl.at(\type);
				valueObj = VTMValue.makeFromType(type, decl);
			} {
				Error("[%] - Unknown parameter type: '%'".format(this.fullPath, this.type)).throw;
			};
		},{
			Error("[%] - Value type for value element not defined.".format(this.fullPath)).throw;
		});
	}

	*attributeDescriptions{
		^super.attributeDescriptions.addAll([
			(name: \type, type: \string)
		]);
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
