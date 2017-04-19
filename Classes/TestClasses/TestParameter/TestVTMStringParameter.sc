TestVTMStringParameter : TestVTMValueParameter {

	*makeRandomValue{arg params;
		^this.makeRandomString(params);
	}

	*prMakeRandomAttribute{arg key, params;
		var result;
		result = super.prMakeRandomAttribute(key, params);
		if(result.isNil, {
			switch(key,
				\pattern, { result = this.makeRandomString(params); },
				\matchPattern, { result = this.makeRandomBoolean(params); }
			);
		});
		^result;
	}


	setUp{
		"Setting up a VTMStringParameterTest".postln;
	}

	tearDown{
		"Tearing down a VTMStringParameterTest".postln;
	}

	test_DefaultAttributes{
		var param = VTMStringParameter.new('myString');
		this.assert(
			param.defaultValue.class == String and: {param.defaultValue.isEmpty},
			"StringParameter defaultValue defaults to empty string:\n\tIS: %\n\tSHOULD BE: %".format(param.defaultValue, "")
		);
		//value should default to empty string if value not defined
		this.assert(
			param.value.class == String and: { param.value == param.defaultValue },
			"StringParameter value defaults to empty string.\n\tIS: %\n\tSHOULD BE: %".format(param.value.asCompileString, "".asCompileString)
		);
		//pattern should be empty string by default
		this.assertEquals(
			param.pattern, "",
			"StringParameter pattern is empty symbol by default"
		);

		//matchPattern should be true by default
		this.assertEquals(
			param.matchPattern, true,
			"StringParameter matchPattern is true by default"
		);
	}

	test_SettingAttributesWithAttributes{
		var desc, param;
		desc = (
			value: "heisann.3",
			defaultValue: "heisann.5",
			pattern: "^heisann\\.\\d+$"
		);
		param = VTMStringParameter.new('myString', desc);
		this.assertEquals(
			param.value, desc[\value],
			"StringParameter set value through attributes"
		);
		this.assertEquals(
			param.defaultValue, desc[\defaultValue],
			"StringParameter set defaultValue through attributes"
		);
		this.assertEquals(
			param.pattern, desc[\pattern],
			"StringParameter set value through attributes"
		);
	}


	test_ConvertingSymbolArgsToStrings{
		var desc, param;
		desc = (
			value: 'heisann.3',
			defaultValue: 'heisann.5',
			pattern: '^heisann\\.\\d+$'
		);
		param = VTMStringParameter.new('myString', desc);
		this.assertEquals(
			param.defaultValue.class, String,
			"StringParameter converted defaultValue symbol arg to String"
		);
		this.assertEquals(
			param.value.class, String,
			"StringParameter converted value symbol arg to String"
		);
		this.assertEquals(
			param.pattern.class, String,
			"StringParameter converted pattern symbol arg to String"
		);
	}

	test_RegexMatchingWhenSettingValue{
		var testValue;
		var desc = (
			defaultValue: "bingo",
			value: "bongo",
			pattern: "^b(a|i|o|e|y)ngo$"
		);
		var param = VTMStringParameter.new('myString', desc);

		//Should ignore values that doesn't match the pattern
		testValue = "bikke";
		param.value_(testValue);
		this.assertEquals(
			param.value, desc[\value],
			"StringParameter ignored setting unmatched value string"
		);

		//Should set values that match the pattern
		testValue = "bango";
		param.value_(testValue);
		this.assertEquals(
			param.value, testValue,
			"StringParameter set setting matched value string"
		);

		//Should not check pattern is pattern is empty
		param.pattern = "";
		testValue = "baila";
		param.value_(testValue);
		this.assertEquals(
			param.value, testValue,
			"StringParameter did not check pattern when pattern was empty string"
		);

		//Should not check pattern if matchPattern is false
		param.pattern = "^b(a|i|o|e|y)ngo$";
		param.matchPattern = false;
		testValue = "bokfngo";
		param.value = testValue;
		this.assertEquals(
			param.value, testValue,
			"StringParameter did not check pattern when matchPatter is false"
		);

		//Should set pattern to default when matchPattern is turned on
		//and current value is non-matching
		param.matchPattern = false;
		param.pattern = "^b(a|i|o|e|y)ngo$";
		testValue = "jogge";
		param.value = testValue;
		param.matchPattern = true;
		this.assertEquals(
			param.value, param.defaultValue,
			"StringParameter set value to default when matchPattern is turned on and current value don't match"
		);

	}

	test_ClearingValue{
		var testValue, wasRun = false;
		var desc = (
			defaultValue: "bingo",
			value: "bongo",
			pattern: "^b(a|i|o|e|y)ngo$",
			action: {|p| wasRun = true;}
		);
		var param = VTMStringParameter.new('myString', desc);
		param.clear;
		this.assertEquals(
			param.value, param.defaultValue,
			"StringParameter set value to defaultValue upon clearing with pattern matching off"
		);

		//Should set to empty when pattern matching is not active
		param.matchPattern = false;
		param.value = "bingo";
		param.clear;
		this.assertEquals(
			param.value, "",
			"StringParameter set value to empty string upon clear when pattern matchng is disabled"
		);

		//Should run action if doActionUponClear is true
		param.value = "bungo";
		wasRun = false;
		param.matchPattern = true;
		param.clear(doActionUponClear: true);
		this.assertEquals(
			wasRun, true,
			"StringParameter did do Action when clearing with doActionUponClear is true"
		);

		///Should do action when pattern match is disabled and doActionUponClear is true
		wasRun = false;
		param.value = "bango";
		param.matchPattern = false;
		param.clear(doActionUponClear: true);
		this.assertEquals(
			wasRun, true,
			"StringParameter did do Action when clearing with doActionUponClear true, and pattern matching active"
		);
	}

}
