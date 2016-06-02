TestVTMScalarParameter : UnitTest {
	setUp{
		"Setting up a VTMScalarParameterTest".postln;
	}

	tearDown{
		"Tearing down a VTMScalarParameterTest".postln;
	}

	test_DefaultAttributes{
		var param = VTMScalarParameter.new('myScalar');
		//defaultValue should default to the class default value
		this.assertEquals(
			param.defaultValue, param.prDefaultValueForType, "ScalarParameter defaultValue defaults to 0"
		);
		//value should default to 0 if value not defined
		this.assertEquals(
			param.value, param.defaultValue, "ScalarParameter value defaults to 0"
		);
		//stepsize should default to 0
		this.assertEquals(
			param.stepsize, 0, "ScalarParameter stepsize defaults to 0"
		);
		//clipmode should default to 'none'
		this.assertEquals(
			param.clipmode, \none, "ScalarParameter clipmode defaults to 'none'"
		);
		//minVal should default to nil (this might change later)
		this.assert(
			param.minVal.isNil, "ScalarParameter minVal defaults to nil"
		);
		//maxVal should default to nil (this might change later)
		this.assert(
			param.maxVal.isNil, "ScalarParameter maxVal defaults to nil"
		);
	}

	test_SetGetValue{
		var param = VTMScalarParameter.new('myScalar');
		var testValue = 50;
		//SetGet 'value'
		param.value = testValue;
		this.assertEquals(
			param.value, testValue, "ScalarParameter value was set"
		);
	}

	test_OnlyTypecheckWhenTypecheckIsTrue{}

	test_OnlyCheckRangesWhenMinValAndMaxValAreDefined{
		var param = VTMScalarParameter.new('myScalar');
		var testValue = 50;

		//Should only check higher values when maxVal is not nil

		//Should only check lower values when minVal is not nil
	}

	test_SetGetAttributes{
		var param = VTMScalarParameter.new('myScalar');
		var testValue;
		//SetGet 'minVal'
		testValue = -2;
		param.minVal = testValue;
		this.assertEquals(
			param.minVal, testValue, "ScalarParameter minVal was set"
		);
		//SetGet 'maxVal'
		testValue = 200;
		param.maxVal = testValue;
		this.assertEquals(
			param.maxVal, testValue, "ScalarParameter maxVal was set"
		);
		//SetGet 'stepsize'
		testValue = 11;
		param.stepsize = testValue;
		this.assertEquals(
			param.stepsize, testValue, "ScalarParameter stepsize was set"
		);
		//SetGet 'clipmode'
		testValue = \high;
		param.clipmode = testValue;
		this.assertEquals(
			param.clipmode, testValue, "ScalarParameter clipmode was set"
		);
	}

	test_IgnoreAndWarnOnWrongTypes{
		var testValue;
		var param = VTMScalarParameter.new('myScalar');

		//Should ignore and warn on wrong 'minVal' type
		testValue = -100;
		param.minVal = testValue;
		param.minVal = \notAValidValue;
		this.assertEquals(
			param.minVal, testValue, "ScalarParameter minVal wasn't changed by val of invalid type"
		);

		//Should ignore and warn on wrong 'maxVal' type
		testValue = 300;
		param.maxVal = testValue;
		param.maxVal = \notAValidValue;
		this.assertEquals(
			param.maxVal, testValue, "ScalarParameter maxVal wasn't changed by val of invalid type"
		);

		//Should ignore and warn on wrong 'stepsize' type
		testValue = 11;
		param.stepsize = testValue;
		param.stepsize = \notAValidValue;
		this.assertEquals(
			param.stepsize, testValue, "ScalarParameter stepsize wasn't changed by val of invalid type"
		);

		//Should ignore and warn on wrong 'clipmode' type
		testValue = \low;
		param.clipmode = testValue;
		param.clipmode = 4321;
		this.assertEquals(
			param.clipmode, testValue, "ScalarParameter clipmode wasn't changed by val of invalid type"
		);

		//Should ignore and warn on wrong 'value' type
		param.clipmode = \none;
		testValue = 299;
		param.value = testValue;
		param.value = \notAValidValueType;
		this.assertEquals(
			param.value, testValue, "ScalarParameter value wasn't changed by val of invalid type"
		);
	}

	test_Clipmode{
		var param = VTMScalarParameter.new('myScalar');
		var testValue;
		param.minVal = 0.0;
		param.maxVal = 1.0;

		//Should not clip values when clipmode == none
		param.clipmode = \none; //should be set by default
		testValue = -0.1;
		param.value = testValue;
		this.assertEquals(
			param.value, testValue, "ScalarParameter lower value was not clipped in clipmode 'none'"
		);
		testValue = 1.01;
		param.value = testValue;
		this.assertEquals(
			param.value, testValue, "ScalarParameter higher value was not clipped in clipmode 'none'"
		);

		//Should clip larger values than maxVal when clipmode == high
		param.clipmode = \high;
		testValue = 1.5;
		param.value = testValue;
		this.assertEquals(
			param.value, param.maxVal, "ScalarParameter higher value was clipped in clipmode 'high'"
		);
		testValue = -2.3;
		param.value = testValue;
		this.assertEquals(
			param.value, testValue, "ScalarParameter lower value was not clipped in clipmode 'high'"
		);

		//Should clip smaller values than minVal when clipmode == low
		param.clipmode = \low;
		testValue = 3.0;
		param.value = testValue;
		this.assertEquals(
			param.value, testValue, "ScalarParameter higher value was not clipped in clipmode 'low'"
		);
		testValue = -3.1;
		param.value = testValue;
		this.assertEquals(
			param.value, param.minVal, "ScalarParameter lower value was clipped in clipmode 'low'"
		);

		//Should clip all values outside range when clipmode == both
		param.clipmode = \both;
		testValue = 1.1;
		param.value = testValue;
		this.assertEquals(
			param.value, param.maxVal, "ScalarParameter higher value was clipped in clipmode 'both'"
		);
		testValue = -0.2;
		param.value = testValue;
		this.assertEquals(
			param.value, param.minVal, "ScalarParameter lower value was clipped in clipmode 'both'"
		);
	}

	test_StepsizeIncrementAndDecrement{
		var param = VTMScalarParameter.new('myScalar');
		var testValue, testStepsize, wasRun, wasCorrectValue;
		param.minVal = 0;
		param.maxVal = 10;
		param.clipmode = \both;

		//Stepsize should be positive numbers only
		//Convert into absolute value and print warning if negative value is used.
		testStepsize = -1;
		param.stepsize = testStepsize;
		this.assertEquals(
			param.stepsize, testStepsize.abs, "ScalarParameter converted negative stepsize to positive"
		);

		//Should increment according to stepsize
		testValue = 1;
		testStepsize = 2;
		param.stepsize = testStepsize;
		param.value = testValue;
		{ param.increment; } ! 3; // increment 3 times
		this.assertEquals(
			param.value, testValue + (testStepsize * 3), "ScalarParameter value was incremented correctly"
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
			wasRun and: {wasCorrectValue}, "ScalarParameter ran action with correct value"
		);

		//Should decrement according to stepsize
		testValue = -20;
		testStepsize = 7;
		param.value = testValue;
		param.stepsize = testStepsize;
		{param.decrement} ! 2;
		this.assertEquals(
			param.value, testValue - (testStepsize * 2), "ScalarParameter value was decremented correctly"
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
			param.value, param.maxVal, "ScalarParameter value was clipped when incremented more than maxVal"
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
			param.value, param.minVal, "ScalarParameter value was clipped when decremented more than minVal"
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
			"ScalarParameter value was incremented with optional doAction false causing action not to be run"
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
			"ScalarParameter value was decremented with optional doAction false causing action not to be run"
		)


	}

	test_UpdateValueWhenMinValAndMaxValChange{
		var wasRun = false;
		var param = VTMScalarParameter.new('myValue');
		param.minVal = 0.0;
		param.maxVal = 3.0;
		param.action = {|p| wasRun = true; };
		param.clipmode = \both;
		param.value = 2.0; //set the value higher than defined maxVal

		//Should update the value after maxVal set
		//Should not run the action when maxVal change
		param.maxVal = 1.1;
		this.assertEquals(
			param.value, param.maxVal,
			"ScalarParameter value adjusted value when maxVal changed"
		);
		this.assert(
			wasRun.not,
			"ScalarParameter - adjusting out-of-range value for maxVal did not run action"
		);

		//Should update the value after minVal set
		//Should not run the action when minVal change
		wasRun = false;
		param.minVal = 0.0;
		param.maxVal = 5.0;
		param.value = 1.0;
		param.minVal = 1.5;
		this.assertEquals(
			param.value, param.minVal,
			"ScalarParameter value adjusted value when minVal changed"
		);
		this.assert(
			wasRun.not,
			"ScalarParameter - adjusting out-of-range value for minVal did not run action"
		);
	}

	test_AllowSettingMinValAndMaxValToNil{
		var param = VTMScalarParameter.new('myScalar');
		param.minVal = 0.0;
		param.maxVal = 1.0;

		//Should allow setting minVal to nil
		try{
			param.minVal = nil;
			this.assert(
				param.minVal.isNil,
				"ScalarParameter allowed setting minVal to nil"
			);
		} {
			this.failed(thisMethod,
				"ScalarParameter failed to allow setting minVal to nil"
			);
		};

		//Should allow setting maxVal to nil
		try{
			param.maxVal = nil;
			this.assert(
				param.maxVal.isNil,
				"ScalarParameter allowed setting maxVal to nil"
			);
		} {
			this.failed(thisMethod,
				"ScalarParameter failed to allow setting maxVal to nil"
			);
		};
	}


	test_OnlyClipValuesWhenMinValAndMaxValAreDefined{
		var param = VTMScalarParameter.new('myScalar');
		var testValue;

		//Should only check for lower value in clipmode 'both' if 'maxVal' is not defined
		param.clipmode = \both;
		testValue = 999999.0;
		param.value = testValue;
		param.maxVal = nil;
		param.minVal = 0.0;
		this.assert(
			param.maxVal.isNil and: {param.value == testValue},
			message: "ScalarParameter didn't clip value when maxVal was nil."
		);
		testValue = -999999.0;
		param.value = testValue;
		this.assertEquals(
			param.value, param.minVal,
			"ScalarParameter clipped value to minVal in clipmode 'both' when maxVal was nil."
		);

		//Should only check for higher value in clipmode 'both' if 'minVal' is not defined
		param.clipmode = \both;
		param.minVal = nil;
		param.maxVal = 1000.0;
		testValue = -999999.0;
		param.value = testValue;
		this.assert(
			param.minVal.isNil and: {param.value == testValue},
			message: "ScalarParameter didn't clip value when minVal was nil."
		);
		testValue = 999999.0;
		param.value = testValue;
		this.assertEquals(
			param.value, param.maxVal,
			"ScalarParameter clipped value to maxVal in clipmode 'both' when minVal was nil."
		);
	}

	test_UpdateValueWhenClipmodeChange{
		var param = VTMScalarParameter.new('myScalar');
		var wasRun = false;
		var testValue;
		param.minVal = -2.0;
		param.maxVal = 22.0;
		param.clipmode = \none;
		param.action = {|p| wasRun = true; };
		param.value = 25;

		//Should update the higher-than-maxVal-value when clipmode changes from 'none' to 'high'
		param.clipmode = \high;
		this.assertEquals(
			param.value, param.maxVal,
			"ScalarParameter - adjusted value when clipmode changed to 'high'"
		);
		//Should not run the action when clipmode changed
		this.assert(
			wasRun.not, "ScalarParameter didn't run action when adjusting the clipmode"
		);

		//Should update the lower-than-minVal-value when clipmode changes 'none' to 'low'
		param.clipmode = \none;
		param.value = -10.0;
		param.minVal = -5.5;
		param.minVal = 0.0;
		param.clipmode = \low;
		this.assertEquals(
			param.value, param.minVal,
			"ScalarParameter - adjusted value clipmode changed to 'low'"
		);

		//Should not adjust value when it is higher than maxVal, and clipmode is set
		//to 'low'
		param.clipmode = \none;
		param.minVal = 0.0;
		param.maxVal = 1.2;
		testValue = 112.0;
		param.value = testValue;
		param.clipmode = \low;
		this.assertEquals(
			param.value, testValue,
			"ScalarParameter - did not adjust higher value when clipmode changed to 'low'"
		);

		//Should not adjust value when it is lower than minVal, and clipmode is set
		//to 'high'
		param.clipmode = \none;
		param.minVal = -1.0;
		param.maxVal = 300.0;
		testValue = -500.0;
		param.value = testValue;
		param.clipmode = \high;
		this.assertEquals(
			param.value, testValue,
			"ScalarParameter - did not adjust higher value when clipmode changed to 'low'"
		);


		//Should adjust value if value is higher than maxVal and clipmode set to 'both'
		param.clipmode = \none;
		param.minVal = 0.0;
		param.maxVal = 2.0;
		param.value = 2.222;
		param.clipmode = \both;
		this.assertEquals(
			param.value, param.maxVal,
			"ScalarParameter adjusted higher value when clipmode set to 'both'"
		);

		//Should adjust value if value is lower than minVal and clipmode is set to 'both'
		param.clipmode = \none;
		param.minVal = -111.0;
		param.maxVal = 222.0;
		param.value = -222.999;
		param.clipmode = \both;
		this.assertEquals(
			param.value, param.minVal,
			"ScalarParameter adjusted lower value when clipmode set to 'both'"
		);

		//Should not adjust higher value in clipmode transition 'low' to 'none'
		param.clipmode = \low;
		param.minVal = 0.0;
		param.maxVal = 11.0;
		testValue = 99.0;
		param.value = testValue;
		param.clipmode = \none;
		this.assertEquals(
			param.value, testValue,
			"ScalarParameter kept higher value when clipmode went from 'low' 'none'"
		);

		//Should not adjust lower value in clipmode transition 'high' to 'none'
		param.clipmode = \high;
		param.minVal = -1.0;
		param.maxVal = 146.0;
		testValue = -99.0;
		param.value = testValue;
		param.clipmode = \none;
		this.assertEquals(
			param.value, testValue,
			"ScalarParameter kept higher value when clipmode went from 'low' 'none'"
		);

	}


	test_MinValShouldNotBeLowerThanMaxValAndViceVersa{}


	test_SetAttributesInDescription{
		var description = IdentityDictionary[
			\path -> '/myValuePath/tester',
			\action -> {|p| p.value * 2.1; },
			\enabled -> true,
			\defaultValue -> 1.11,
			\value -> 0.91,
			\typecheck -> true,
			\filterRepetitions -> false,
			\minVal -> -1100.0,
			\maxVal -> 1234.0,
			\stepsize -> 12,
			\clipmode -> \low
		];
		var param = VTMScalarParameter.new('myValue', description);
		this.assertEquals(
			param.path, description[\path], "ScalarParameter set path through description"
		);
		this.assertEquals(
			param.fullPath, '/myValuePath/tester/myValue', "ScalarParameter set fullPath through description"
		);
		this.assertEquals(
			param.action, description[\action], "ScalarParameter set action through description"
		);
		this.assertEquals(
			param.enabled, description[\enabled], "ScalarParameter set enabled through description"
		);
		this.assertEquals(
			param.defaultValue, description[\defaultValue], "ScalarParameter set defaultValue through description"
		);
		this.assertEquals(
			param.value, description[\value], "ScalarParameter set value through description"
		);
		this.assertEquals(
			param.typecheck, description[\typecheck], "ScalarParameter set typecheck through description"
		);
		this.assertEquals(
			param.filterRepetitions, description[\filterRepetitions],
			"ScalarParameter set filterRepetitions through description"
		);
		this.assertEquals(
			param.minVal, description[\minVal], "ScalarParameter set minVal through description"
		);
		this.assertEquals(
			param.maxVal, description[\maxVal], "ScalarParameter set maxVal through description"
		);
		this.assertEquals(
			param.stepsize, description[\stepsize], "ScalarParameter set stepsize through description"
		);
		this.assertEquals(
			param.clipmode, description[\clipmode], "ScalarParameter set clipmode through description"
		);


	}

	test_GetAttributes{
		var description = IdentityDictionary[
			\path -> '/myValuePath/tester',
			\action -> {|p| p.value * 2.1; },
			\enabled -> true,
			\defaultValue -> 1.11,
			\value -> 0.91,
			\typecheck -> true,
			\filterRepetitions -> false,
			\minVal -> -1100.0,
			\maxVal -> 1234.0,
			\stepsize -> 12,
			\clipmode -> \low
		];
		var testAttributes;
		var param = VTMScalarParameter.new('myValue', description);
		testAttributes = description.deepCopy.put(\name, 'myValue');
		testAttributes.put(\action, testAttributes[\action].asCompileString);
		this.assertEquals(
			param.attributes, testAttributes, "ScalarParameter returned correct attributes"
		);
	}
}