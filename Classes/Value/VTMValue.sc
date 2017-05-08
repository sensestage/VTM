VTMValue {
	var enum;
	var <>action;
	var <selectedEnum;
	var scheduler;
	var attributes;

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

	*string{arg attr; ^this.makeFromType(thisMethod.name, attr); }
	*boolean{arg attr; ^this.makeFromType(thisMethod.name, attr); }
	*timecode{arg attr; ^this.makeFromType(thisMethod.name, attr); }
	*integer{arg attr; ^this.makeFromType(thisMethod.name, attr); }
	*decimal{arg attr; ^this.makeFromType(thisMethod.name, attr); }
	*array{arg attr; ^this.makeFromType(thisMethod.name, attr); }
	*dictionary{arg attr; ^this.makeFromType(thisMethod.name, attr); }
	*schema{arg attr; ^this.makeFromType(thisMethod.name, attr); }
	*list{arg attr; ^this.makeFromType(thisMethod.name, attr); }
	*tuple{arg attr; ^this.makeFromType(thisMethod.name, attr); }


	*makeFromType{arg type, attributes;
		var class;
		class = this.typeToClass(type);
		if(class.notNil, {
			^class.new(attributes);
		}, {
			Error("Unknown type").throw;
		});
	}

	*new{arg attributes;
		^super.new.initValue(attributes);
	}

	initValue{arg attributes_;
		attributes = VTMAttributes.newFrom(attributes_);
		if(attributes.notEmpty, {
			if(attributes.includesKey(\value), {
				this.value_(attributes[\value]);
			});
			if(attributes.includesKey(\defaultValue), {
				this.defaultValue_(attributes[\defaultValue]);
			});
			if(attributes.includesKey(\enum), {
				this.enum_(attributes[\enum]);
			});

		});

	}

	//only non-abstract sub classes will implement this.
	isValidType{arg val;
		this.subclassResponsibility(thisMethod);
	}

	initValueParameter{
		if(attributes.notEmpty, {
			if(attributes.includesKey(\enabled), {
				//Is enabled by default so only disabled if defined
				if(attributes[\enabled].not, {
					this.disable;
				})
			});
			if(attributes.includesKey(\defaultValue), {
				"AAA".postln;
				this.defaultValue_(attributes[\defaultValue]);
			});
			if(attributes.includesKey(\value), {
				this.value_(attributes[\value]);
			});
			if(attributes.includesKey(\filterRepetitions), {
				this.filterRepetitions = attributes[\filterRepetitions];
			});
			if(attributes.includesKey(\enum), {
				//enums are stored as key value pairs
				this.enum = VTMNamedList.newFromKeyValuePairs(attributes[\enum]);
			});
			if(attributes.includesKey(\restrictValueToEnum), {
				this.restrictValueToEnum = attributes[\restrictValueToEnum];
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

	*attributeKeys{
		^[\enabled, \filterRepetitions, \value, \defaultValue, \enum, \restrictValueToEnum];
	}

	set{arg key, val;
		attributes.put(key, val);
		this.changed(\attributes, key);
	}

	get{arg key;
		^attributes[key];
	}

	attributes{arg includeDefaultValues = true;
		var result = attributes.deepCopy;
		if(includeDefaultValues, {
			this.class.attributeKeys.do({arg attrKey;
				var attrVal = this.perform(attrKey);
				//don't use the one that are nil
				if(attrVal.notNil, {
					result.put(attrKey, attrVal);
				});
			});
		});

		^result;
	}

	//Attribute setters and getters
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
