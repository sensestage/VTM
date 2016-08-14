/*
A StringParameter is where its value is always a string value which may optionally be
parsed in some way. Its pattern value defines a regex pattern that checks the validity of

incoming string values.
*/
VTMStringParameter : VTMValueParameter {
	var <pattern = ""; //empty string cause no pattern match
	var <matchPattern = true;

	type{ ^\string; }

	prDefaultValueForType{ ^""; }

	*isValidType{arg val;
		^val.isKindOf(String);
	}

	*new{arg name, declaration;
		^super.new(name, declaration).initStringParameter;
	}

	initStringParameter{
		if(declaration.notEmpty, {
			if(declaration.includesKey(\pattern), {
				this.pattern_(declaration[\pattern]);
			});
			if(declaration.includesKey(\matchPattern), {
				this.matchPattern_(declaration[\matchPattern]);
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
			"StringParameter:matchPattern_ '%' - ignoring val because of non-matching pattern: '%[%]'".format(
				this.fullPath, val, val.class
			).warn;
		});
	}

	pattern_{arg val;
		if(val.isString, {
			pattern = val;
		}, {
			"StringParameter:pattern_ '%' - ignoring val because of invalid type: '%[%]'".format(
				this.fullPath, val, val.class
			).warn;
		});
	}

	defaultValue_{arg val;
		if(val.class == Symbol, {//Symbols are accepted and converted into strings
			val = val.asString;
		});
		if(matchPattern and: {pattern.isEmpty.not}, {
			if(pattern.matchRegexp(val), {
				super.defaultValue_(val);
			}, {
				"StringParameter:defaultValue_ '%' - ignoring val because of unmatched pattern pattern: '%[%]'".format(
					this.fullPath, val, pattern
				).warn;
			});
		}, {
			super.defaultValue_(val);
		});
	}

	value_{arg val;
		if(matchPattern and: {pattern.isEmpty.not}, {
			if(pattern.matchRegexp(val), {
				super.value_(val, true);
			}, {
				"StringParameter:value_ '%' - ignoring val because of unmatched pattern pattern: '%[%]'".format(
					this.fullPath, val, pattern
				).warn;
			});
		}, {
			super.value_(val, true);
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

	attributes{
		var result;
		result = super.attributes.putAll(IdentityDictionary[
			\matchPattern -> this.matchPattern,
			\pattern -> this.pattern
		]);
		^result;
	}

	*attributeKeys{
		^(super.attributeKeys ++ [\matchPattern, \pattern]);
	}


}
