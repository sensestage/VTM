VTMValue {
	var enum;
	var <>action;
	var <selectedEnum;
	var scheduler;
	var declaration;

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

	*string{arg decl; ^this.makeFromType(thisMethod.name, decl); }
	*boolean{arg decl; ^this.makeFromType(thisMethod.name, decl); }
	*timecode{arg decl; ^this.makeFromType(thisMethod.name, decl); }
	*integer{arg decl; ^this.makeFromType(thisMethod.name, decl); }
	*decimal{arg decl; ^this.makeFromType(thisMethod.name, decl); }
	*array{arg decl; ^this.makeFromType(thisMethod.name, decl); }
	*dictionary{arg decl; ^this.makeFromType(thisMethod.name, decl); }
	*schema{arg decl; ^this.makeFromType(thisMethod.name, decl); }
	*list{arg decl; ^this.makeFromType(thisMethod.name, decl); }
	*tuple{arg decl; ^this.makeFromType(thisMethod.name, decl); }

	*makeFromDeclaration{arg declaration;
		var dec = declaration.deepCopy;
		var type = dec.removeAt(\type);
		^this.makeFromType(type, dec);
	}

	*makeFromType{arg type, declaration;
		var class;
		class = this.typeToClass(type);
		if(class.notNil, {
			^class.new(declaration);
		}, {
			Error("Unknown type").throw;
		});
	}

	*new{arg declaration;
		^super.new.initValue(declaration);
	}

	initValue{arg declaration_;
		declaration = VTMDeclaration.newFrom(declaration_);
		if(declaration.notEmpty, {
			if(declaration.includesKey(\value), {
				this.value_(declaration[\value]);
			});
			if(declaration.includesKey(\defaultValue), {
				this.defaultValue_(declaration[\defaultValue]);
			});
			if(declaration.includesKey(\enum), {
				this.enum_(declaration[\enum]);
			});

		});

	}

	//only non-abstract sub classes will implement this.
	isValidType{arg val;
		this.subclassResponsibility(thisMethod);
	}

	initValueParameter{
		if(declaration.notEmpty, {
			if(declaration.includesKey(\enabled), {
				//Is enabled by default so only disabled if defined
				if(declaration[\enabled].not, {
					this.disable;
				})
			});
			if(declaration.includesKey(\defaultValue), {
				"AAA".postln;
				this.defaultValue_(declaration[\defaultValue]);
			});
			if(declaration.includesKey(\value), {
				this.value_(declaration[\value]);
			});
			if(declaration.includesKey(\filterRepetitions), {
				this.filterRepetitions = declaration[\filterRepetitions];
			});
			if(declaration.includesKey(\enum), {
				//enums are stored as key value pairs
				this.enum = VTMNamedList.newFromKeyValuePairs(declaration[\enum]);
			});
			if(declaration.includesKey(\restrictValueToEnum), {
				this.restrictValueToEnum = declaration[\restrictValueToEnum];
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

	*declarationKeys{
		^[\enabled, \filterRepetitions, \value, \defaultValue, \enum, \restrictValueToEnum];
	}

	set{arg key, val;
		declaration.put(key, val);
		this.changed(\declaration, key);
	}

	get{arg key;
		^declaration[key];
	}

	declaration{arg includeDefaultValues = true;
		var result = declaration.deepCopy;
		if(includeDefaultValues, {
			this.class.declarationKeys.do({arg attrKey;
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
