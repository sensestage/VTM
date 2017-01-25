VTMValueParameter : VTMParameter {
	var value;
	var <>filterRepetitions = false;//only perform action when incoming value is unequal to current value.
	var <>defaultValue;
	var enum;
	var <selectedEnum;
	var <restrictValueToEnum = false;
	var scheduler;

	prDefaultValueForType{
		this.subclassResponsibility(thisMethod);
	}

	//This is an abstract class and can not be used directly.
	*new{arg name, attributes;
		^super.new(name, attributes).initValueParameter;
	}

	value{
		^value;
	}

	//only non-abstract sub classes will implement this.
	isValidType{arg val;
		this.subclassResponsibility(thisMethod);
   	}

	initValueParameter{
		if(attributes.notEmpty, {
			if(attributes.includesKey(\defaultValue), {
				this.defaultValue_(attributes[\defaultValue]);
			});
			if(attributes.includesKey(\value), {
				this.value_(attributes[\value]);
			});
			if(attributes.includesKey(\filterRepetitions), {
				filterRepetitions = attributes[\filterRepetitions];
			});
			if(attributes.includesKey(\enum), {
				//enums are stored as key value pairs
				enum = VTMNamedList.newFromKeyValuePairs(attributes[\enum]);
			});
			if(attributes.includesKey(\restrictValueToEnum), {
				restrictValueToEnum = attributes[\restrictValueToEnum];
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

	ramp{arg val, time;
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

	*makeAttributeGetterFunctions{arg param;
		^super.makeAttributeGetterFunctions(param).putAll(
			IdentityDictionary[
				\value -> {param.value},
				\defaultValue -> {param.defaultValue;},
				\filterRepetitions -> {param.filterRepetitions;},
				\enum -> {param.enum;},
				\restrictValueToEnum -> { param.restrictValueToEnum; }
			]
		);
	}

	*makeAttributeSetterFunctions{arg param;
		^super.makeAttributeSetterFunctions(param).putAll(IdentityDictionary[
			'value' -> {arg ...args; param.valueAction_(*args); },
			'filterRepetitions' -> {arg ...args;
				param.filterRepetitions_(args[0].booleanValue); },
			'restrictValueToEnum' -> {arg ...args;
				param.restrictValueToEnum_(args[0].booleanValue); }
		]);
	}

	*attributeKeys{
		^(super.attributeKeys ++ [\value, \defaultValue, \filterRepetitions, \enum,
			\restrictValueToEnum
		]);
	}

	*makeOSCAPI{arg param;
		^super.makeOSCAPI(param).putAll(IdentityDictionary[
			'ramp' -> {arg ...args; param.ramp(*args); }
		]);
	}

}
