VTMValueElement : VTMAbstractData {
	var <valueObj;//TEMP getter

	*new{arg name, declaration, manager;
		^super.new(name, declaration, manager).initValueElement;
	}

	initValueElement{
		var valueClass = VTMValue.typeToClass(declaration[\type]) ? VTMValue;
		var valueProperties = VTMOrderedIdentityDictionary.new;
		//extract property values from declaration
		valueClass.propertyKeys.do({arg propKey;
			if(declaration.includesKey(propKey), {
				valueProperties.put(propKey, declaration[propKey]);
			});
		});
		valueObj = VTMValue.makeFromType(declaration[\type], valueProperties);
	}

	action_{arg func;
		valueObj.action_(func);
	}

	*parameterDescriptions{
		^super.parameterDescriptions.putAll(
			VTMOrderedIdentityDictionary[
				\type -> (type: \string, optional: true)
			]
		);
	}

	valueAction_{arg ...args;
		valueObj.valueAction_(*args);
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

	declaration{
		^valueObj.properties.putAll(parameters);
	}

	//setting the value object properties.
	set{arg key...args;
		valueObj.set(key, *args);
	}

	//getting the vaue object properties, or if not found
	//try getting the Element parameters
	get{arg key;
		var result;
		result = valueObj.get(key);
		if(result.isNil, {
			result = super.get(key);
		});
		^result;
	}

}
