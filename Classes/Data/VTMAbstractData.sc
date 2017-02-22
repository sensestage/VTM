//Data is what can be stored and loaded from file, and transmitted
//between contexts as OSC.
VTMAbstractData{
	var attributes;
	var attributeGetterFunctionsThunk;
	var attributeSetterFunctionsThunk;

	*new{arg attributes;
		^super.new.initAbstractData(attributes);
	}

	initAbstractData{arg attributes_;
		if(attributes_.notNil, {
			attributes = attributes_.deepCopy;
		}, {
			attributes = IdentityDictionary.new;
		});

		//lazy attributesGetters and setters
		attributeGetterFunctionsThunk = Thunk({
			this.class.makeAttributeGetterFunctions(this);
		});
		attributeSetterFunctionsThunk = Thunk({
			this.class.makeAttributeSetterFunctions(this);
		});
	}

	free{
		attributes = nil;
	}

	attributes{
		var result;
		result = IdentityDictionary.new;
		this.class.attributeKeys.do({arg attrKey;
			var val;
			val = this.attributeGetterFunctions[attrKey].value;
			result.put(
				attrKey,
				val
			);
		});
		^result;
	}

	attributeGetterFunctions{
		^attributeGetterFunctionsThunk.value;
	}

	attributeSetterFunctions{
		^attributeSetterFunctionsThunk.value;
	}

	*makeAttributeGetterFunctions{arg data;
		this.subclassResponsibility(thisMethod);
	}

	*makeAttributeSetterFunctions{arg param;
		this.subclassResponsibility(thisMethod);
	}

}