TestVTMIntegerParameter : VTMUnitTest {
	setUp{
		"Setting up a VTMIntegerParameterTest".postln;
	}

	tearDown{
		"Tearing down a VTMIntegerParameterTest".postln;
	}

	test_DefaultAttributes{
		var param = VTMIntegerParameter.new('myInteger');
		this.assert(
			param.defaultValue.class == Integer and: {param.defaultValue == 0},
			"IntegerParameter defaultValue defaults to integer 0"
		);
		//value should default to 0 if value not defined
		this.assert(
			param.value == param.defaultValue and: {param.value.class == Integer},
			"IntegerParameter value defaults to Integer 0"
		);
		//stepsize should default to 0
		this.assertEquals(
			param.stepsize, 0, "IntegerParameter stepsize defaults to 0"
		);
		//clipmode should default to 'none'
		this.assertEquals(
			param.clipmode, \none, "IntegerParameter clipmode defaults to 'none'"
		);
		//minVal should default to nil (this might change later)
		this.assert(
			param.minVal.isNil, "IntegerParameter minVal defaults to nil"
		);
		//maxVal should default to nil (this might change later)
		this.assert(
			param.maxVal.isNil, "IntegerParameter maxVal defaults to nil"
		);
	}

	test_SetGetValue{
		var param = VTMIntegerParameter.new('myInteger');
		var testValue = 50;
		//SetGet 'value'
		param.value = testValue;
		this.assertEquals(
			param.value, testValue, "IntegerParameter value was set"
		);
	}

	test_ConvertDecimalNumbersToInteger{
		var testValue;
		var desc = (
			defaultValue: 11.1, value: 21.5,
			clipmode: \both, minVal: -23.1, maxVal: 88.9,
			stepsize: 8.8
		);
		var param = VTMIntegerParameter.new('myInteger', desc);
		this.assertEquals(
			param.value, desc[\value].asInteger,
			"IntegerParameter changed decimal value in declaration to Integer"
		);
		this.assertEquals(
			param.defaultValue, desc[\defaultValue].asInteger,
			"IntegerParameter changed decimal defaultValue in declaration to Integer"
		);
		this.assertEquals(
			param.minVal, desc[\minVal].asInteger,
			"IntegerParameter changed decimal minVal in declaration to Integer"
		);
		this.assertEquals(
			param.maxVal, desc[\maxVal].asInteger,
			"IntegerParameter changed decimal maxVal in declaration to Integer"
		);
		this.assertEquals(
			param.stepsize, desc[\stepsize].asInteger,
			"IntegerParameter changed decimal stepsize in declaration to Integer"
		);

		//Test the setter methods also
		testValue = 22.02;
		param.value = testValue;
		this.assertEquals(
			param.value, testValue.asInteger,
			"IntegerParameter changed decimal value in setter to Integer"
		);

		testValue = 0.01;
		param.defaultValue = testValue;
		this.assertEquals(
			param.defaultValue, testValue.asInteger,
			"IntegerParameter changed decimal defaultValue in setter to Integer"
		);

		testValue = -22.0144;
		param.minVal = testValue;
		this.assertEquals(
			param.minVal, testValue.asInteger,
			"IntegerParameter changed decimal minVal in setter to Integer"
		);

		testValue = 1232.9191;
		param.maxVal = testValue;
		this.assertEquals(
			param.maxVal, testValue.asInteger,
			"IntegerParameter changed decimal maxVal in setter to Integer"
		);

		testValue = 12.2;
		param.stepsize = testValue;
		this.assertEquals(
			param.stepsize, testValue.asInteger,
			"IntegerParameter changed decimal stepsize in setter to Integer"
		);
	}

	//	test_OnlyTypecheckWhenTypecheckIsTrue{}

	//	test_OnlyCheckRangesWhenMinValAndMaxValAreDefined{}

	test_SetGetAttributes{
		var param = VTMIntegerParameter.new('myInteger');
		var testValue;
		//SetGet 'minVal'
		testValue = -2;
		param.minVal = testValue;
		this.assertEquals(
			param.minVal, testValue, "IntegerParameter minVal was set"
		);
		//SetGet 'maxVal'
		testValue = 200;
		param.maxVal = testValue;
		this.assertEquals(
			param.maxVal, testValue, "IntegerParameter maxVal was set"
		);
		//SetGet 'stepsize'
		testValue = 11;
		param.stepsize = testValue;
		this.assertEquals(
			param.stepsize, testValue, "IntegerParameter stepsize was set"
		);
		//SetGet 'clipmode'
		testValue = \high;
		param.clipmode = testValue;
		this.assertEquals(
			param.clipmode, testValue, "IntegerParameter clipmode was set"
		);
	}

	test_IgnoreAndWarnOnWrongTypes{
		var testValue;
		var param = VTMIntegerParameter.new('myInteger');

		//Should ignore and warn on wrong 'minVal' type
		testValue = -100;
		param.minVal = testValue;
		param.minVal = \notAValidValue;
		this.assertEquals(
			param.minVal, testValue, "IntegerParameter minVal wasn't changed by val of invalid type"
		);

		//Should ignore and warn on wrong 'maxVal' type
		testValue = 300;
		param.maxVal = testValue;
		param.maxVal = \notAValidValue;
		this.assertEquals(
			param.maxVal, testValue, "IntegerParameter maxVal wasn't changed by val of invalid type"
		);

		//Should ignore and warn on wrong 'stepsize' type
		testValue = 11;
		param.stepsize = testValue;
		param.stepsize = \notAValidValue;
		this.assertEquals(
			param.stepsize, testValue, "IntegerParameter stepsize wasn't changed by val of invalid type"
		);

		//Should ignore and warn on wrong 'clipmode' type
		testValue = \low;
		param.clipmode = testValue;
		param.clipmode = 4321;
		this.assertEquals(
			param.clipmode, testValue, "IntegerParameter clipmode wasn't changed by val of invalid type"
		);

		//Should ignore and warn on wrong 'value' type
		param.clipmode = \none;
		testValue = 299;
		param.value = testValue;
		param.value = \notAValidValueType;
		this.assertEquals(
			param.value, testValue, "IntegerParameter value wasn't changed by val of invalid type"
		);
	}

	test_Clipmode{
		var param = VTMIntegerParameter.new('myInteger');
		var testValue;
		param.minVal = 0;
		param.maxVal = 1000;

		//Should not clip values when clipmode == none
		param.clipmode = \none; //should be set by default
		testValue = -11;
		param.value = testValue;
		this.assertEquals(
			param.value, testValue, "IntegerParameter lower value was not clipped in clipmode 'none'"
		);
		testValue = 1234;
		param.value = testValue;
		this.assertEquals(
			param.value, testValue, "IntegerParameter higher value was not clipped in clipmode 'none'"
		);

		//Should clip larger values than maxVal when clipmode == high
		param.value = 500;
		param.clipmode = \high;
		testValue = 1500;
		param.value = testValue;
		this.assertEquals(
			param.value, param.maxVal, "IntegerParameter higher value was clipped in clipmode 'high'"
		);
		testValue = -230;
		param.value = testValue;
		this.assertEquals(
			param.value, testValue, "IntegerParameter lower value was not clipped in clipmode 'high'"
		);

		//Should clip smaller values than minVal when clipmode == low
		param.value = 500;
		param.clipmode = \low;
		testValue = 19999;
		param.value = testValue;
		this.assertEquals(
			param.value, testValue, "IntegerParameter higher value was not clipped in clipmode 'low'"
		);
		testValue = -2354;
		param.value = testValue;
		this.assertEquals(
			param.value, param.minVal, "IntegerParameter lower value was clipped in clipmode 'low'"
		);

		//Should clip all values outside range when clipmode == both
		param.value = 500;
		param.clipmode = \both;
		testValue = 1001;
		param.value = testValue;
		this.assertEquals(
			param.value, param.maxVal, "IntegerParameter higher value was clipped in clipmode 'both'"
		);
		testValue = -1;
		param.value = testValue;
		this.assertEquals(
			param.value, param.minVal, "IntegerParameter lower value was clipped in clipmode 'both'"
		);
	}

	test_StepsizeIncrementAndDecrement{
		var param = VTMIntegerParameter.new('myInteger');
		var testValue, testStepsize, wasRun, wasCorrectValue;
		param.minVal = 0;
		param.maxVal = 10;
		param.clipmode = \both;

		//Stepsize should be positive numbers only
		//Convert into absolute value and print warning if negative value is used.
		testStepsize = -1;
		param.stepsize = testStepsize;
		this.assertEquals(
			param.stepsize, testStepsize.abs, "IntegerParameter converted negative stepsize to positive"
		);

		//Should increment according to stepsize
		testValue = 1;
		testStepsize = 2;
		param.stepsize = testStepsize;
		param.value = testValue;
		{ param.increment; } ! 3; // increment 3 times
		this.assertEquals(
			param.value, testValue + (testStepsize * 3), "IntegerParameter value was incremented correctly"
		);

		//Should run action send new value to it
		wasRun = false;
		wasCorrectValue = false;
		testValue = 1;
		testStepsize = 5;
		param.clipmode = \none;
		param.action = {arg p;
			wasRun = true;
			wasCorrectValue = (testValue + testStepsize) == p.value; };
		param.value = testValue;
		param.stepsize = testStepsize;
		param.increment;
		this.assert(
			wasRun and: {wasCorrectValue}, "IntegerParameter ran action with correct value"
		);

		//Should decrement according to stepsize
		testValue = -20;
		testStepsize = 7;
		param.value = testValue;
		param.stepsize = testStepsize;
		{param.decrement} ! 2;
		this.assertEquals(
			param.value, testValue - (testStepsize * 2), "IntegerParameter value was decremented correctly"
		);

		//Should not increment outside range if clipped
		testValue = 99;
		testStepsize = 3;
		param.minVal = -100;
		param.maxVal = 100;
		param.value = testValue;
		param.stepsize = testStepsize;
		param.clipmode = \both;
		{ param.increment; } ! 5;
		this.assertEquals(
			param.value, param.maxVal, "IntegerParameter value was clipped when incremented more than maxVal"
		);

		//Should not decrement outside range if clipped
		testValue = -99;
		testStepsize = 10;
		param.minVal = -100;
		param.maxVal = 100;
		param.value = testValue;
		param.stepsize = testStepsize;
		param.clipmode = \both;
		{ param.decrement; } ! 5;
		this.assertEquals(
			param.value, param.minVal, "IntegerParameter value was clipped when decremented more than minVal"
		);

		//Should disable doAction optionally when incrementing
		wasRun = false;
		param.action = {arg p; wasRun = true};
		testValue = 0;
		testStepsize = 3;
		param.value = testValue;
		param.stepsize = testStepsize;
		param.increment(doAction: false);
		this.assert(
			param.value == (testValue + testStepsize) and: {wasRun.not},
			"IntegerParameter value was incremented with optional doAction false causing action not to be run"
		);

		//Should disable doAction optionally when decrementing
		wasRun = false;
		param.action = {arg p; wasRun = true};
		testValue = 0;
		testStepsize = 3;
		param.value = testValue;
		param.stepsize = testStepsize;
		param.decrement(doAction: false);
		this.assert(
			param.value == (testValue - testStepsize) and: {wasRun.not},
			"IntegerParameter value was decremented with optional doAction false causing action not to be run"
		)

	}

	test_UpdateValueWhenMinValAndMaxValChange{
		var wasRun = false;
		var param = VTMIntegerParameter.new('myValue');
		param.minVal = 0;
		param.maxVal = 30;
		param.action = {|p| wasRun = true; };
		param.clipmode = \both;
		param.value = 20; //set the value higher than defined maxVal

		//Should update the value after maxVal set
		//Should not run the action when maxVal change
		param.maxVal = 15;
		this.assertEquals(
			param.value, param.maxVal,
			"IntegerParameter value adjusted value when maxVal changed"
		);
		this.assert(
			wasRun.not,
			"IntegerParameter - adjusting out-of-range value for maxVal did not run action"
		);

		//Should update the value after minVal set
		//Should not run the action when minVal change
		wasRun = false;
		param.minVal = 0;
		param.maxVal = 50;
		param.value = 10;
		param.minVal = 15;
		this.assertEquals(
			param.value, param.minVal,
			"IntegerParameter value adjusted value when minVal changed"
		);
		this.assert(
			wasRun.not,
			"IntegerParameter - adjusting out-of-range value for minVal did not run action"
		);
	}


	test_AllowSettingMinValAndMaxValToNil{
		var param = VTMIntegerParameter.new('myInteger');
		param.minVal = 0;
		param.maxVal = 100;

		//Should allow setting minVal to nil
		try{
			param.minVal = nil;
			this.assert(
				param.minVal.isNil,
				"IntegerParameter allowed setting minVal to nil"
			);
		} {
			this.failed(thisMethod,
				"IntegerParameter failed to allow setting minVal to nil"
			);
		};

		//Should allow setting maxVal to nil
		try{
			param.maxVal = nil;
			this.assert(
				param.maxVal.isNil,
				"IntegerParameter allowed setting maxVal to nil"
			);
		} {
			this.failed(thisMethod,
				"IntegerParameter failed to allow setting maxVal to nil"
			);
		};
	}


	test_OnlyClipValuesWhenMinValAndMaxValAreDefined{
		var param = VTMIntegerParameter.new('myInteger');
		var testValue;

		//Should only check for lower value in clipmode 'both' if 'maxVal' is not defined
		param.clipmode = \both;
		testValue = 999999;
		param.value = testValue;
		param.maxVal = nil;
		param.minVal = 0;
		this.assert(
			param.maxVal.isNil and: {param.value == testValue},
			message: "IntegerParameter didn't clip value when maxVal was nil."
		);
		testValue = -999999;
		param.value = testValue;
		this.assertEquals(
			param.value, param.minVal,
			"IntegerParameter clipped value to minVal in clipmode 'both' when maxVal was nil."
		);

		//Should only check for higher value in clipmode 'both' if 'minVal' is not defined
		param.clipmode = \both;
		param.minVal = nil;
		param.maxVal = 1000;
		testValue = -999999;
		param.value = testValue;
		this.assert(
			param.minVal.isNil and: {param.value == testValue},
			message: "IntegerParameter didn't clip value when minVal was nil."
		);
		testValue = 999999;
		param.value = testValue;
		this.assertEquals(
			param.value, param.maxVal,
			"IntegerParameter clipped value to maxVal in clipmode 'both' when minVal was nil."
		);
	}

	test_UpdateValueWhenClipmodeChange{
		var param = VTMIntegerParameter.new('myInteger');
		var wasRun = false;
		var testValue;
		param.minVal = -20;
		param.maxVal = 22;
		param.clipmode = \none;
		param.action = {|p| wasRun = true; };
		param.value = 25;

		//Should update the higher-than-maxVal-value when clipmode changes from 'none' to 'high'
		param.clipmode = \high;
		this.assertEquals(
			param.value, param.maxVal,
			"IntegerParameter - adjusted value when clipmode changed to 'high'"
		);
		//Should not run the action when clipmode changed
		this.assert(
			wasRun.not, "IntegerParameter didn't run action when adjusting the clipmode"
		);

		//Should update the lower-than-minVal-value when clipmode changes 'none' to 'low'
		param.clipmode = \none;
		param.value = -100;
		param.minVal = -55;
		param.minVal = 0;
		param.clipmode = \low;
		this.assertEquals(
			param.value, param.minVal,
			"IntegerParameter - adjusted value clipmode changed to 'low'"
		);

		//Should not adjust value when it is higher than maxVal, and clipmode is set
		//to 'low'
		param.clipmode = \none;
		param.minVal = 0;
		param.maxVal = 12;
		testValue = 1120;
		param.value = testValue;
		param.clipmode = \low;
		this.assertEquals(
			param.value, testValue,
			"IntegerParameter - did not adjust higher value when clipmode changed to 'low'"
		);

		//Should not adjust value when it is lower than minVal, and clipmode is set
		//to 'high'
		param.clipmode = \none;
		param.minVal = -10;
		param.maxVal = 3000;
		testValue = -5000;
		param.value = testValue;
		param.clipmode = \high;
		this.assertEquals(
			param.value, testValue,
			"IntegerParameter - did not adjust higher value when clipmode changed to 'low'"
		);


		//Should adjust value if value is higher than maxVal and clipmode set to 'both'
		param.clipmode = \none;
		param.minVal = 0;
		param.maxVal = 20;
		param.value = 22;
		param.clipmode = \both;
		this.assertEquals(
			param.value, param.maxVal,
			"IntegerParameter adjusted higher value when clipmode set to 'both'"
		);

		//Should adjust value if value is lower than minVal and clipmode is set to 'both'
		param.clipmode = \none;
		param.minVal = -1110;
		param.maxVal = 2220;
		param.value = -2229;
		param.clipmode = \both;
		this.assertEquals(
			param.value, param.minVal,
			"IntegerParameter adjusted lower value when clipmode set to 'both'"
		);

		//Should not adjust higher value in clipmode transition 'low' to 'none'
		param.clipmode = \low;
		param.minVal = 0;
		param.maxVal = 110;
		testValue = 990;
		param.value = testValue;
		param.clipmode = \none;
		this.assertEquals(
			param.value, testValue,
			"IntegerParameter kept higher value when clipmode went from 'low' 'none'"
		);

		//Should not adjust lower value in clipmode transition 'high' to 'none'
		param.clipmode = \high;
		param.minVal = -10;
		param.maxVal = 1460;
		testValue = -990;
		param.value = testValue;
		param.clipmode = \none;
		this.assertEquals(
			param.value, testValue,
			"IntegerParameter kept higher value when clipmode went from 'low' 'none'"
		);

	}


	test_MinValShouldNotBeLowerThanMaxValAndViceVersa{}


	test_SetAttributesIndeclaration{
		var declaration = IdentityDictionary[
			\path -> '/myValuePath/tester',
			\action -> {|p| p.value * 21; },
			\enabled -> true,
			\defaultValue -> 111,
			\value -> 91,
			\typecheck -> true,
			\filterRepetitions -> false,
			\minVal -> -1100,
			\maxVal -> 1234,
			\stepsize -> 12,
			\clipmode -> \low
		];
		var param = VTMIntegerParameter.new('myValue', declaration);
		this.assertEquals(
			param.path, declaration[\path], "IntegerParameter set path through declaration"
		);
		this.assertEquals(
			param.fullPath, '/myValuePath/tester/myValue', "IntegerParameter set fullPath through declaration"
		);
		this.assertEquals(
			param.action, declaration[\action], "IntegerParameter set action through declaration"
		);
		this.assertEquals(
			param.enabled, declaration[\enabled], "IntegerParameter set enabled through declaration"
		);
		this.assertEquals(
			param.defaultValue, declaration[\defaultValue], "IntegerParameter set defaultValue through declaration"
		);
		this.assertEquals(
			param.value, declaration[\value], "IntegerParameter set value through declaration"
		);
		this.assertEquals(
			param.typecheck, declaration[\typecheck], "IntegerParameter set typecheck through declaration"
		);
		this.assertEquals(
			param.filterRepetitions, declaration[\filterRepetitions],
			"IntegerParameter set filterRepetitions through declaration"
		);
		this.assertEquals(
			param.minVal, declaration[\minVal], "IntegerParameter set minVal through declaration"
		);
		this.assertEquals(
			param.maxVal, declaration[\maxVal], "IntegerParameter set maxVal through declaration"
		);
		this.assertEquals(
			param.stepsize, declaration[\stepsize], "IntegerParameter set stepsize through declaration"
		);
		this.assertEquals(
			param.clipmode, declaration[\clipmode], "IntegerParameter set clipmode through declaration"
		);
	}

	test_GetAttributes{
		var declaration = IdentityDictionary[
			\path -> '/myValuePath/tester',
			\action -> {|p| p.value * 21; },
			\enabled -> true,
			\defaultValue -> 111,
			\value -> 91,
			\typecheck -> true,
			\filterRepetitions -> false,
			\minVal -> -1100,
			\maxVal -> 1234,
			\stepsize -> 12,
			\clipmode -> \low
		];
		var testAttributes;
		var param = VTMIntegerParameter.new('myValue', declaration);
		testAttributes = declaration.deepCopy.put(\name, 'myValue');
		testAttributes.put(\action, testAttributes[\action].asCompileString);
		this.assertEquals(
			param.attributes, testAttributes, "IntegerParameter returned correct attributes"
		);
	}

}