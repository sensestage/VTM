TestVTMValueParameter : TestVTMParameter {

	*generateRandomAttributes{arg description;
		var result = super.generateRandomAttributes(description);
		^result;
	}

	*makeRandomValue{arg params;
		^this.subclassResponsibility(thisMethod);
	}
	
	*prMakeRandomAttribute{arg key, params;
		var result;
		result = super.prMakeRandomAttribute(key, params);
		if(result.isNil, {
			switch(key,
				\filterRepetitions, { result = this.makeRandomBoolean(params); },
				\value, { result = this.makeRandomValue(params); },
				\defaultValue, { result = this.makeRandomValue(params); }
			);
		});
		^result;
	}

	setUp{
		"Setting up a VTMValueParameterTest".postln;
	}

	tearDown{
		"Tearing down a VTMValueParameterTest".postln;
	}

	test_SetGetValue{
		this.class.testClasses.do({arg class;
			var testClass, testValue;
			var name = "my%".format(class.name);
			var param = class.new(name);
			testClass = VTMUnitTest.testclassForType( class.type );
			testValue = testClass.makeRandomValue;
			param.value = testValue;
			this.assertEquals(
				param.value, testValue, "Parameter value was set [%]".format(testClass.name)
			);
			param.free;
		});
	}

	test_SetGetDefaultValue{
		this.class.testClasses.do({arg class;
			var testClass, testValue;
			var name = "my%".format(class.name);
			var param;
			try{
			testClass = VTMUnitTest.testclassForType( class.type );
			testValue = testClass.makeRandomValue;
			param = class.new(name, declaration: (defaultValue: testValue));
			this.assertEquals(
				param.defaultValue, testValue, "Parameter defaultValue was set [%]".format(testClass.name)
			);
			this.assertEquals(
				param.value, testValue,
				"Parameter value was set to defined defaultValue when value was not defined [%]".format(testClass.name)
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
		this.class.testClasses.do({arg class;
			var testClass, testValue, wasRun;
			var name = "my%".format(class.name);
			var param;
			testClass = VTMUnitTest.testclassForType( class.type );
			testValue = testClass.makeRandomValue;
			param = class.new(name);
			param.value = testClass.makeRandomValue;
			param.defaultValue = testClass.makeRandomValue;
			param.reset;
			this.assertEquals(
				param.value, param.defaultValue,
				"Parameter value was set to defaultValue upon reset[%]".format(testClass.name)
			);
			wasRun = false;
			param.action_({arg p; wasRun = true;});
			param.value = testClass.makeRandomValue;
			param.defaultValue = testClass.makeRandomValue;
			param.reset(doActionUponReset: true);
			this.assert(
				wasRun,
				"Parameter action was run upon reset when defined to do so [%]".format(testClass.name)
			);
			param.free;
		});
	}

	test_DefaultValueShouldNotBeNil{
		this.class.testClasses.do({arg class;
			var testClass, testValue, wasRun;
			var name = "my%".format(class.name);
			var param;
			testClass = VTMUnitTest.testclassForType( class.type );
			param = class.new(name);
			this.assert(
				param.defaultValue.notNil,
				"Parameter did not initialize defaultValue to nil [%]".format(
					class.name
				)
			);
			param.free;
		});
	}
//
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
		this.class.testClasses.do({arg class;
			var testClass, testValue, wasRun;
			var name = "my%".format(class.name);
			var param;
			testClass = VTMUnitTest.testclassForType( class.type );
			param = class.new(name);
			testValue = wrongValuesForType[class.type];
			try{
				this.assert(
					param.isValidType(testValue).not,
					"Parameter value input of wrong type failed validation check corretly [%]".format(class.name)
				);
			} {|err|
				this.failed(
					thisMethod,
					"Parameter input value validation failed by unknown error. [%]\n\t%".format(class.name, err.errorString)
				);
			};
		});
	}

	test_AccessValueInAction{
		this.class.testClasses.do({arg class;
			try{
				var testClass, testValue, wasRun;
				var name = "my%".format(class.name);
				var param, gotValue = false, gotParamPassed = false;
				testClass = VTMUnitTest.testclassForType( class.type );
				param = class.new(name);
				testValue = testClass.makeRandomValue;
				param.value = testValue;
				param.action = {arg p;
					gotParamPassed = p === param;
					gotValue = p.value == testValue;
				};
				param.doAction;
				this.assert(gotParamPassed,
				   	"ValueParameter got param passed in action [%]".format(class.name));
				this.assert(gotValue,
				   	"ValueParameter got value in action [%]".format(class.name));
				param.free;
			} {|err|
				this.failed(
					thisMethod,
					"Parameter test failed unknown error [%]\n\t%".format(
						class.name,
						err.errorString
					)
				);
			};
		});
	}

	test_ValueAction{
		this.class.testClasses.do({arg class;
			var testClass, testValue;
			var name = "my%".format(class.name);
			var param, wasRun, gotUpdatedValue;
			wasRun = false;
			gotUpdatedValue = false;
			testClass = VTMUnitTest.testclassForType( class.type );
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
				"ValueParameter valueAction set correct value and passed it into action"
			);
			param.free;
		});
	}

	test_FilterRepeatingValues{
		this.class.testClasses.do({arg class;
			var testClass, testValue;
			var name = "my%".format(class.name);
			var param;
			var wasRun = false;
			testClass = VTMUnitTest.testclassForType( class.type );
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
				wasRun.not, "ValueParameter action was prevented to run since values where equal"
			);
		});

	}

//	test_SetVariablesFromDeclaration{
//		var param, declaration, wasRun = false;
//		declaration = (
//			path: '/myPath', defaultValue: 222, value: 333,
//			action: {arg p; wasRun = true},
//			filterRepetitions: true,
//			typecheck: false
//		);
//		param = VTMValueParameter.new('myName', declaration);
//		this.assert(param.path.notNil,
//			"ValueParameter path is notNil");
//		this.assertEquals(
//			param.path, '/myPath', "ValueParameter path was set through declaration"
//		);
//
//		this.assert(param.fullPath.notNil,
//			"ValueParameter fullPath is notNil");
//		this.assertEquals(
//			param.fullPath, '/myPath/myName', "ValueParameter fullPath was set through declaration"
//		);
//
//		this.assert(param.defaultValue.notNil,
//			"ValueParameter defaultValue is notNil");
//		this.assertEquals(
//			param.defaultValue, 222, "ValueParameter defaultValue was set through declaration"
//		);
//
//		this.assert(param.value.notNil,
//			"ValueParameter value is notNil");
//		this.assertEquals(
//			param.value, 333, "ValueParameter value was set through declaration"
//		);
//
//		param.doAction;
//		this.assert(wasRun, "ValueParameter action was set through declaration");
//
//		this.assert(param.filterRepetitions,
//			"ValueParameter filterRepetitions was set through declaration");
//	}
//
//
//	test_GetAttributes{
//		var declaration = IdentityDictionary[
//			\path -> '/myValuePath/tester',
//			\action -> {|p| p.value - 12.3; },
//			\enabled -> true,
//			\defaultValue -> -0.2,
//			\value -> 9.9,
//			\typecheck -> false,
//			\filterRepetitions -> true
//		];
//		var testAttributes;
//		var param = VTMValueParameter.new('myValue', declaration);
//		testAttributes = declaration.deepCopy.put(\name, 'myValue');
//		testAttributes.put(\action, testAttributes[\action].asCompileString);
//		this.assertEquals(
//			param.attributes, testAttributes, "ValueParameter returned correct attributes"
//		);
//	}

}
