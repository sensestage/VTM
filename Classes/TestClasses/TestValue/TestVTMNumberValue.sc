TestVTMNumberValue : TestVTMValue {

	*classesForTesting{
		^[
			VTMDecimalValue,
			VTMIntegerValue
		];
	}

	*makeRandomAttribute{arg key, params;
		var result;
		result = super.makeRandomAttribute(key, params);
		if(result.isNil, {
			switch(key,
				\minVal, { result = this.makeRandomValue(params); },
				\maxVal, { result = this.makeRandomValue(params); },
				\stepsize, { result = this.makeRandomValue(params ? (minVal: 0.0, maxVal: 1000.0)).abs; },
				\clipmode, { result = [\none, \low, \high, \both].choose; }
			);
		});
		^result;
	}

	test_DefaultProperties{
		this.class.classesForTesting.do({arg class;
			var testClass, testValue;
			var name = "my%".format(class.name);
			var param = class.new(name);
			testClass = this.class.testclassForType( class.type );

			this.assertEquals(
				param.defaultValue, param.class.prDefaultValueForType, "NumberValue defaultValue defaults to 0"
			);
			//value should default to 0 if value not defined
			this.assertEquals(
				param.value, param.defaultValue, "NumberValue value defaults to 0"
			);
			//stepsize should default to 0
			this.assertEquals(
				param.stepsize, 0, "NumberValue stepsize defaults to 0"
			);
			//clipmode should default to 'none'
			this.assertEquals(
				param.clipmode, \none, "NumberValue clipmode defaults to 'none'"
			);
			//minVal should default to nil (this might change later)
			this.assert(
				param.minVal.isNil, "NumberValue minVal defaults to nil"
			);
			//maxVal should default to nil (this might change later)
			this.assert(
				param.maxVal.isNil, "NumberValue maxVal defaults to nil"
			);
			param.free;
		});
	}

	test_SetGetValue{
		this.class.classesForTesting.do({arg class;
			var testClass, testValue;
			var name = "my%".format(class.name);
			var param = class.new(name);
			testClass = this.class.testclassForType( class.type );
			testValue = testClass.makeRandomValue;
			param.value = testValue;
			this.assertEquals(
				param.value, testValue, "NumberValue value was set"
			);
		});
	}

	// test_OnlyCheckRangesWhenMinValAndMaxValAreDefined{
	// 	this.class.classesForTesting.do({arg class;
	// 		var testClass, testValue;
	// 		var name = "my%".format(class.name);
	// 		var param = class.new(name);
	// 		var testMinVal, testMaxVal;
	// 		testClass = this.class.testclassForType( class.type );
	//
	// 		//Should only check higher values when maxVal is not nil
	// 		testValue = testClass.makeRandomValue();
	// 		testMaxVal = testClass.makeRandomValue((minVal: testValue + 1));
	// 		param.value = testValue;
	//
	//
	// 		//Should only check lower values when minVal is not nil
	//
	// 		this.assertEquals(
	// 			param.value, testValue, "NumberValue value was set"
	// 		);
	// 	});
	// }

	test_SetGetProperties{
		this.class.classesForTesting.do({arg class;
			var testClass, testValue;
			var name = "my%".format(class.name);
			var param = class.new(name);
			testClass = this.class.testclassForType( class.type );
			//SetGet 'minVal'
			testValue = testClass.makeRandomValue;
			param.minVal = testValue;
			this.assertEquals(
				param.minVal, testValue, "NumberValue minVal was set"
			);
			//SetGet 'maxVal'
			testValue = testClass.makeRandomValue;
			param.maxVal = testValue;
			this.assertEquals(
				param.maxVal, testValue, "NumberValue maxVal was set"
			);
			//SetGet 'stepsize'
			testValue = testClass.makeRandomValue.abs;
			param.stepsize = testValue;
			this.assertEquals(
				param.stepsize, testValue, "NumberValue stepsize was set"
			);
			//SetGet 'clipmode'
			testValue = testClass.makeRandomAttribute(\clipmode);
			param.clipmode = testValue;
			this.assertEquals(
				param.clipmode, testValue.asSymbol, "NumberValue clipmode was set"
			);
		});
	}

	test_IgnoreAndWarnOnWrongTypes{
		this.class.classesForTesting.do({arg class;
			var testClass, testValue;
			var name = "my%".format(class.name);
			var param = class.new(name);
			testClass = this.class.testclassForType( class.type );

			//Should ignore and warn on wrong 'minVal' type
			testValue = -100;
			param.minVal = testValue;
			param.minVal = \notAValidValue;
			this.assertEquals(
				param.minVal, testValue, "% minVal wasn't changed by val of invalid type".format(testClass)
			);

			//Should ignore and warn on wrong 'maxVal' type
			testValue = 300;
			param.maxVal = testValue;
			param.maxVal = \notAValidValue;
			this.assertEquals(
				param.maxVal, testValue, "% maxVal wasn't changed by val of invalid type".format(testClass)
			);

			//Should ignore and warn on wrong 'stepsize' type
			testValue = 11;
			param.stepsize = testValue;
			param.stepsize = \notAValidValue;
			this.assertEquals(
				param.stepsize, testValue, "% stepsize wasn't changed by val of invalid type".format(testClass)
			);

			//Should ignore and warn on wrong 'clipmode' type
			testValue = \low;
			param.clipmode = testValue;
			param.clipmode = 4321;
			this.assertEquals(
				param.clipmode, testValue, "% clipmode wasn't changed by val of invalid type".format(testClass)
			);
		});
	}

	test_CheckValueTypeValidity{
		this.class.classesForTesting.do({arg class;
			var testClass, testValue;
			var name = "my%".format(class.name);
			var param = class.new(name);
			var invalidValue;
			testClass = this.class.testclassForType( class.type );

			//type validation should block setting value
			param.clipmode = \none;
			testValue = 299;
			param.value = testValue;
			invalidValue = \notANumber;
			if(param.isValidType(invalidValue), {
				param.value = invalidValue;
			});
			this.assertEquals(
				param.value, testValue, "% value wasn't changed by val of invalid type".format(testClass)
			);
		});
	}

	test_Clipmode{
		this.class.classesForTesting.do({arg class;
			var testClass, testValue;
			var name = "my%".format(class.name);
			var param = class.new(name);
			testClass = this.class.testclassForType( class.type );
			param.minVal = testClass.makeRandomValue((minVal: -100.0, maxVal: 100.0));
			param.maxVal = testClass.makeRandomValue((minVal: 101.0, maxVal: 1000.0));

			//Should not clip values when clipmode == none
			param.clipmode = \none; //should be set by default
			testValue = testClass.makeRandomValue((minVal: -200.0, maxVal: -101.0));
			param.value = testValue;
			this.assertEquals(
				param.value, testValue, "% lower value was not clipped in clipmode 'none'".format(testClass)
			);
			testValue = testClass.makeRandomValue((minVal: 1001.0, maxVal: 2000.0));
			param.value = testValue;
			this.assertEquals(
				param.value, testValue, "% higher value was not clipped in clipmode 'none'".format(testClass)
			);

			//Should clip larger values than maxVal when clipmode == high
			param.clipmode = \high;
			testValue = testClass.makeRandomValue((minVal: 1001.0, maxVal: 2000.0));
			param.value = testValue;
			this.assertEquals(
				param.value, param.maxVal, "% higher value was clipped in clipmode 'high'".format(testClass)
			);
			testValue = testClass.makeRandomValue((minVal: -200.0, maxVal: -101.0));
			param.value = testValue;
			this.assertEquals(
				param.value, testValue, "% lower value was not clipped in clipmode 'high'".format(testClass)
			);

			//Should clip smaller values than minVal when clipmode == low
			param.clipmode = \low;
			testValue = testClass.makeRandomValue((minVal: 1001.0, maxVal: 2000.0));
			param.value = testValue;
			this.assertEquals(
				param.value, testValue, "% higher value was not clipped in clipmode 'low'".format(testClass)
			);
			testValue = testClass.makeRandomValue((minVal: -200.0, maxVal: -101.0));
			param.value = testValue;
			this.assertEquals(
				param.value, param.minVal, "% lower value was clipped in clipmode 'low'".format(testClass)
			);

			//Should clip all values outside range when clipmode == both
			param.clipmode = \both;
			testValue = testClass.makeRandomValue((minVal: 1001.0, maxVal: 2000.0));
			param.value = testValue;
			this.assertEquals(
				param.value, param.maxVal, "% higher value was clipped in clipmode 'both'".format(testClass)
			);
			testValue = testClass.makeRandomValue((minVal: -200.0, maxVal: -101.0));
			param.value = testValue;
			this.assertEquals(
				param.value, param.minVal, "% lower value was clipped in clipmode 'both'".format(testClass)
			);
		});
	}

	test_StepsizeIncrementAndDecrement{
		this.class.classesForTesting.do({arg class;
			var testClass, testValue;
			var name = "my%".format(class.name);
			var param = class.new(name);
			var testStepsize, wasRun, wasCorrectValue;
			testClass = this.class.testclassForType( class.type );
			param.minVal = testClass.makeRandomValue((minVal: -100.0, maxVal: 0.0));
			param.maxVal = testClass.makeRandomValue((minVal: 1.0, maxVal: 200.0));
			param.clipmode = \both;

			//Stepsize should be positive numbers only
			//Convert into absolute value and print warning if negative value is used.
			testStepsize = testClass.makeRandomValue((minVal: -10.0, maxVal: -1.0));
			param.stepsize = testStepsize;
			this.assertEquals(
				param.stepsize, testStepsize.abs, "% converted negative stepsize to positive".format(testClass)
			);

			//Should increment according to stepsize
			param.clipmode_(\none);
			testValue = testClass.makeRandomValue((minVal: 50.0, maxVal: 100.0));
			testStepsize = testClass.makeRandomValue((minVal: 1.0, maxVal: 4.0));
			param.stepsize = testStepsize;
			param.value = testValue;
			{ param.increment; } ! 3; // increment 3 times
			this.assertEquals(
				param.value, testValue + (testStepsize * 3), "% value was incremented correctly with stepsize %".format(testClass, testStepsize)
			);

			//Should run action send new value to it
			wasRun = false;
			wasCorrectValue = false;
			testValue = testClass.makeRandomValue((minVal: 1.0, maxVal: 15.0));
			testStepsize = testClass.makeRandomValue((minVal: 1.0, maxVal: 5.0));
			param.clipmode = \none;
			param.action = {arg p;
				wasRun = true;
				wasCorrectValue = (testValue + testStepsize) == p.value; };
			param.value = testValue;
			param.stepsize = testStepsize;
			param.increment;
			this.assert(
				wasRun and: {wasCorrectValue}, "% ran action with correct value".format(testClass)
			);

			//Should decrement according to stepsize
			testValue = testClass.makeRandomValue((minVal: 20.0, maxVal: 30.0));
			testStepsize = testClass.makeRandomValue((minVal: 1.0, maxVal: 5.0));
			param.value = testValue;
			param.stepsize = testStepsize;
			{param.decrement} ! 2;
			this.assertEquals(
				param.value, testValue - (testStepsize * 2), "% value was decremented correctly".format(testClass)
			);

			//Should not increment outside range if clipped
			param.minVal = -100.0;
			param.maxVal = 100.0;
			testValue = 99.0;
			testStepsize = 3.0;
			param.value = testValue;
			param.stepsize = testStepsize;
			param.clipmode = \both;
			{ param.increment; } ! 5;
			this.assertEquals(
				param.value, param.maxVal, "% value was clipped when incremented more than maxVal".format(testClass)
			);

			//Should not decrement outside range if clipped
			testValue = -99.0;
			testStepsize = 10.0;
			param.minVal = -100.0;
			param.maxVal = 100.0;
			param.value = testValue;
			param.stepsize = testStepsize;
			param.clipmode = \both;
			{ param.decrement; } ! 5;
			this.assertEquals(
				param.value, param.minVal, "% value was clipped when decremented more than minVal".format(testClass)
			);

			//Should disable doAction optionally when incrementing
			wasRun = false;
			param.action = {arg p; wasRun = true};
			testValue = 0.0;
			testStepsize = 3.0;
			param.value = testValue;
			param.stepsize = testStepsize;
			param.increment(doAction: false);
			this.assert(
				param.value == (testValue + testStepsize) and: {wasRun.not},
				"% value was incremented with optional doAction false causing action not to be run".format(testClass)
			);

			//Should disable doAction optionally when decrementing
			wasRun = false;
			param.action = {arg p; wasRun = true};
			testValue = 0.0;
			testStepsize = 3.0;
			param.value = testValue;
			param.stepsize = testStepsize;
			param.decrement(doAction: false);
			this.assert(
				param.value == (testValue - testStepsize) and: {wasRun.not},
				"% value was decremented with optional doAction false causing action not to be run".format(testClass)
			)
		});
	}

	test_UpdateValueWhenMinValAndMaxValChange{
		this.class.classesForTesting.do({arg class;
			var testClass, testValue;
			var name = "my%".format(class.name);
			var param = class.new(name);
			var wasRun = false;
			testClass = this.class.testclassForType( class.type );
			param.minVal = testClass.makeRandomValue((minVal: -100.0, maxVal: 0.0));
			param.maxVal = testClass.makeRandomValue((minVal: 100.0, maxVal: 200.0));
			param.action = {|p| wasRun = true; };
			param.clipmode = \both;
			//set the value higher than defined maxVal
			param.value = testClass.makeRandomValue((minVal: 201.0, maxVal: 300.0));

			//Should update the value after maxVal set
			//Should not run the action when maxVal change
			param.maxVal = testClass.makeRandomValue((minVal: 50.0, maxVal: 90.0));
			this.assertEquals(
				param.value, param.maxVal,
				"% value adjusted value when maxVal changed".format(testClass)
			);
			this.assert(
				wasRun.not,
				"% - adjusting out-of-range value for maxVal did not run action".format(testClass)
			);

			//Should update the value after minVal set
			//Should not run the action when minVal change
			wasRun = false;
			param.minVal = testClass.makeRandomValue((minVal: -100.0, maxVal: 0.0));//0.0;
			param.maxVal = testClass.makeRandomValue((minVal: 100.0, maxVal: 200.0));//5.0;
			param.value = testClass.makeRandomValue((minVal: 1.0, maxVal: 20.0));//1.0;
			param.minVal = testClass.makeRandomValue((minVal: 21.0, maxVal: 50.0));//1.5;
			this.assertEquals(
				param.value, param.minVal,
				"% value adjusted value when minVal changed".format(testClass)
			);
			this.assert(
				wasRun.not,
				"% - adjusting out-of-range value for minVal did not run action".format(testClass)
			);
		});
	}

	test_AllowSettingMinValAndMaxValToNil{
		this.class.classesForTesting.do({arg class;
			var testClass, testValue;
			var name = "my%".format(class.name);
			var param = class.new(name);
			var wasRun = false;
			testClass = this.class.testclassForType( class.type );

			param.minVal = testClass.makeRandomValue((minVal: -100.0, maxVal: 0.0));
			param.maxVal = testClass.makeRandomValue((minVal: 1.0, maxVal: 100.0));

			//Should allow setting minVal to nil
			try{
				param.minVal = nil;
				this.assert(
					param.minVal.isNil,
					"% allowed setting minVal to nil".format(testClass)
				);
			} {
				this.failed(thisMethod,
					"% failed to allow setting minVal to nil".format(testClass)
				);
			};

			//Should allow setting maxVal to nil
			try{
				param.maxVal = nil;
				this.assert(
					param.maxVal.isNil,
					"% allowed setting maxVal to nil".format(testClass)
				);
			} {
				this.failed(thisMethod,
					"% failed to allow setting maxVal to nil".format(testClass)
				);
			};
		});
	}


	test_OnlyClipValuesWhenMinValAndMaxValAreDefined{
		this.class.classesForTesting.do({arg class;
			var testClass, testValue;
			var name = "my%".format(class.name);
			var param = class.new(name);
			var wasRun = false;
			testClass = this.class.testclassForType( class.type );

			//Should only check for lower value in clipmode 'both' if 'maxVal' is not defined
			param.clipmode = \both;
			testValue = 999999.0;
			param.value = testValue;
			param.maxVal = nil;
			param.minVal = 0.0;
			this.assert(
				param.maxVal.isNil and: {param.value == testValue},
				message: "% didn't clip value when maxVal was nil.".format(testClass)
			);
			testValue = -999999.0;
			param.value = testValue;
			this.assertEquals(
				param.value, param.minVal,
				"% clipped value to minVal in clipmode 'both' when maxVal was nil.".format(testClass)
			);

			//Should only check for higher value in clipmode 'both' if 'minVal' is not defined
			param.clipmode = \both;
			param.minVal = nil;
			param.maxVal = 1000.0;
			testValue = -999999.0;
			param.value = testValue;
			this.assert(
				param.minVal.isNil and: {param.value == testValue},
				message: "% didn't clip value when minVal was nil.".format(testClass)
			);
			testValue = 999999.0;
			param.value = testValue;
			this.assertEquals(
				param.value, param.maxVal,
				"% clipped value to maxVal in clipmode 'both' when minVal was nil.".format(testClass)
			);
		});
	}

	test_UpdateValueWhenClipmodeChange{
		this.class.classesForTesting.do({arg class;
			var testClass, testValue;
			var name = "my%".format(class.name);
			var param = class.new(name);
			var wasRun = false;
			testClass = this.class.testclassForType( class.type );
			param.minVal = -2.0;
			param.maxVal = 22.0;
			param.clipmode = \none;
			param.action = {|p| wasRun = true; };
			param.value = 25.0;

			//Should update the higher-than-maxVal-value when clipmode changes from 'none' to 'high'
			param.clipmode = \high;
			this.assertEquals(
				param.value, param.maxVal,
				"% - adjusted value when clipmode changed to 'high'".format(testClass)
			);
			//Should not run the action when clipmode changed
			this.assert(
				wasRun.not, "% didn't run action when adjusting the clipmode".format(testClass)
			);

			//Should update the lower-than-minVal-value when clipmode changes 'none' to 'low'
			param.clipmode = \none;
			param.value = -10.0;
			param.minVal = -5.5;
			param.minVal = 0.0;
			param.clipmode = \low;
			this.assertEquals(
				param.value, param.minVal,
				"% - adjusted value clipmode changed to 'low'".format(testClass)
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
				"% - did not adjust higher value when clipmode changed to 'low'".format(testClass)
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
				"% - did not adjust higher value when clipmode changed to 'low'".format(testClass)
			);


			//Should adjust value if value is higher than maxVal and clipmode set to 'both'
			param.clipmode = \none;
			param.minVal = 0.0;
			param.maxVal = 2.0;
			param.value = 2.222;
			param.clipmode = \both;
			this.assertEquals(
				param.value, param.maxVal,
				"% adjusted higher value when clipmode set to 'both'".format(testClass)
			);

			//Should adjust value if value is lower than minVal and clipmode is set to 'both'
			param.clipmode = \none;
			param.minVal = -111.0;
			param.maxVal = 222.0;
			param.value = -222.999;
			param.clipmode = \both;
			this.assertEquals(
				param.value, param.minVal,
				"% adjusted lower value when clipmode set to 'both'".format(testClass)
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
				"% kept higher value when clipmode went from 'low' 'none'".format(testClass)
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
				"% kept higher value when clipmode went from 'low' 'none'".format(testClass)
			);

		});
	}
}
