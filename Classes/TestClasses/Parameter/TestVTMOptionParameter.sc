TestVTMOptionParameter : VTMUnitTest {
	setUp{
		"Setting up a VTMOptionParameterTest".postln;
	}

	tearDown{
		"Tearing down a VTMOptionParameterTest".postln;
	}

	test_DefaultAttributes{
		var testValue, testOptions;
		var param = VTMOptionParameter.new('myOption');
		//value should be be nil
		this.assertEquals(
			param.value, nil,
			"OptionParameter value is nil by default"
		);

		//defaultValue should be nil
		this.assertEquals(
			param.defaultValue, nil,
			"OptionParameter defaultValue is nil by default"
		);

		//options should be niil
		this.assertEquals(
			param.options, nil,
			"OptionParameter options is nil by default"
		);
	}

	test_SetAttributesThroughdeclaration{
		var desc = (
			options: [\aaa, \bbb, \ccc, \ddd],
			defaultValue: \ddd,
			value: \ccc
		);
		var param = VTMOptionParameter.new('myOption', desc);
		this.assertEquals(
			param.value, desc[\value],
			"OptionParameter set value through declaration"
		);
		this.assertEquals(
			param.defaultValue, desc[\defaultValue],
			"OptionParameter set defaultValue through declaration"
		);
		this.assertEquals(
			param.options, desc[\options],
			"OptionParameter set options through declaration"
		);
	}

	test_DefaultValueAndValueWhenOptionsChange{
		var testOptions, testValue;
		var wasRun;
		var param = VTMOptionParameter.new('myOption');
		param.action = {
			wasRun = true;
		};

		//When options are set, the first item in the option list should be the defaultValue
		testOptions = [\cc, \bb, \aa];
		param.options = testOptions;
		this.assertEquals(
			param.defaultValue, testOptions.first,
			"OptionParameter defaultValue becomes first item in option list if not defined"
		);

		//defaultValue changes when set
		testValue = \bb;
		param.defaultValue = \bb;
		this.assertEquals(
			param.defaultValue, testValue,
			"OptionParameter changed defaultValue correctly"
		);

		//Should warn and not set defaultValue when tried to set to non-existing option
		testValue = param.defaultValue;
		param.defaultValue = \qq;
		this.assertEquals(
			param.defaultValue, testValue,
			"OptionParameter did not change defaultValue if set to non-existing option"
		);

		//When options change and current value is not in the new options, the value should set to defaultValue,
		//which is the first item in the option list.
		//The action should not be run when option change causes value change.
		wasRun = false;
		param.options = [\aa, \bb, \cc];
		testValue = \aa;
		testOptions = [\zz, \yy, \zz];
		param.options = testOptions;
		this.assertEquals(
			param.defaultValue, testOptions.first,
			"OptionParameter changed the defaultValue when options changed"
		);
		this.assertEquals(
			param.value, param.defaultValue,
			"OptionParameter changed the value to defaultValue when options changed and current value was not in the new options"
		);
		this.assert(
			wasRun.not, "OptionParameter did not run action when option change changed caused value change"
		);

		//When options are changed but the current value is still in the options,
		//the value should stay the same and the action should not run
		wasRun = false;
		testOptions = [\yyy, \ttt, \rrr];
		testValue = \ttt;
		param.options = testOptions;
		param.value = testValue;
		testOptions = [\ttt, \ooo, \bbb];// ttt is the current value
		param.options = testOptions;
		this.assertEquals(
			param.value, testValue,
			"Options kept value when options changed and current value was still among the new options"
		);
		this.assert(wasRun.not, "OptionParameter did not rund action when options change did not cause value change");

		//When options are cleared the defaultValue and the value should both be nil.
		//The action should not be run
		wasRun = false;
		param.options = [\aa, \bb, \cc];
		param.value = \cc;
		param.defaultValue = \bb;
		param.options = nil;
		this.assertEquals(
			param.defaultValue, nil,
			"OptionParameter defaultValue becomes nil when options are cleared"
		);
		this.assertEquals(
			param.value, nil,
			"OptionParameter set value to nil when clearing options"
		);
		this.assert(
			wasRun.not, "OptionParameter did not run action when options were cleared"
		);

	}


	test_AvoidDuplicateOptions{
		var testOptions, shouldBe;
		var param = VTMOptionParameter.new('myOption', (options: [999,888,777,666]));
		testOptions = [333,333,555,555,333,777];
		shouldBe = [333,555,777];
		//Should remove duplicate options but keep the order
		param.options = testOptions;
		this.assertEquals(
			param.options, shouldBe,
			"OptionParameter removed duplicate and kept order when array with duplicates are used for setting options"
		);
	}

	test_NextAndPrevious{
		var desc = (
			options: [11,22,33,44],
			defaultValue: 11
		);
		var param = VTMOptionParameter.new('myOption', desc);
		param.nextOption;
		this.assertEquals(
			param.value, desc[\options][1],
			"OptionParameter got next option correctly"
		);
		{param.nextOption} ! 2;
		this.assertEquals(
			param.value, desc[\options].last,
			"OptionParameter got next option correctly again"
		);
		param.sequenceMode(\clip);

		//Should not change due to clipping
		param.nextOption;
		this.assertEquals(
			param.value, desc[\options].last,
			"OptionParameter clipped nextOption in sequence mode clip"
		);

		//Should wrap and then go to second item in option list
		param.sequenceMode = \wrap;
		{ param.nextOption } ! 2;
		this.assertEquals(
			param.value, desc[\options][1],
			"OptionParameter wrapped nextOption in sequence mode clip"
		);

		//Should wrap back to last option
		{ param.previousOption } ! 2;
		this.assertEquals(
			param.value, desc[\options].last,
			"OptionParameter wrapped back to last item in option list"
		);

	}

	test_IgnoreOtherThanDefinedOptions{
		var testValue = \cc;
		var desc = (
			options: [\aa, \bb, \cc, \dd]
		);
		var param = VTMOptionParameter.new('myOption', desc);
		testValue = desc[\options].choose;
		param.value = testValue;
		param.value = \xx;
		this.assertEquals(
			param.value, testValue,
			"OptionParameter ignore non-existing option as value"
		);
	}

	test_ClearingOptionsUpdatesValue{
		var testOptions, testValue, testDefaultValue;
		var param = VTMOptionParameter.new('myOption', (options: [\aa, \bb, \cc, \dd]));
		param.value  = \dd;

		testOptions = [\xx, \yy, \zz];
		param.options = testOptions;
		this.assertEquals(
			param.value, testOptions.first,
			"OptionParameter updated value to first item when current value didn't match the previous options"
		);
		this.assertEquals(
			param.defaultValue, testOptions.first,
			"OptionParameter updated defaultValue to first item when current value didn't match the previous options"
		);

		param.options = [\aa, \tt, \yy, \pp];
		testValue = \yy;
		testDefaultValue = \pp;
		param.value = testValue;
		param.defaultValue = testDefaultValue;

		testOptions = [\xx, \yy, \zz, \ii, \pp];
		param.options = testOptions;
		this.assertEquals(
			param.value, testValue,
			"OptionParameter kept value when new options included matching item to current value"
		);
		this.assertEquals(
			param.defaultValue, testDefaultValue,
			"OptionParameter kept defaultValue when new options included matching item to current value"
		);
	}
}
