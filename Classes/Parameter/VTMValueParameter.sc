VTMValueParameter : VTMParameter {
	var value;
	var <>filterRepetitions = false;//only perform action when incoming value is unequal to current value.
	var <>defaultValue;
	var <>format;
	var enum;
	var <selectedEnum;
	var <restrictValueToEnum = false;
	var scheduler;

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
			if(declaration.includesKey(\enum), {
				//enums are stored as key value pairs
				enum = VTMNamedList.newFromKeyValuePairs(declaration[\enum]);
			});
			if(declaration.includesKey(\restrictValueToEnum), {
				restrictValueToEnum = declaration[\restrictValueToEnum];
			});
		});
		enum = enum ? VTMNamedList();
		if(defaultValue.isNil, {
			this.defaultValue_(this.prDefaultValueForType.deepCopy);
		});
		if(value.isNil, {
			this.value_( defaultValue );
		});
		scheduler = Routine{};
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

	enum_{arg val;
		//All enum must be valid
		if(val.isKindOf(SequenceableCollection), {
			if(val.every({arg it; this.isValidType(it)}), {
				enum = val;
			}, {
				"%:% - % All enum must be valid. [%]".format(
					this.class.name,
					thisMethod.name,
					this.name,
					val
				).warn;
			});
		}, {
			"%:% - % enum must be an array. [%]".format(
				this.class.name,
				thisMethod.name,
				this.name,
				val
			).warn;
		});
	}

	value_{arg val;
		if(restrictValueToEnum, {
			if(enum.includes(val), {
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

	rampValue{arg val, time;
		if(scheduler.isPlaying, {
			scheduler.stop;
		});
		scheduler = fork{
			time.wait;
			this.valueAction_(val);
		};
	}

	free{
		value = nil;
		defaultValue = nil;
		super.free;
	}

	enum{
		^enum.asKeyValuePairs;
	}

	addEnum{arg val, name, slot;
		enum.addItem(val, name, slot);
		this.changed(\enum);
	}

	removeEnum{arg slotOrName;
		if(enum.removedItem(slotOrName).notNil, {
			this.changed(\enum);
		});
	}

	moveEnum{arg slotOrName, toSlot;
		enum.moveItem(slotOrName, toSlot);
		this.changed(\enum);
	}

	setEnumName{arg slotOrName, itemName;
		enum.setItemName(slotOrName, itemName);
		this.changed(\enum);
	}

	changeEnum{arg slotOrName, newValue;
		enum.changeItem(slotOrName, newValue);
		this.changed(\enum);
	}

	getEnumValue{arg slotOrName;
		^enum[slotOrName];
	}

	attributes{
		^super.attributes.putAll(IdentityDictionary[
			\value -> this.value,
			\defaultValue -> this.defaultValue,
			\filterRepetitions -> this.filterRepetitions,
			\enum -> enum.asKeyValuePairs
		]);
	}

	*attributeKeys{
		^(super.attributeKeys ++ [\value, \defaultValue, \filterRepetitions, \enum]);
	}

}
