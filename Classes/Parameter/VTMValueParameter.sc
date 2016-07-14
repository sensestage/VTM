VTMValueParameter : VTMParameter {
	var value;
	var <>typecheck = true;//for checking type when value is set, adds overhead and safety.
	var <>filterRepetitions = false;//only perform action when incoming value is unequal to current value.
	var <>defaultValue;

	prDefaultValueForType{
		//this.subclassResponsibility(thisMethod);//this will be uncommented when test class setup is subclass testing
		^nil;
	}

	//This is an abstract class and can not be used directly.
	//Use AnythingParameter for parameters that can receive any value type.
	*new{arg name, declaration;
		^super.new(name, declaration).initValueParameter;
	}

	value{
		^value;
	}

	//this class will accept any type
	*isValidType{arg val; ^true; }

	initValueParameter{
		if(declaration.notNil, {
			if(declaration.includesKey(\defaultValue), {
				this.defaultValue_(declaration[\defaultValue]);
			});
			if(declaration.includesKey(\value), {
				this.value_(declaration[\value]);
			});
			if(declaration.includesKey(\filterRepetitions), {
				filterRepetitions = declaration[\filterRepetitions];
			});
		});
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

	value_{arg val, omitTypecheck = false; //don't do typecheck if already performed in subclass
		if(typecheck or: {omitTypecheck.not}, {
			if(this.class.isValidType(val), {
				value = val;
				this.changed(\value);
			}, {
				"ValueParameter:value_ '%' - ignoring val because of invalid type: '%[%]'".format(
					this.fullPath, val, val.class
				).warn;
			});
		}, {
			value = val;
			this.changed(\value);
		});
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
			\filterRepetitions -> this.filterRepetitions,
			\typecheck -> this.typecheck
		]);
	}

	*attributeKeys{
		^(super.attributeKeys ++ [\value, \defaultValue, \filterRepetitions, \typecheck]);
	}

}
