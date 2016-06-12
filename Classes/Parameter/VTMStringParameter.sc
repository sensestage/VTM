/*
A StringParameter is where its value is always a string value which may optionally be
parsed in some way. Its regex value defines a regex pattern that checks the validity of
incoming string values.
*/
VTMStringParameter : VTMValueParameter {
	var <regex = ""; //empty string cause no pattern match
	var <matchPattern = true;

	type{ ^\string; }

	prDefaultValueForType{ ^""; }

	*isValidType{arg val;
		^val.isKindOf(String);
	}

	*new{arg name, description;
		^super.new(name, description).initStringParameter;
	}

	initStringParameter{
		if(description.notNil, {
			if(description.includesKey(\regex), {
				this.regex_(description[\regex]);
			});
			if(description.includesKey(\matchPattern), {
				this.matchPattern_(description[\matchPattern]);
			});
		});
	}

	matchPattern_{arg val;
		if(val.isKindOf(Boolean), {
			matchPattern = val;
			//Check the current value for matching, set to default if not.
			if(matchPattern and: {regex.notEmpty}, {
				if(regex.matchRegexp(this.value).not, {
					this.value_(this.defaultValue);
				});
			});
		}, {
			"StringParameter:matchPattern_ '%' - ignoring val because of invalid type: '%[%]'".format(
				this.fullPath, val, val.class
			).warn;
		});
	}

	regex_{arg val;
		if(val.class == Symbol, {//Symbols are accepted and converted into strings
			val = val.asString;
		});
		if(typecheck, {
			if(this.class.isValidType(val), {
				regex = val;
			}, {
				"StringParameter:regex_ '%' - ignoring val because of invalid type: '%[%]'".format(
					this.fullPath, val, val.class
				).warn;
			});
		}, {
			regex = val;
		});
	}

	defaultValue_{arg val;
		if(val.class == Symbol, {//Symbols are accepted and converted into strings
			val = val.asString;
		});
		if(typecheck, {
			if(this.class.isValidType(val), {
				if(matchPattern and: {regex.isEmpty.not}, {
					if(regex.matchRegexp(val), {
						super.defaultValue_(val);
					}, {
						"StringParameter:defaultValue_ '%' - ignoring val because of unmatched regex pattern: '%[%]'".format(
							this.fullPath, val, regex
						).warn;
					});
				}, {
					super.defaultValue_(val);
				});
			}, {
				"StringParameter:defaultValue_ '%' - ignoring val because of invalid type: '%[%]'".format(
					this.fullPath, val, val.class
				).warn;
			});
		}, {
			super.defaultValue_(val);
		});

	}

	value_{arg val, omitTypecheck = false;
		if(val.class == Symbol, {//Symbols are accepted and converted into strings
			val = val.asString;
		});
		if(typecheck or: {omitTypecheck.not}, {
			if(this.class.isValidType(val), {
				if(matchPattern and: {regex.isEmpty.not}, {
					if(regex.matchRegexp(val), {
						super.value_(val, true);
					}, {
						"StringParameter:value_ '%' - ignoring val because of unmatched regex pattern: '%[%]'".format(
							this.fullPath, val, regex
						).warn;
					});
				}, {
					super.value_(val, true);
				});
			}, {
				"StringParameter:value_ '%' - ignoring val because of invalid type: '%[%]'".format(
					this.fullPath, val, val.class
				).warn;
			});
		}, {
			if(matchPattern and: {regex.isEmpty.not}, {
				if(regex.matchRegexp(val), {
					super.value_(val, true);
				}, {
					"StringParameter:value_ '%' - ignoring val because of unmatched regex pattern: '%[%]'".format(
						this.fullPath, val, regex
					).warn;
				});
			}, {
				super.value_(val);
			});
		});
	}

	clear{arg doActionUponClear = false;
		var valToSet;
		//Set to default if pattern matching is enabled
		if(matchPattern and: {regex.isEmpty.not}, {
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
			\regex -> this.regex
		]);
		^result;
	}

}