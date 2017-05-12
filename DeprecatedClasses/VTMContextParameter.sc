VTMContextParameter : VTMElement {
	classvar attrKeysThunk;
	var <valueObj;


	*managerClass{ ^VTMContextParameterManager; }


	*initClass{

		Class.initClassTree(VTMValue);
		attrKeysThunk = Thunk{
			var valSubClassMethods;
			valSubClassMethods = VTMValue.allSubclasses.collect({arg cl;
				cl.attributeKeys;
			}).flat.as(IdentitySet).removeAll(VTMValue.attributeKeys);
			super.attributeKeys ++ VTMValue.attributeKeys ++ valSubClassMethods;
		};
	}

	*new{arg name, attributes, manager;
		^super.new(name, attributes, manager).initContextParameter;
	}

	initContextParameter{

		if(attributes.includesKey(\type), {
			try{
				var type, attr;
				attr = attributes.deepCopy;
				type = attr.at(\type);
				valueObj = VTMValue.makeFromType(type, attr);
			} {
				Error("[%] - Unknown parameter type: '%'".format(this.fullPath, this.type)).throw;
			}
		},{
			Error("[%] - Value type for context parameter not defined: '%'".format(this.fullPath)).throw;
		});
	}

	action_{arg func;
		valueObj.action = func;
	}

	//TODO: optimize this? lazythunkit
	*attributeKeys{
		^attrKeysThunk.value;
	}

	attributeKeys{
		^super.attributeKeys ++ valueObj.class.attributeKeys;
	}

	attributes{arg includeDefaultValues = true;
		^valueObj.attributes(includeDefaultValues);
	}

	//overriding parent class set/get to inly use the valueObj attributes as data storage
	set{arg attributeKey, value;
		valueObj.set(attributeKey, value);
		this.changed(\attribute, attributeKey);
	}

	get{arg attributeKey;
		^valueObj.at(attributeKey);
	}


	type{ ^valueObj.type; }
	valueAction_{arg val; valueObj.valueAction_(val); }

	//Atribute setters and getters
	value{ ^valueObj.value;	}
	value_{arg val; valueObj.value_(val); }

	enabled{ ^valueObj.enabled;	}
	enabled_{arg val; valueObj.enabled_(val); }

	filterRepetitions{ ^valueObj.filterRepetitions;	}
	filterRepetitions_{arg val; valueObj.filterRepetitions_(val); }

	defaultValue{ ^valueObj.defaultValue;	}
	defaultValue_{arg val; valueObj.defaultValue_(val); }

	enum{ ^valueObj.enum;	}
	enum_{arg val; valueObj.enum_(val); }

	restrictValueToEnum{ ^valueObj.restrictValueToEnum;	}
	restrictValueToEnum_{arg val; valueObj.restrictValueToEnum_(val); }

	//hackish way of forwarding to inner value object
	minVal{
		if(valueObj.respondsTo(\minVal), {
			^valueObj.minVal;
		});
		^nil;
	}
	minVal_{arg val;
		if(valueObj.respondsTo(\minVal_), {
			valueObj.minVal_(val);
		});
	}

	maxVal{
		if(valueObj.respondsTo(\maxVal), {
			^valueObj.maxVal;
		});
		^nil;
	}
	maxVal_{arg val;
		if(valueObj.respondsTo(\maxVal_), {
			valueObj.maxVal_(val);
		});
	}

	stepsize{
		if(valueObj.respondsTo(\stepsize), {
			^valueObj.stepsize;
		});
		^nil;
	}
	stepsize_{arg val;
		if(valueObj.respondsTo(\stepsize_), {
			valueObj.stepsize_(val);
		});
	}

	pattern{
		if(valueObj.respondsTo(\pattern), {
			^valueObj.pattern;
		});
		^nil;
	}
	pattern_{arg val;
		if(valueObj.respondsTo(\pattern_), {
			valueObj.pattern_(val);
		});
	}

	matchPattern{
		if(valueObj.respondsTo(\matchPattern), {
			^valueObj.matchPattern;
		});
		^nil;
	}
	matchPattern_{arg val;
		if(valueObj.respondsTo(\matchPattern_), {
			valueObj.matchPattern_(val);
		});
	}

	clipmode{
		if(valueObj.respondsTo(\clipmode), {
			^valueObj.clipmode;
		});
		^nil;
	}
	clipmode_{arg val;
		if(valueObj.respondsTo(\clipmode_), {
			valueObj.clipmode_(val);
		});
	}
}