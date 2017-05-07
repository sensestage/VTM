TestVTMValue : VTMUnitTest {
	*testClasses{
		^[
			VTMBooleanValue,
			VTMStringValue,
			// VTMListValue,
			// VTMDictionaryValue,
			// VTMArrayValue,
			//	VTMTimecodeValue,
			VTMDecimalValue,
			VTMIntegerValue
			// VTMSchemaValue,
			// VTMTupleValue
		];
	}

	*testclassForType{arg val;
		^"TestVTM%Value".format(val.asString.capitalize).asSymbol.asClass;
	}

	*testTypes{
		^this.testClasses.collect(_.type);
	}

	*generateRandomAttributes{arg description;
		var result = IdentityDictionary.new;
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
			\name, {result = this.makeRandomSymbol(params ? (noNumbers: true)); },
			\path, {
				var minLevels, maxLevels;
				if(params.notNil and: { params.isKindOf(Dictionary) },{
					minLevels = params[\minLevels] ? 1;
					maxLevels = params[\maxLevels] ? 3;
				}, {
					minLevels = 1;
					maxLevels = 3;
				});
				result = rrand(minLevels,maxLevels).collect({
					"/%".format(this.makeRandomString.value(params));
				});
				result = String.newFrom(result.flat).asSymbol;
			},
			\enabled, {result = this.makeRandomBoolean.value(params)},
			\willStore, {result = this.makeRandomBoolean.value(params)},
			\onlyReturn, {result = this.makeRandomBoolean.value(params)},
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
				[i + 1, this.makeRandomSymbol((noNumbers: true, noSpaces: true))].choose,
				this.makeRandomValue
			];
		}).flatten;
	}


	*makeRandomAttributes{arg type;
		var testClass, class, attrKeys, result;
		class = "VTM%Value".format(type.asString.capitalize).asSymbol.asClass;
		testClass = "Test%".format(class.name).asSymbol.asClass;
		attrKeys = class.attributeKeys;
		attrKeys.add(\type -> type);
		result = testClass.generateRandomAttributes(attrKeys);
		^result;
	}

	setUp{
		"Setting up a VTMValueTest".postln;
	}

	tearDown{
		"Tearing down a VTMValueTest".postln;
	}

	test_ShouldErrorIfNameNotDefined{
		this.class.testClasses.do({arg testClass;
			try{
				var param;
				param = testClass.new();
				this.failed(
					thisMethod,
					"Value should fail if name not defined [%]".format(testClass)
				);
				param.free;
			} {|err|
				this.passed(thisMethod,
					"Value failed correctly when name was not defined. [%]".format(testClass)
				)
			}
		});
	}

	test_SettingName{
		this.class.testClasses.do({arg testClass;
			try{
				var param, name = "my%".format(testClass.name).asSymbol;
				param = testClass.new(name);
				this.assertEquals(
					param.name, name,
					"Value returned 'name' correctly[%]".format(testClass)
				);
				param.free;
			} {|err|
				this.failed(thisMethod,
					"Value test failed due to unknown error. [%]\n\t%".format(
						testClass, err.errorString);
				)
			};
		});
	}

	test_ReturnFullPathAsPrefixedNameByDefault{
		this.class.testClasses.do({arg testClass;
			var param, name = "my%".format(testClass.name).asSymbol;
			param = testClass.new(name);
			this.assertEquals(
				param.fullPath, "/%".format(name).asSymbol,
				"Value returned 'fullPath' with 'name' prefixed with slash [%]".format(testClass)
			);
			param.free;
		});
	}

	test_ReturnPathAsAsNilIfNotSet{
		this.class.testClasses.do({arg testClass;
			var param, name = "my%".format(testClass.name).asSymbol;
			param = testClass.new(name);
			this.assertEquals(
				param.path, nil,
				"Value returned 'path' as nil. [%]".format(testClass)
			);
			param.free;
		});
	}

	test_RemoveLeadingSlashInNameIfDefined{
		this.class.testClasses.do({arg testClass;
			var param, name = "my%".format(testClass.name).asSymbol;
			param = testClass.new("/%".format(name).asSymbol);
			this.assertEquals(
				param.name, name,
				"Value: Uneccesary leading slash in name removed. [%]".format(testClass)
			);
			param.free;
		});
	}

	test_SetGetPathAndFullPath{
		this.class.testClasses.do({arg testClass;
			var name = "my%".format(testClass.name).asSymbol;
			var param = testClass.new(name);
			var aPath = '/myPath';
			param.path = aPath;
			this.assertEquals(
				param.path, '/myPath',
				"Value return correct path [%]".format(testClass.name)
			);
			this.assertEquals(
				param.fullPath, "/myPath/%".format(name).asSymbol,
				"Value return correct fullPath [%]".format(testClass)
			);
			param.free;
		});
	}

	test_AddLeadingSlashToPathIfNotDefined{
		this.class.testClasses.do({arg testClass;
			var name = "my%".format(testClass.name).asSymbol;
			var param = testClass.new(name);
			param.path = 'myPath';
			this.assertEquals(
				param.path, '/myPath',
				"Value leading path slash was added. [%]".format(testClass)
			);
			this.assertEquals(
				param.fullPath, "/myPath/%".format(name).asSymbol,
				"Value leading path slash was added. [%]".format(testClass)
			);
			param.free;
		});
	}

	test_SetAndDoActionWithParamAsArg{
		this.class.testClasses.do({arg testClass;
			var name = "my%".format(testClass.name).asSymbol;
			var param = testClass.new(name);
			var wasRun = false;
			var gotParamAsArg = false;
			param.action = {arg param;
				wasRun = true;
				gotParamAsArg = param === param;
			};
			param.doAction;
			this.assert(
				wasRun and: {gotParamAsArg},
				"Value action was set, run and got passed itself as arg. [%]".format(testClass.name)
			);
			param.free;
		});
	}

	test_SetGetRemoveEnableAndDisableAction{
		this.class.testClasses.do({arg testClass;
			var name = "my%".format(testClass.name).asSymbol;
			var param = testClass.new(name);
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
			var name = "my%".format(testClass.name).asSymbol;
			var param, aAttributes, anAction;
			var wasRun = false;
			anAction = {arg param; wasRun = true;};
			aAttributes = (
				action: anAction,
				path: '/myPath',
				enabled: false,
			);
			param = testClass.new(name, aAttributes);

			//path is set through attributes
			this.assertEquals(param.path, '/myPath',
				"Path was defined through attributes"
			);
			this.assertEquals(param.fullPath, "/myPath/%".format(name).asSymbol,
				"Full path was defined through attributes"
			);
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
	//
	//	test_ValueFree{
	//		//Should free responders, internal Values, mappings, and oscInterface
	//	}
	//
	test_GetAttributes{
		//Only testing for attributes relevant to VTMValue class
		topEnvironment.put('paramTest', ());
		TestVTMValue.testClasses.do({arg testClass;
			var wasRun = false;
			var attributes = this.class.makeRandomAttributes(testClass.type);
			var param = testClass.makeFromAttributes(attributes);
			topEnvironment['paramTest'].put(param.name,
				(attributes: param.attributes.deepCopy, attributes: attributes.deepCopy)
			);

			this.assertEquals(
				param.attributes, attributes,
				"% returned correct attributes.\nA:\t%\nB:\t%".format(
					testClass,
					param.attributes.keys.asArray.sort,
					attributes.keys.asArray.sort
				)
			);
			// topEnvironment.put(\response, param.attributes);
			// topEnvironment.put(\attributes, attributes);

			/*
			"Attributes:".postln;
			param.attributes.keysValuesDo({arg key, val;
			"\t:% - [%]%".format(key, val.class, val).postln;
			});
			"Attributes".postln;
			attributes.keysValuesDo({arg key, val;
			"\t:% - [%]%".format(key, val.class, val).postln;
			});
			*/
			//If the action is not a closed function it should not get returned as attribute
			param.action = {|p| wasRun = true;};
			this.assert(
				param.attributes[\action].isNil,
				"% returned open function as nil. [%]".format(testClass)
			);
			param.free;
		});
	}


	//previously Value PArameter test methods
	test_SetGetValue{
		TestVTMValue.testClasses.do({arg class;
			var testClass, testValue;
			var name = "my%".format(class.name);
			var param = class.new(name);
			testClass = this.class.testclassForType( class.type );
			testValue = testClass.makeRandomValue;
			param.value = testValue;
			this.assertEquals(
				param.value, testValue, "Value value was set [%]".format(testClass.name)
			);
			param.free;
		});
	}

	test_SetGetDefaultValue{
		TestVTMValue.testClasses.do({arg class;
			var testClass, testValue;
			var name = "my%".format(class.name);
			var param;
			try{
				testClass = this.class.testclassForType( class.type );
				testValue = testClass.makeRandomValue;
				param = class.new(name, attributes: (defaultValue: testValue));
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
			var name = "my%".format(class.name);
			var param;
			testClass = this.class.testclassForType( class.type );
			testValue = testClass.makeRandomValue;
			param = class.new(name);
			param.value = testClass.makeRandomValue;
			param.defaultValue = testClass.makeRandomValue;
			param.reset;
			this.assertEquals(
				param.value, param.defaultValue,
				"Value value was set to defaultValue upon reset[%]".format(testClass.name)
			);
			wasRun = false;
			param.action_({arg p; wasRun = true;});
			param.value = testClass.makeRandomValue;
			param.defaultValue = testClass.makeRandomValue;
			param.reset(doActionUponReset: true);
			this.assert(
				wasRun,
				"Value action was run upon reset when defined to do so [%]".format(testClass.name)
			);
			param.free;
		});
	}

	test_DefaultValueShouldNotBeNil{
		TestVTMValue.testClasses.do({arg class;
			var testClass, testValue, wasRun;
			var name = "my%".format(class.name);
			var param;
			testClass = this.class.testclassForType( class.type );
			param = class.new(name);
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
			var name = "my%".format(class.name);
			var param;
			testClass = this.class.testclassForType( class.type );
			param = class.new(name);
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
				var name = "my%".format(class.name);
				var param, gotValue = false, gotParamPassed = false;
				testClass = this.class.testclassForType( class.type );
				param = class.new(name);
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
			var name = "my%".format(class.name);
			var param, wasRun, gotUpdatedValue;
			wasRun = false;
			gotUpdatedValue = false;
			testClass = this.class.testclassForType( class.type );
			testValue = testClass.makeRandomValue;
			param = class.new(name);
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
			var name = "my%".format(class.name);
			var param;
			var wasRun = false;
			testClass = this.class.testclassForType( class.type );
			testValue = testClass.makeRandomValue;
			param = class.new(name);
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
			var testAttributes, wasRun = false;
			testClass = this.class.testclassForType( class.type );
			testAttributes = testClass.generateRandomAttributes(
				[
					\value,
					\defaultValue,
					\path,
					\action -> {arg p; wasRun = true; },
					\filterRepetitions,
					\name,
					\type -> class.type
				]
			);

			param = VTMValue.makeFromAttributes(testAttributes);

			//Change string values to symbols for testing object return values
			testAttributes[\name] = testAttributes[\name].asSymbol;
			testAttributes[\path] = testAttributes[\path].asSymbol;

			[\value, \defaultValue, \path, \name, \filterRepetitions].do({arg item;
				// "CHECKING %: \n\t%[%]\n\t%[%]".format(
				// 	item,
				// 	param.perform(item), param.perform(item).class,
				// 	testAttributes[item], testAttributes[item].class
				// ).postln;
				this.assertEquals(
					param.perform(item), testAttributes[item],
					"Value set % through attributes [%]".format(item, class.name)
				);
			});
			param.doAction;
			this.assert(
				wasRun,
				"Value action was set through attributes [%]".format(class.name)
			);
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
					\path,
					\action -> {arg p; 1 + 1 },
					\filterRepetitions,
					\name,
					\type -> class.type,
					\enabled -> true,
					\enum
				]
			);
			testEnum = testAttributes.at(\enum);
			param = VTMValue.makeFromAttributes(testAttributes);
			testAttributes.put(\action, testAttributes[\action].asCompileString);
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
