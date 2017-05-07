VTMStringValue : VTMValue {
	var <pattern = ""; //empty string cause no pattern match
	var <matchPattern = true;

	*type{ ^\string; }

	*prDefaultValueForType{ ^""; }

	isValidType{arg val;
		^(val.isKindOf(String) or: {val.isKindOf(Symbol)});
	}

	*new{arg attributes;
		^super.new(attributes).initStringParameter;
	}

	initStringParameter{
		if(attributes.notEmpty, {
			if(attributes.includesKey(\pattern), {
				this.pattern_(attributes[\pattern]);
			});
			if(attributes.includesKey(\matchPattern), {
				this.matchPattern_(attributes[\matchPattern]);
			});
		});
	}

	matchPattern_{arg val;
		if(val.isKindOf(Boolean), {
			matchPattern = val;
			//Check the current value for matching, set to default if not.
			if(matchPattern and: {pattern.notEmpty}, {
				if(pattern.matchRegexp(this.value).not, {
					this.value_(this.defaultValue);
				});
			});
		}, {
			"StringParameter:matchPattern_- ignoring val because of non-matching pattern: '%[%]'".format(
				val, val.class
			).warn;
		});
	}

	pattern_{arg val;
		var result = val ? "";
		if(val.isString or: {val.isKindOf(Symbol)}, {
			pattern = val.asString;
		}, {
			"StringParameter:pattern_ - ignoring val because of invalid type: '%[%]'".format(
				val, val.class
			).warn;
		});
	}

	defaultValue_{arg val;
		var inval = val.copy.asString;
		"sdgasdf".postln;
		if(inval.class == Symbol, {//Symbols are accepted and converted into strings
			inval = inval.asString;
		});
		if(matchPattern and: {pattern.isEmpty.not}, {
			if(pattern.matchRegexp(inval), {
				super.defaultValue_(inval);
				}, {
					"StringParameter:defaultValue_ - ignoring val because of unmatched pattern pattern: '%[%]'".format(
						inval, pattern
					).warn;
			});
			}, {
				super.defaultValue_(inval);
		});
	}

	value_{arg val;
		var inval = val.copy;
		if(inval.class == Symbol, {//Symbols are accepted and converted into strings
			inval = inval.asString;
		});
		if(matchPattern and: {pattern.isEmpty.not}, {
			if(pattern.matchRegexp(inval), {
				super.value_(inval, true);
			}, {
				"StringParameter:value_ - ignoring val because of unmatched pattern pattern: '%[%]'".format(
					inval, pattern
				).warn;
			});
		}, {
			super.value_(inval, true);
		});
	}

	clear{arg doActionUponClear = false;
		var valToSet;
		//Set to default if pattern matching is enabled
		if(matchPattern and: {pattern.isEmpty.not}, {
			valToSet = this.defaultValue;
		}, {
			valToSet = "";
		});
		this.value_(valToSet);
		if(doActionUponClear, {
			this.doAction;
		});
	}

	*makeAttributeGetterFunctions{arg param;
		^super.makeAttributeGetterFunctions(param).putAll(
			IdentityDictionary[
				\matchPattern -> {param.matchPattern;},
				\pattern -> {param.pattern;}
			]
		);
	}

	*makeAttributeSetterFunctions{arg param;
		^super.makeAttributeSetterFunctions(param).putAll(
			IdentityDictionary[
				\matchPattern -> {arg ...args; param.matchPattern_(*args);},
				\pattern -> {arg ...args; param.pattern_(*args);}
			]
		);
	}

	*attributeKeys{
		^(super.attributeKeys ++ [\matchPattern, \pattern]);
	}
}
