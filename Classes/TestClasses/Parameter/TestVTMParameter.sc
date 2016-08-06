TestVTMParameter : VTMUnitTest {
	var testClasses;

	getRandom{arg val, desc, obj;
		var result;
		var generateRandomString = {
			var str;
			var charSet = (0..127).collect(_.asAscii).select(_.isAlphaNum);
			str = { charSet.choose } ! rrand(2, 16);
			String.newFrom(str.scramble);
		};
		switch(val,
			\name, { result = generateRandomString.value; },
			\path, {
				var pt = {"/" ++ generateRandomString.value} ! rrand(1, 3);
				result = String.newFrom(pt.flat);
			},
			\enabled, {result = 0.5.coin; }
		);
		^result;
	}

	makeParameter{arg declaration, randomAttributes;
		var result, desc = declaration.deepCopy;
		if(randomAttributes.notNil, {
			randomAttributes.do({arg item;
				var val;
				val = this.getRandom(item);
				"Generating random for: %".format(item).postln;
				desc.put(item, val);
				"GOT : %".format(val).postln;
			});
		});
		// desc.put(\type, VTMParameter.classToType(this.class.name.asString.findRegexp("^Test(.+)$")[1][1]).interpret);
		"this class: %\n\t_got obj class: %\n\t_ resolveds to: %".format(
			this.class.name,
			this.class.name.asString.findRegexp("^Test(.+)$")[1][1],
			VTMParameter.typeToClass(this.class.name.asString.findRegexp("^Test(.+)$")[1][1])
		).postln;
		"DESC before make: %".format(desc).postln;
		result = VTMParameter.makeFromDeclaration(desc);
		^result;
	}

	setUp{
		"Setting up a VTMParameterTest".postln;
		testClasses = [
			VTMBooleanParameter,
			VTMTimecodeParameter,
			VTMDictionaryParameter,
			VTMStringParameter,
			VTMSchemaParameter,
			VTMFunctionParameter,
			VTMListParameter,
			VTMArrayParameter,
			VTMTupleParameter,
			VTMIntegerParameter,
			VTMDecimalParameter,
		];
	}

	tearDown{
		"Tearing down a VTMParameterTest".postln;
	}

	test_ShouldErrorIfNameNotDefined{
		testClasses.do({arg testClass;
			try{
				var aParameter;
				aParameter = testClass.new();
				this.failed(
					thisMethod,
					"Parameter should fail if name not defined [%]".format(testClass)
				);
			} {|err|
				this.passed(thisMethod,
					"Parameter failed correctly when name was not defined. [%]".format(testClass)
				)
			}

		});
	}

	test_ReturnFullPathAsPrefixedNameByDefault{
		testClasses.do({arg testClass;
			var aParameter, name = "my%".format(testClass.name).asSymbol;
			aParameter = testClass.new(name);
			this.assertEquals(
				aParameter.fullPath, "/%".format(name).asSymbol,
				"Parameter returned 'fullPath' with 'name' prefixed with slash [%]".format(testClass)
			);
		});
	}

	test_ReturnPathAsAsNilIfNotSet{
		testClasses.do({arg testClass;
			var aParameter, name = "my%".format(testClass.name).asSymbol;
			aParameter = testClass.new(name);
			this.assertEquals(
				aParameter.path, nil,
				"Parameter returned 'path' as nil. [%]".format(testClass)
			);

		});
	}

	test_RemoveLeadingSlashInNameIfDefined{
		testClasses.do({arg testClass;
			var aParameter, name = "my%".format(testClass.name).asSymbol;
			aParameter = testClass.new("/%".format(name).asSymbol);
			this.assertEquals(
				aParameter.name, name,
				"Parameter: Uneccesary leading slash in name removed. [%]".format(testClass)
			);
		});
	}

	test_SetGetPathAndFullPath{
		testClasses.do({arg testClass;
			var name = "my%".format(testClass.name).asSymbol;
			var aParameter = testClass.new(name);
			var aPath = '/myPath';
			aParameter.path = aPath;
			this.assertEquals(
				aParameter.path, '/myPath',
				"Parameter return correct path [%]".format(testClass.name)
			);
			this.assertEquals(
				aParameter.fullPath, "/myPath/%".format(name).asSymbol,
				"Parameter return correct fullPath [%]".format(testClass)
			);

		});
	}

	test_AddLeadingSlashToPathIfNotDefined{
		testClasses.do({arg testClass;
			var name = "my%".format(testClass.name).asSymbol;
			var aParameter = testClass.new(name);
			aParameter.path = 'myPath';
			this.assertEquals(
				aParameter.path, '/myPath',
				"Parameter leading path slash was added. [%]".format(testClass)
			);
			this.assertEquals(
				aParameter.fullPath, "/myPath/%".format(name).asSymbol,
				"Parameter leading path slash was added. [%]".format(testClass)
			);
		});
	}

	test_SetAndDoActionWithParamAsArg{
		testClasses.do({arg testClass;
			var name = "my%".format(testClass.name).asSymbol;
			var aParameter = testClass.new(name);
			var wasRun = false;
			var gotParamAsArg;
			aParameter.action = {arg param;
				wasRun = true;
				gotParamAsArg = param === aParameter;
			};
			aParameter.doAction;
			this.assert(
				wasRun and: {gotParamAsArg},
				"Parameter action was set, run and got passed itself as arg. [%]".format(testClass.name)
			);
		});
	}

	test_SetGetRemoveEnableAndDisableAction{
		testClasses.do({arg testClass;
			var name = "my%".format(testClass.name).asSymbol;
			var aParam = testClass.new(name);
			var wasRun, aValue;
			var anAction, anotherAction;
			anAction = {arg param; wasRun = true; };
			anotherAction = {arg param; wasRun = true; };

			//action should point to identical action as defined
			aParam.action = anAction;
			this.assert(
				anAction === aParam.action, "Parameter action point to correct action");

			//Remove action
			aParam.action = nil;
			this.assert(aParam.action.isNil, "Removed Parameter action succesfully");

			//should be enabled by default
			this.assert( aParam.enabled, "Parameter should be enabled by default" );

			//disable should set 'enabled' false
			aParam.disable;
			this.assert( aParam.enabled.not, "Parameter should be disabled by calling '.disable'" );

			//disable should prevent action from being run
			wasRun = false;
			aParam.action = anAction;
			aParam.doAction;
			this.assert(wasRun.not, "Parameter action was prevented to run by disable");

			//We should still be able to acces the action instance
			this.assert(
				aParam.action.notNil and: {aParam.action === anAction},
				"Wasn't able to access parameter action while being disabled"
			);

			//enable should set 'enabled' true
			aParam.enable;
			this.assert(aParam.enabled, "Parameter was enabled again");

			//enable should allow action to be run
			wasRun = false;
			aParam.doAction;
			this.assert(wasRun, "Parameter enabled, reenabled action to run");

			//If another action is set when parameter is disabled, the
			//other action should be returned and run when the parameter is enabled
			//again
			anAction = {arg param; aValue = 111;};
			anotherAction = {arg param; aValue = 222;};
			aParam.action = anAction;
			aParam.disable;
			aParam.action = anotherAction;
			aParam.enable;
			aParam.doAction;
			this.assert(aParam.action === anotherAction and: { aValue == 222; },
				"Parameter action was changed correctly during disabled state"
			);

			//Action should be run upon enable if optionally defined in enable call
			wasRun = false;
			aParam.disable;
			aParam.action = {arg param; wasRun = true; };
			aParam.enable(doActionWhenEnabled: true);
			this.assert(wasRun,
				"Parameter action was optionally performed on enabled");
		});

	}

	test_SetVariablesThroughDeclaration{
		testClasses.do({arg testClass;
			var name = "my%".format(testClass.name).asSymbol;
			var aParam, aDeclaration, anAction;
			var wasRun = false;
			anAction = {arg param; wasRun = true;};
			aDeclaration = (
				action: anAction,
				path: '/myPath',
				enabled: false,
			);
			aParam = testClass.new(name, aDeclaration);

			//path is set through declaration
			this.assertEquals(aParam.path, '/myPath',
				"Path was defined through declaration"
			);
			this.assertEquals(aParam.fullPath, "/myPath/%".format(name).asSymbol,
				"Full path was defined through declaration"
			);
			//enabled is set through declaration
			this.assert(aParam.enabled.not,
				"Parameter was disabled through declaration"
			);

			//action is set through declaration
			aParam.enable; //Reenable parameter
			aParam.doAction;
			this.assert(wasRun and: {aParam.action === anAction},
				"Parameter action was set through declaration"
			);
		});
	}

	test_ParameterFree{
		//Should free responders, internal parameters, mappings, and oscInterface
	}

	test_GetAttributes{
		//Only testing for attributes relevant to VTMParameter class
		testClasses.do({arg testClass;
			var testName = "my%".format(testClass.name).asSymbol;
			var wasRun = false;
			var declaration = IdentityDictionary[
				\path -> '/myPath/is',
				\action -> {|p| 11 + 8; },
				\enabled -> true
			];
			var testAttributes;
			var param = testClass.new(testName, declaration);
			// Should return an IdentityDictionary with the keys: name, path, action, enabled
			testAttributes = declaration.deepCopy.put(
				\name, testName
			);
			testAttributes.put(\action, declaration[\action].asCompileString);

			this.assert(
				param.attributes.keys.sect(Set[\path, \action, \enabled, \name]).notEmpty,
				"Parameter returned correct attributes. [%]".format(testClass.name)
			);

			//If the action is not a closed function it should not get returned as attribute
			param.action = {|p| wasRun = true;};
			this.assert(
				param.attributes[\action].isNil,
				"Parameter returned open function as nil. [%]".format(testClass.name)
			);
		});

	}
}
