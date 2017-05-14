VTMValue {
	var enum;
	var <>action;
	var <selectedEnum;
	var scheduler;
	var description;

	*prDefaultValueForType{
		this.subclassResponsibility(thisMethod);
	}

	*typeToClass{arg val;
		^"VTM%Value".format(val.asString.capitalize).asSymbol.asClass;
	}

	*classToType{arg val;
		^val.name.asString.findRegexp("^VTM(.+)Value$")[1][1].toLower;
	}

	*type{
		this.subclassResponsibility(thisMethod);
	}

	type{
		^this.class.type;
	}

	*string{arg description; ^this.makeFromType(thisMethod.name, description); }
	*boolean{arg description; ^this.makeFromType(thisMethod.name, description); }
	*timecode{arg description; ^this.makeFromType(thisMethod.name, description); }
	*integer{arg description; ^this.makeFromType(thisMethod.name, description); }
	*decimal{arg description; ^this.makeFromType(thisMethod.name, description); }
	*array{arg description; ^this.makeFromType(thisMethod.name, description); }
	*dictionary{arg description; ^this.makeFromType(thisMethod.name, description); }
	*schema{arg description; ^this.makeFromType(thisMethod.name, description); }
	*list{arg description; ^this.makeFromType(thisMethod.name, description); }
	*tuple{arg description; ^this.makeFromType(thisMethod.name, description); }

	*makeFromDescription{arg description;
		var dec = description.deepCopy;
		var type = dec.removeAt(\type);
		^this.makeFromType(type, dec);
	}

	*makeFromType{arg type, description;
		var class;
		class = this.typeToClass(type);
		if(class.notNil, {
			^class.new(description);
		}, {
			Error("Unknown type").throw;
		});
	}

	*new{arg description;
		^super.new.initValue(description);
	}

	initValue{arg description_;
		description = VTMValueDescription.newFrom(description_);
		if(description.notEmpty, {
			if(description.includesKey(\value), {
				this.value_(description[\value]);
			});
			if(description.includesKey(\defaultValue), {
				this.defaultValue_(description[\defaultValue]);
			});
			if(description.includesKey(\enum), {
				this.enum_(description[\enum]);
			});

		});

	}

	//only non-abstract sub classes will implement this.
	isValidType{arg val;
		this.subclassResponsibility(thisMethod);
	}

	initValueParameter{
		if(description.notEmpty, {
			if(description.includesKey(\enabled), {
				//Is enabled by default so only disabled if defined
				if(description[\enabled].not, {
					this.disable;
				})
			});
			if(description.includesKey(\defaultValue), {
				"AAA".postln;
				this.defaultValue_(description[\defaultValue]);
			});
			if(description.includesKey(\value), {
				this.value_(description[\value]);
			});
			if(description.includesKey(\filterRepetitions), {
				this.filterRepetitions = description[\filterRepetitions];
			});
			if(description.includesKey(\enum), {
				//enums are stored as key value pairs
				this.enum = VTMNamedList.newFromKeyValuePairs(description[\enum]);
			});
			if(description.includesKey(\restrictValueToEnum), {
				this.restrictValueToEnum = description[\restrictValueToEnum];
			});
		});
		enum = enum ? VTMNamedList();
		if(this.defaultValue.isNil, {
			this.defaultValue_(this.prDefaultValueForType.deepCopy);
		});
		if(this.value.isNil, {
			this.value_( this.defaultValue );
		});
		scheduler = Routine{};
	}

	//set value to default
	reset{arg doActionUponReset = false;
		if(this.defaultValue.notNil, {
			this.value_(this.defaultValue);
			if(doActionUponReset, {
				this.doAction;
			});
		});
	}

	valueAction_{arg val;
		if(this.filterRepetitions, {
			var willDoAction = val != this.value; //check if new value is different
			this.value_(val);
			if(willDoAction, {
				this.doAction;
			});
		}, {
			this.value_(val);
			this.doAction;
		});
	}

	//Enabled by default.
	//Will enable action to be run
	enable{arg doActionWhenEnabled = false;
		this.enabled_(true);
		if(doActionWhenEnabled, {
			this.doAction;
		});
	}

	//Will disable action from being run
	disable{
		this.enabled_(false);
	}

	doAction{
		if(this.enabled, {
			action.value(this);
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

	addEnum{arg val, name, slot;
		enum.addItem(val, name, slot);
		this.changed(\enum);
	}

	removeEnum{arg slotOrName;
		if(enum.removedItem(slotOrName).notNil, {§
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

	*descriptionKeys{
		^[\enabled, \filterRepetitions, \value, \defaultValue, \enum, \restrictValueToEnum];
	}

	set{arg key, val;
		description.put(key, val);
		this.changed(\description, key);
	}

	get{arg key;
		^description[key];
	}

	description{arg includeDefaultValues = true;
		var result = description.deepCopy;
		if(includeDefaultValues, {
			this.class.descriptionKeys.do({arg attrKey;
				var attrVal = this.perform(attrKey);
				//don't use the ones that are nil
				if(attrVal.notNil, {
					result.put(attrKey, attrVal);
				});
			});
		});

		^result;
	}

	//Attribute getters
	enabled{ ^this.get(\enabled) ? true; }
	enabled_{arg val; this.set(\enabled, val); }

	filterRepetitions{ ^this.get(\filterRepetitions) ? false; }
	filterRepetitions_{arg val; this.set(\filterRepetitions, val); }

	value{ ^this.get(\value) ? this.defaultValue; }
	value_{arg val;
		if(this.restrictValueToEnum, {
			if(this.enum.includes(val), {
				this.set(\value, val);
			});
		}, {
			this.set(\value, val);
		});
	}

	defaultValue{ ^this.get(\defaultValue) ? this.class.prDefaultValueForType; }
	defaultValue_{arg val; this.set(\defaultValue, val); }

	enum{ ^this.get(\enum); }
	enum_{arg val; this.set(\enum, val); }

	restrictValueToEnum{ ^this.get(\restrictValueToEnum) ? false; }
	restrictValueToEnum_{arg val; this.set(\restrictValueToEnum, val); }

}
