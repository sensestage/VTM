VTMValueElement : VTMAbstractData {
	var <valueObj;//TEMP getter
	var properties;

	*new{arg name, declaration, manager;
		^super.new(name, declaration, manager).initValueElement;
	}

	initValueElement{
		var valueClass = VTMValue.typeToClass(\integer);
		//extract property values from declaration
		properties = VTMOrderedIdentityDictionary.new;
		valueClass.propertyKeys.do({arg propKey;
			if(declaration.includesKey(propKey), {
				properties.put(propKey, declaration[propKey]);
			});
		});
		valueObj = VTMValue.makeFromType(declaration[\type], properties);
	}

	prInitValueObject{
	}

	*parameterDescriptions{
		^super.parameterDescriptions.putAll(
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
