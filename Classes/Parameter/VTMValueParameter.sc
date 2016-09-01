VTMValueParameter : VTMParameter {
	var value;
	var <>filterRepetitions = false;//only perform action when incoming value is unequal to current value.
	var <>defaultValue;
	var <>format;
	var <options;
	var <restrictValueToOptions = false;

	prDefaultValueForType{
		this.subclassResponsibility(thisMethod);
	}

	//This is an abstract class and can not be used directly.
	*new{arg name, declaration;
		^super.new(name, declaration).initValueParameter;
	}

	value{
		^value;
	}

	//only non-abstract sub classes will implement this.
	isValidType{arg val; 
		this.subclassResponsibility(thisMethod);
   	}

	initValueParameter{
		if(declaration.notEmpty, {
			if(declaration.includesKey(\defaultValue), {
				this.defaultValue_(declaration[\defaultValue]);
			});
			if(declaration.includesKey(\value), {
				this.value_(declaration[\value]);
			});
			if(declaration.includesKey(\filterRepetitions), {
				filterRepetitions = declaration[\filterRepetitions];
			});
			if(declaration.includesKey(\options), {
				options = declaration[\options].asArray;
			});
			if(declaration.includesKey(\restrictValueToOptions), {
				restrictValueToOptions = declaration[\restrictValueToOptions];
			});
		});
		options = options ? [];
		if(defaultValue.isNil, {
			this.defaultValue_(this.prDefaultValueForType.deepCopy);
		});
		if(value.isNil, {
			this.value_( defaultValue );
		});
	}

	//set value to default
	reset{arg doActionUponReset = false;
		if(defaultValue.notNil, {
			this.value_(defaultValue);
			if(doActionUponReset, {
				this.doAction;
			});
		});
	}

	options_{arg val;
		//All options must be valid
		if(val.isKindOf(SequenceableCollection), {
			if(val.every({arg it; this.isValidType(it)}), {
				options = val;
			}, {
				"%:% - % All options must be valid. [%]".format(
					this.class.name,
					thisMethod.name, 
					this.name,
					val
				).warn;
			});
		}, {
			"%:% - % Options must be an array. [%]".format(
				this.class.name,
				thisMethod.name, 
				this.name,
				val
			).warn;
		});
	}

	value_{arg val;
		if(restrictValueToOptions, {
			if(options.includes(val), {
				value = val;
			});
		}, {
			value = val;
		});
		this.changed(\value);
	}

	valueAction_{arg val;
		if(filterRepetitions, {
			var willDoAction = val != value; //check if new value is different
			this.value_(val);
			if(willDoAction, {
				this.doAction;
			});
		}, {
			this.value_(val);
			this.doAction;
		});
	}

	free{
		value = nil;
		defaultValue = nil;
		super.free;
	}

	attributes{
		^super.attributes.putAll(IdentityDictionary[
			\value -> this.value,
			\defaultValue -> this.defaultValue,
			\filterRepetitions -> this.filterRepetitions
		]);
	}

	*attributeKeys{
		^(super.attributeKeys ++ [\value, \defaultValue, \filterRepetitions]);
	}

}
