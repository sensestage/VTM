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
		});
	}
//
//	test_PreventDefaultValueAsNil{
//		//not sure yet if this should be tested in this class
//	}
//
//	test_SetGetTypecheckOnAndOff{
//		var wasRun;
//		var param = VTMValueParameter.new('myName');
//		//Typecheck should be set. This won't have any efect on
//		//ValueParameter class, as it will accept any type.
//		param.typecheck = false;
//		this.assert(param.typecheck.not, "ValueParameter typecheck was turned off");
//		param.typecheck = true;
//		this.assert(param.typecheck, "ValueParameter typecheck was turned on");
//	}
//
//	test_Typechecking{
//		var param = VTMValueParameter.new('myName');
//
//		//Should be on by default
//		this.assert(
//			param.typecheck, "ValueParameter type is on by default");
//
//		//Should accept any type
//		param.value = 11;
//		this.assertEquals(
//			param.value, 11, "ValueParameter accepted Integer"
//		);
//
//		param.value = 9.9;
//		this.assertFloatEquals(
//			param.value, 9.9, "ValueParameter accepted Float"
//		);
//
//		param.value = \tester;
//		this.assertEquals(
//			param.value, \tester, "ValueParameter accepted Symbol"
//		);
//
//		param.value = "another tester";
//		this.assertEquals(
//			param.value,
//			"another tester",
//			"ValueParameter accepted String"
//		);
//
//		param.value = ["array", 'of', "1 2 3", 11, [34, 2], 0.3, -1.2];
//		this.assertEquals(
//			param.value,
//			["array", 'of', "1 2 3", 11, [34, 2], 0.3, -1.2],
//			"ValueParameter array of various types"
//		);
//
//		param.value = (hei: 33, hallo: "334", bing: \hehe);
//		this.assertEquals(
//			param.value,
//			(hei: 33, hallo: "334", bing: \hehe),
//			"ValueParameter dictionary type"
//		);
//
//		//ValueParameter can even be set to nil..
//		param.value = nil;
//		this.assertEquals(
//			param.value,
//			nil,
//			"ValueParameter dictionary type"
//		);
//	}
//
//	test_AccessValueInAction{
//		var param = VTMValueParameter.new('myName');
//		var val = 33, gotValue = false;
//		param.action = {arg p;
//			gotValue = p === param and: {p.value == val};
//		};
//		param.value_(val);
//		param.doAction;
//		this.assert(gotValue, "ValueParameter got value in action");
//	}
//
//	test_ValueAction{
//		var param = VTMValueParameter.new('myName');
//		var wasRun, testValue, gotUpdatedValue;
//		//should run action when setting valueAction
//		testValue = 222;
//		wasRun = false;
//		gotUpdatedValue = false;
//		param.action = {arg p;
//			wasRun = true;
//			gotUpdatedValue = p.value == testValue;
//		};
//		param.value = 111;
//		param.valueAction_(testValue);
//		this.assert(
//			wasRun and: {gotUpdatedValue},
//			"ValueParameter valueAction set correct value and passed it into action"
//		);
//
//	}
//
//	test_FilterRepeatingValues{
//		var param = VTMValueParameter.new('myName');
//		var wasRun = false;
//		var aValue = "a test string", anotherValue = "a test string";
//		var anAction = {arg p;
//			wasRun = true;
//		};
//		param.filterRepetitions = true;
//
//		//Action should not be run when input value are equal to current value
//		param.value = aValue;
//		param.valueAction_(anotherValue);
//		this.assert(
//			wasRun.not, "ValueParameter action was prevented to run since values where equal"
//		);
//
//		//Filter repetitions is based on equality, not idenity, so value will be set to new
//		//object instance, but the action won't be run.
//		//It is only the action we want to prevent from happening.
//		//This test only works when using object of types that are separate object instances,
//		//such as Array, Strings, Dictionaries etc.
//		//This test proves false if input is primitive objects such as Integers, Float, Symbol,
//		//in which the actual object isn't actually changed.
//		//This might have to do with some kind of behind-the-scenes optimization(?)
//		this.assert(
//			(param.value == aValue)
//			&& (param.value !== aValue)
//			&& (param.value === anotherValue),
//			"ValueParameter value is still equal to the first value, but a different instance";
//		);
//
//	}
//
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
//	test_InitValueToDefaultValueIfNoValueDefined{
//		var param, declaration;
//		declaration = (
//			path: '/myPath', defaultValue: 9999
//		);
//		param = VTMValueParameter.new('myName', declaration);
//		//check if value is initialized to defaultValue
//		this.assertEquals(
//			param.value, 9999, "ValueParameter value was initialized to defaultValue"
//		);
//
//	}
//	test_TypeWasInferredFromValueTypeInDeclaration{}
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
