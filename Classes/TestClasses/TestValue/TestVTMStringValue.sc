TestVTMStringValue : TestVTMValue {

	*makeRandomValue{arg params;
		^this.makeRandomString(params);
	}

	*makeRandomAttribute{arg key, params;
		var result;
		result = super.makeRandomAttribute(key, params);
		if(result.isNil, {
			switch(key,
				\pattern, { result = this.makeRandomString(params); },
				\matchPattern, { result = this.makeRandomBoolean(params); }
			);
		});
		^result;
	}

	test_DefaultAttributes{
		var param = VTMStringValue.new;
		this.assert(
			param.defaultValue.class == String and: {param.defaultValue.isEmpty},
			"StringValue defaultValue defaults to empty string:\n\tIS: %\n\tSHOULD BE: %".format(param.defaultValue, "")
		);
		//value should default to empty string if value not defined
		this.assert(
			param.value.class == String and: { param.value == param.defaultValue },
			"StringValue value defaults to empty string.\n\tIS: %\n\tSHOULD BE: %".format(param.value.asCompileString, "".asCompileString)
		);
		//pattern should be empty string by default
		this.assertEquals(
			param.pattern, "",
			"StringValue pattern is empty symbol by default"
		);

		//matchPattern should be true by default
		this.assertEquals(
			param.matchPattern, true,
			"StringValue matchPattern is true by default"
		);
	}

	test_SettingAttributesWithAttributes{
		var desc, param;
		desc = (
			value: "heisann.3",
			defaultValue: "heisann.5",
			pattern: "^heisann\\.\\d+$"
		);
		param = VTMStringValue.new(desc);
		this.assertEquals(
			param.value, desc[\value],
			"StringValue set value through attributes"
		);
		this.assertEquals(
			param.defaultValue, desc[\defaultValue],
			"StringValue set defaultValue through attributes"
		);
		this.assertEquals(
			param.pattern, desc[\pattern],
			"StringValue set value through attributes"
		);
	}


	test_ConvertingSymbolArgsToStrings{
		var desc, param;
		desc = (
			value: 'heisann.3',
			defaultValue: 'heisann.5',
			pattern: '^heisann\\.\\d+$'
		);
		param = VTMStringValue.new(desc);
		this.assertEquals(
			param.defaultValue.class, String,
			"StringValue converted defaultValue symbol arg to String"
		);
		this.assertEquals(
			param.value.class, String,
			"StringValue converted value symbol arg to String"
		);
		this.assertEquals(
			param.pattern.class, String,
			"StringValue converted pattern symbol arg to String"
		);
	}

	test_RegexMatchingWhenSettingValue{
		var testValue;
		var desc = (
			defaultValue: "bingo",
			value: "bongo",
			pattern: "^b(a|i|o|e|y)ngo$"
		);
		var param = VTMStringValue.new(desc);

		//Should ignore values that doesn't match the pattern
		testValue = "bikke";
		param.value_(testValue);
		this.assertEquals(
			param.value, desc[\value],
			"StringValue ignored setting unmatched value string"
		);

		//Should set values that match the pattern
		testValue = "bango";
		param.value_(testValue);
		this.assertEquals(
			param.value, testValue,
			"StringValue set setting matched value string"
		);

		//Should not check pattern is pattern is empty
		param.pattern = "";
		testValue = "baila";
		param.value_(testValue);
		this.assertEquals(
			param.value, testValue,
			"StringValue did not check pattern when pattern was empty string"
		);

		//Should not check pattern if matchPattern is false
		param.pattern = "^b(a|i|o|e|y)ngo$";
		param.matchPattern = false;
		testValue = "bokfngo";
		param.value = testValue;
		this.assertEquals(
			param.value, testValue,
			"StringValue did not check pattern when matchPatter is false"
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
			"StringValue set value to default when matchPattern is turned on and current value don't match"
		);

	}

	test_ClearingValue{
		var testValue, wasRun = false;
		var desc = (
			defaultValue: "bingo",
			value: "bongo",
			pattern: "^b(a|i|o|e|y)ngo$"
		);
		var param = VTMStringValue.new(desc);
		param.action = {|p| wasRun = true;};
		param.clear;
		this.assertEquals(
			param.value, param.defaultValue,
			"StringValue set value to defaultValue upon clearing with pattern matching off"
		);

		//Should set to empty when pattern matching is not active
		param.matchPattern = false;
		param.value = "bingo";
		param.clear;
		this.assertEquals(
			param.value, "",
			"StringValue set value to empty string upon clear when pattern matchng is disabled"
		);

		//Should run action if doActionUponClear is true
		param.value = "bungo";
		wasRun = false;
		param.matchPattern = true;
		param.clear(doActionUponClear: true);
		this.assertEquals(
			wasRun, true,
			"StringValue did do Action when clearing with doActionUponClear is true"
		);

		///Should do action when pattern match is disabled and doActionUponClear is true
		wasRun = false;
		param.value = "bango";
		param.matchPattern = false;
		param.clear(doActionUponClear: true);
		this.assertEquals(
			wasRun, true,
			"StringValue did do Action when clearing with doActionUponClear true, and pattern matching active"
		);
	}

}
