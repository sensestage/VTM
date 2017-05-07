TestVTMValue : VTMUnitTest {
	*testClasses{
		^[
			VTMBooleanValue,
			VTMStringValue,
			VTMDecimalValue,
			VTMIntegerValue,
			VTMTimecodeValue,
			VTMListValue,
			VTMDictionaryValue,
			VTMArrayValue,
			VTMSchemaValue,
			VTMTupleValue
		];
	}

	*testclassForType{arg val;
		^"TestVTM%Value".format(val.asString.capitalize).asSymbol.asClass;
	}

	*testTypes{
		^this.testClasses.collect(_.type);
	}

	*generateRandomAttributes{arg description;
		var result = VTMAttributes.new;
		if(description.notNil, {
			description.do({arg item;
				if(item.isKindOf(Symbol), {
					result.add(this.prConstructAttribute(item));
				});
				if(item.isKindOf(Association), {
					result.add(
						this.prConstructAttribute(item.key, item.value);
					);
				});
			});
		});
		^result;
	}

	*makeRandomValue{arg params;
		^this.subclassResponsibility(thisMethod);
	}

	*prConstructAttribute{arg key, data;
		var result;
		if(data.notNil, {
			if(data.isKindOf(Association) and: {data.key == \random}, {
				switch(data.key,
					//If it is a \random -> (minVal: 7) i.e. \random Assiciation
					//the class' random function will be used
					\random, {
						result = this.prMakeRandomAttribute(key, data.value);
					},
					//use the result of the function
					\function, {
						result = data.value;
					}
				);
			}, {
				//If it is defined otherwise, use that value
				result = data;
			});
		}, {
			//If it is nil we make a random attribute with
			//undefined Values.
			result = this.prMakeRandomAttribute(key);
		});
		^Association.new(key, result);
	}

	*prMakeRandomAttribute{arg key, params;
		var result;
		switch(key,
			\enabled, {result = this.makeRandomBoolean.value(params)},
			\filterRepetitions, { result = this.makeRandomBoolean(params); },
			\value, { result = this.makeRandomValue(params); },
			\defaultValue, { result = this.makeRandomValue(params); },
			\enum, { result = this.makeRandomEnum(params); },
			\restrictValueToEnum, { result = this.makeRandomBoolean(params); },
			{result = nil;}
		);
		^result;
	}

	*makeRandomEnum{arg params;
		var minRand = 5, maxRand = 10;
		^rrand(minRand, maxRand).collect({arg i;
			[
				[i, this.makeRandomSymbol((noNumbers: true, noSpaces: true))].choose,
				this.makeRandomValue
			];
		}).flatten;
	}


	*makeRandomAttributes{arg type;
		var testClass, class, attrKeys, result;
		class = "VTM%Value".format(type.asString.capitalize).asSymbol.asClass;
		testClass = "Test%".format(class.name).asSymbol.asClass;
		attrKeys = class.attributeKeys;
		result = testClass.generateRandomAttributes(attrKeys);
		^result;
	}

	setUp{
		"Setting up a VTMValueTest".postln;
	}

	tearDown{
		"Tearing down a VTMValueTest".postln;
	}

	test_SetAndDoActionWithParamAsArg{
		this.class.testClasses.do({arg testClass;
			var param = testClass.new();
			var wasRun = false;
			var gotParamAsArg = false;
			param.action = {arg param;
				wasRun = true;
				gotParamAsArg = param === param;
			};
			param.doAction;
			this.assert(
				wasRun and: {gotParamAsArg},
				"[%] - Value action was set, run and got passed itself as arg.".format(testClass)
			);
			param.free;
		});
	}

	test_SetGetRemoveEnableAndDisableAction{
		this.class.testClasses.do({arg testClass;
			var param = testClass.new();
			var wasRun, aValue;
			var anAction, anotherAction;
			anAction = {arg param; wasRun = true; };
			anotherAction = {arg param; wasRun = true; };

			//action should point to identical action as defined
			param.action = anAction;
			this.assert(
				anAction === param.action, "Value action point to correct action");

			//Remove action
			param.action = nil;
			this.assert(param.action.isNil, "Removed Value action succesfully");

			//should be enabled by default
			this.assert( param.enabled, "Value should be enabled by default" );

			//disable should set 'enabled' false
			param.disable;
			this.assert( param.enabled.not, "Value should be disabled by calling '.disable'" );

			//disable should prevent action from being run
			wasRun = false;
			param.action = anAction;
			param.doAction;
			this.assert(wasRun.not, "Value action was prevented to run by disable");

			//We should still be able to acces the action instance
			this.assert(
				param.action.notNil and: {param.action === anAction},
				"Wasn't able to access Value action while being disabled"
			);

			//enable should set 'enabled' true
			param.enable;
			this.assert(param.enabled, "Value was enabled again");

			//enable should allow action to be run
			wasRun = false;
			param.doAction;
			this.assert(wasRun, "Value enabled, reenabled action to run");

			//If another action is set when Value is disabled, the
			//other action should be returned and run when the Value is enabled
			//again
			anAction = {arg param; aValue = 111;};
			anotherAction = {arg param; aValue = 222;};
			param.action = anAction;
			param.disable;
			param.action = anotherAction;
			param.enable;
			param.doAction;
			this.assert(param.action === anotherAction and: { aValue == 222; },
				"Value action was changed correctly during disabled state"
			);

			//Action should be run upon enable if optionally defined in enable call
			wasRun = false;
			param.disable;
			param.action = {arg param; wasRun = true; };
			param.enable(doActionWhenEnabled: true);
			this.assert(wasRun,
				"Value action was optionally performed on enabled");
			param.free;
		});

	}

	test_SetVariablesThroughAttributes{
		this.class.testClasses.do({arg testClass;
			var param, aAttributes, anAction;
			var wasRun = false;
			anAction = {arg param; wasRun = true;};
			aAttributes = (
				enabled: false
			);
			param = testClass.new(aAttributes);
			param.action = anAction;

			//enabled is set through attributes
			this.assert(param.enabled.not,
				"Value was disabled through attributes"
			);

			//action is set through attributes
			param.enable; //Reenable Value
			param.doAction;
			this.assert(wasRun and: {param.action === anAction},
				"Value action was set through attributes"
			);
			param.free;
		});
	}

	test_GetAttributes{
		TestVTMValue.testClasses.do({arg class;
			var wasRun = false;
			var testClass = this.class.findTestClass(class);
			var attributes = testClass.makeRandomAttributes(class.type);
			var param = class.makeFromType(class.type, attributes);

			this.assertEquals(
				param.attributes, attributes,
				"% returned correct attributes.".format(class)
			);
			param.free;
		});
	}


	//previously Value PArameter test methods
	test_SetGetValue{
		TestVTMValue.testClasses.do({arg class;
			var testClass, testValue;
			var param = class.new();
			testClass = this.class.testclassForType( class.type );
			testValue = testClass.makeRandomValue;
			param.value = testValue;
			this.assertEquals(
				param.value, testValue, "% 'value' was set".format(testClass.name)
			);
			param.free;
		});
	}

	test_SetGetDefaultValue{
		TestVTMValue.testClasses.do({arg class;
			var testClass, testValue;
			var param;
			try{
				testClass = this.class.testclassForType( class.type );
				testValue = testClass.makeRandomValue;
				param = class.new((defaultValue: testValue));
				this.assertEquals(
					param.defaultValue, testValue, "Value defaultValue was set [%]".format(testClass.name)
				);
				this.assertEquals(
					param.value, testValue,
					"Value value was set to defined defaultValue when value was not defined [%]".format(testClass.name)
				);
				param.free;
			} {|err|
				this.failed(
					thisMethod,
					"Unknown test fail for %\n\t%".format(class, err.errorString)
				);
			};
		});
	}

	test_ResetSetValueToDefault{
		TestVTMValue.testClasses.do({arg class;
			var testClass, testValue, wasRun;
			var param;
			testClass = this.class.testclassForType( class.type );
			testValue = testClass.makeRandomValue;
			param = class.new;
			param.value = testClass.makeRandomValue;
			param.defaultValue = testClass.makeRandomValue;
			param.reset;
			this.assertEquals(
				param.value, param.defaultValue,
				"[%] - Value value was set to defaultValue upon reset".format(class)
			);
			wasRun = false;
			param.action_({arg p; wasRun = true;});
			param.value = testClass.makeRandomValue;
			param.defaultValue = testClass.makeRandomValue;
			param.reset(doActionUponReset: true);
			this.assert(
				wasRun,
				"[%] - Value action was run upon reset when defined to do so".format(class)
			);
			param.free;
		});
	}

	test_DefaultValueShouldNotBeNil{
		TestVTMValue.testClasses.do({arg class;
			var testClass, testValue, wasRun;
			var param;
			testClass = this.class.testclassForType( class.type );
			param = class.new();
			this.assert(
				param.defaultValue.notNil,
				"Value did not initialize defaultValue to nil [%]".format(
					class.name
				)
			);
			param.free;
		});
	}

	test_Typechecking{
		var wrongValuesForType = (
			integer: \hei,
			decimal: \hei,
			string: 432,
			boolean: 1,
			array: \bingo,
			list: \halo,
			dictionary: -0.93,
			function: 123,
			schema: \hei,
			tuple: \halo
		);
		TestVTMValue.testClasses.do({arg class;
			var testClass, testValue, wasRun;
			var param;
			testClass = this.class.testclassForType( class.type );
			param = class.new();
			testValue = wrongValuesForType[class.type];
			try{
				this.assert(
					param.isValidType(testValue).not,
					"Value value input of wrong type failed validation check corretly [%]".format(class.name)
				);
			} {|err|
				this.failed(
					thisMethod,
					"Value input value validation failed by unknown error. [%]\n\t%".format(class.name, err.errorString)
				);
			};
		});
	}

	test_AccessValueInAction{
		TestVTMValue.testClasses.do({arg class;
			try{
				var testClass, testValue, wasRun;
				var param, gotValue = false, gotParamPassed = false;
				testClass = this.class.testclassForType( class.type );
				param = class.new();
				testValue = testClass.makeRandomValue;
				param.value = testValue;
				param.action = {arg p;
					gotParamPassed = p === param;
					gotValue = p.value == testValue;
				};
				param.doAction;
				this.assert(gotParamPassed,
					"ValueValue got param passed in action [%]".format(class.name));
				this.assert(gotValue,
					"ValueValue got value in action [%]".format(class.name));
				param.free;
			} {|err|
				this.failed(
					thisMethod,
					"Value test failed unknown error [%]\n\t%".format(
						class.name,
						err.errorString
					)
				);
			};
		});
	}
	test_ValueAction{
		TestVTMValue.testClasses.do({arg class;
			var testClass, testValue;
			var param, wasRun, gotUpdatedValue;
			wasRun = false;
			gotUpdatedValue = false;
			testClass = this.class.testclassForType( class.type );
			testValue = testClass.makeRandomValue;
			param = class.new();
			param.action = {arg p;
				wasRun = true;
				gotUpdatedValue = p.value == testValue;
			};
			param.value = testClass.makeRandomValue;
			param.valueAction_(testValue);
			this.assert(
				wasRun and: {gotUpdatedValue},
				"ValueValue valueAction set correct value and passed it into action"
			);
			param.free;
		});
	}
	test_FilterRepeatingValues{
		TestVTMValue.testClasses.do({arg class;
			var testClass, testValue;
			var param;
			var wasRun = false;
			testClass = this.class.testclassForType( class.type );
			testValue = testClass.makeRandomValue;
			param = class.new();
			param.filterRepetitions = true;
			param.action = {arg p;
				wasRun = true;
			};
			//Action should not be run when input value are equal to current value
			param.value = testValue;
			param.valueAction_(testValue);
			this.assert(
				wasRun.not, "ValueValue action was prevented to run since values where equal"
			);
		});
	}

	test_SetVariablesFromAttributes{
		TestVTMValue.testClasses.do({arg class;
			var testClass, testValue;
			var param;
			var testAttributes;
			testClass = this.class.testclassForType( class.type );
			testAttributes = testClass.generateRandomAttributes(
				[
					\value,
					\defaultValue,
					\filterRepetitions
				]
			);

			param = VTMValue.makeFromType(class.type, testAttributes);

			[\value, \defaultValue, \filterRepetitions].do({arg item;
				this.assertEquals(
					param.perform(item), testAttributes[item],
					"Value set % through attributes [%]".format(item, class.name)
				);
			});
			param.free;
		});
	}

	test_Enum{
		TestVTMValue.testClasses.do({arg class;
			var testClass, testValue;
			var name = "my%".format(class.name);
			var param;
			var testEnum;
			var testAttributes;
			testClass = this.class.testclassForType( class.type );
			testAttributes = testClass.generateRandomAttributes(
				[
					\value,
					\defaultValue,
					\filterRepetitions,
					\enabled -> true,
					\enum
				]
			);
			testEnum = testAttributes.at(\enum);
			param = VTMValue.makeFromType(class.type, testAttributes);
			this.assertEquals(
				param.enum, testEnum,
				"ValueValue set and returned correct enum[%]".format(class.name)
			);
			param.free;
		});
	}

	// test_GetAttributes{
	// 	TestVTMValueValue.testClasses.do({arg class;
	// 		var testClass, testValue;
	// 		var name = "my%".format(class.name);
	// 		var param;
	// 		var testAttributes, testAttributes;
	// 		testClass = this.class.testclassForType( class.type );
	// 		testAttributes = testClass.generateRandomAttributes(
	// 			[
	// 				\value,
	// 				\defaultValue,
	// 				\path,
	// 				\action -> {arg p; 1 + 1 },
	// 				\filterRepetitions,
	// 				\name,
	// 				\type -> class.type,
	// 				\enabled -> true,
	// 				\enum
	// 			]
	// 		);
	// 		param = VTMValue.makeFromAttributes(testAttributes);
	// 		testAttributes = testAttributes.deepCopy;
	// 		testAttributes.put(\action, testAttributes[\action].asCompileString);
	// 		this.assert(
	// 			testAttributes.keys.sect(param.attributes.keys) == testAttributes.keys,
	// 			"ValueValue returned correct attribute keys for ValueValue level [%]".format(class.name)
	// 		);
	// 		//			this.assertEquals(
	// 		//				testAttributes.sect(param.attributes),
	// 		//			   	testAttributes,
	// 		//			   	"ValueValue returned correct attribute values for ValueValue level [%]".format(class.name)
	// 		//			);
	// 		param.free;
	// 	});
	// }

}
