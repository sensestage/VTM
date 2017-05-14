VTMStringValue : VTMValue {

	*type{ ^\string; }

	*prDefaultValueForType{ ^""; }

	isValidType{arg val;
		^(val.isKindOf(String) or: {val.isKindOf(Symbol)});
	}

	*new{arg description;
		^super.new(description).initStringParameter;
	}

	initStringParameter{arg stringAttr_;
		if(description.notEmpty, {
			if(description.includesKey(\pattern), {
				this.pattern_(description[\pattern]);
			});
			if(description.includesKey(\matchPattern), {
				this.matchPattern_(description[\matchPattern]);
			});
		});
	}

	clear{arg doActionUponClear = false;
		var valToSet;
		//Set to default if pattern matching is enabled
		if(this.matchPattern and: {this.pattern.isEmpty.not}, {
			valToSet = this.defaultValue;
		}, {
			valToSet = "";
		});
		this.value_(valToSet);
		if(doActionUponClear, {
			this.doAction;
		});
	}

	*descriptionKeys{
		^(super.descriptionKeys ++ [\matchPattern, \pattern]);
	}

	//Description getters and setters
	matchPattern_{arg val;
		if(val.isKindOf(Boolean), {
			this.set(\matchPattern, val);
			//Check the current value for matching, set to default if not.
			if(this.matchPattern and: {this.pattern.notEmpty}, {
				if(this.pattern.matchRegexp(this.value).not, {
					this.value_(this.defaultValue);
				});
			});
		}, {
			"StringParameter:matchPattern_- ignoring val because of non-matching pattern: '%[%]'".format(
				val, val.class
			).warn;
		});
	}
	matchPattern{ ^this.get(\matchPattern) ? false; }

	pattern_{arg val;
		var result = val ? "";
		if(val.isString or: {val.isKindOf(Symbol)}, {
			this.set(\pattern, val.asString);
		}, {
			"StringParameter:pattern_ - ignoring val because of invalid type: '%[%]'".format(
				val, val.class
			).warn;
		});
	}
	pattern{ ^this.get(\pattern) ? ""; }

	defaultValue_{arg val;
		var inval = val.copy.asString;
		if(inval.class == Symbol, {//Symbols are accepted and converted into strings
			inval = inval.asString;
		});
		if(this.matchPattern and: {this.pattern.isEmpty.not}, {
			if(this.pattern.matchRegexp(inval), {
				super.defaultValue_(inval);
			}, {
				"StringParameter:defaultValue_ - ignoring val because of unmatched pattern pattern: '%[%]'".format(
					inval, this.pattern
				).warn;
			});
		}, {
			super.defaultValue_(inval);
		});
	}

	value_{arg val;
		var inval = val.copy.asString;
		if(inval.class == Symbol, {//Symbols are accepted and converted into strings
			inval = inval.asString;
		});
		if(this.matchPattern and: {this.pattern.isEmpty.not}, {
			if(this.pattern.matchRegexp(inval), {
				super.value_(inval, true);
			}, {
				"StringParameter:value_ - ignoring val because of unmatched pattern pattern: '%[%]'".format(
					inval, this.pattern
				).warn;
			});
		}, {
			super.value_(inval, true);
		});
	}
}
