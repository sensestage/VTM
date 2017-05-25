VTMValueElement : VTMAbstractData {
	var <valueObj;//TEMP getter
	var properties;
	var context;

	*new{arg name, declaration, manager;
		^super.new(name, declaration, manager).initValueElement;
	}

	initValueElement{
		var valueClass = VTMValue.typeToClass(declaration[\type]) ? VTMValue;
		//extract property values from declaration
		properties = VTMOrderedIdentityDictionary.new;
		valueClass.propertyKeys.do({arg propKey;
			if(declaration.includesKey(propKey), {
				properties.put(propKey, declaration[propKey]);
			});
		});
		valueObj = VTMValue.makeFromType(declaration[\type], properties);
		//If this element belongs to a Context we need to contextualize
		//the actions so that the includes the context as its second argument.
		if(manager.notNil and: {manager.context.notNil}, {
			context = manager.context;
		});
	}

	action_{arg func;
		valueObj.action_({
			func.value(this, context);
		});
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

	valueAction_{arg ...args;
		valueObj.valueAction_(*args);
	}

	free{
		valueObj = nil;
	}

	type{
		^this.get(\type);
	}
	
}
