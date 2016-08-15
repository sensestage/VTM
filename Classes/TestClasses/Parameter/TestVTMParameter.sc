TestVTMParameter : VTMUnitTest {
	classvar <testClasses;
	*initClass{
		testClasses = [
			VTMBooleanParameter,
			VTMStringParameter,
			VTMListParameter,
			VTMDictionaryParameter,
			VTMArrayParameter,
			VTMFunctionParameter,
			VTMTimecodeParameter,
			VTMDecimalParameter,
			VTMIntegerParameter,
			VTMSchemaParameter,
			VTMTupleParameter
		];
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

	*prConstructAttribute{arg key, data;
		var result;
		if(data.notNil, {
			//if it is a function it will return whatever
			if(data.isKindOf(Function), {
				result = data.value;
			}, {
				//If it is a \random -> (minVal: 7) i.e. \random Assiciation
				//the class random function will be used
				if(data.isKindOf(Association) and: {data.key == \random}, {
					result = this.prMakeRandomAttribute(key, data.value);
				}, {
					//If it is defined otherwise, use that value
					result = data;
				});
			});
		}, {
			//If it is nil we make a random attribute with
			//undefined parameters.
			result = this.prMakeRandomAttribute(key);
		});
		^Association.new(key, result);
	}

	*prMakeRandomAttribute{arg key, params;
		var result;
		switch(key,
			\name, {result = this.makeRandomString.value(params)},
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
				result = String.newFrom(result.flat)
			},
			\enabled, {result = this.makeRandomBoolean.value(params)},
			\willStore, {result = this.makeRandomBoolean.value(params)},
			\onlyReturn, {result = this.makeRandomBoolean.value(params)},
			{result = nil;}
		);
		^result;
	}

	setUp{
		"Setting up a VTMParameterTest".postln;
	}

	tearDown{
		"Tearing down a VTMParameterTest".postln;
	}

	test_ShouldErrorIfNameNotDefined{
		this.class.testClasses.do({arg testClass;
			try{
				var param;
				param = testClass.new();
				this.failed(
					thisMethod,
					"Parameter should fail if name not defined [%]".format(testClass)
				);
				param.free;
			} {|err|
				this.passed(thisMethod,
					"Parameter failed correctly when name was not defined. [%]".format(testClass)
				)
			}
		});
	}

	test_SettingName{
		this.class.testClasses.do({arg testClass;
			var param, name = "my%".format(testClass.name).asSymbol;
			param = testClass.new(name);
			this.assertEquals(
				param.name, name,
				"Parameter returned 'name' correctly[%]".format(testClass)
			);
			param.free;
		});
	}

	test_ReturnFullPathAsPrefixedNameByDefault{
		this.class.testClasses.do({arg testClass;
			var param, name = "my%".format(testClass.name).asSymbol;
			param = testClass.new(name);
			this.assertEquals(
				param.fullPath, "/%".format(name).asSymbol,
				"Parameter returned 'fullPath' with 'name' prefixed with slash [%]".format(testClass)
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
				"Parameter returned 'path' as nil. [%]".format(testClass)
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
				"Parameter: Uneccesary leading slash in name removed. [%]".format(testClass)
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
				"Parameter return correct path [%]".format(testClass.name)
			);
			this.assertEquals(
				param.fullPath, "/myPath/%".format(name).asSymbol,
				"Parameter return correct fullPath [%]".format(testClass)
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
				"Parameter leading path slash was added. [%]".format(testClass)
			);
			this.assertEquals(
				param.fullPath, "/myPath/%".format(name).asSymbol,
				"Parameter leading path slash was added. [%]".format(testClass)
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
				"Parameter action was set, run and got passed itself as arg. [%]".format(testClass.name)
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
				anAction === param.action, "Parameter action point to correct action");

			//Remove action
			param.action = nil;
			this.assert(param.action.isNil, "Removed Parameter action succesfully");

			//should be enabled by default
			this.assert( param.enabled, "Parameter should be enabled by default" );

			//disable should set 'enabled' false
			param.disable;
			this.assert( param.enabled.not, "Parameter should be disabled by calling '.disable'" );

			//disable should prevent action from being run
			wasRun = false;
			param.action = anAction;
			param.doAction;
			this.assert(wasRun.not, "Parameter action was prevented to run by disable");

			//We should still be able to acces the action instance
			this.assert(
				param.action.notNil and: {param.action === anAction},
				"Wasn't able to access parameter action while being disabled"
			);

			//enable should set 'enabled' true
			param.enable;
			this.assert(param.enabled, "Parameter was enabled again");

			//enable should allow action to be run
			wasRun = false;
			param.doAction;
			this.assert(wasRun, "Parameter enabled, reenabled action to run");

			//If another action is set when parameter is disabled, the
			//other action should be returned and run when the parameter is enabled
			//again
			anAction = {arg param; aValue = 111;};
			anotherAction = {arg param; aValue = 222;};
			param.action = anAction;
			param.disable;
			param.action = anotherAction;
			param.enable;
			param.doAction;
			this.assert(param.action === anotherAction and: { aValue == 222; },
				"Parameter action was changed correctly during disabled state"
			);

			//Action should be run upon enable if optionally defined in enable call
			wasRun = false;
			param.disable;
			param.action = {arg param; wasRun = true; };
			param.enable(doActionWhenEnabled: true);
			this.assert(wasRun,
				"Parameter action was optionally performed on enabled");
			param.free;
		});

	}

	test_SetVariablesThroughDeclaration{
		this.class.testClasses.do({arg testClass;
			var name = "my%".format(testClass.name).asSymbol;
			var param, aDeclaration, anAction;
			var wasRun = false;
			anAction = {arg param; wasRun = true;};
			aDeclaration = (
				action: anAction,
				path: '/myPath',
				enabled: false,
			);
			param = testClass.new(name, aDeclaration);

			//path is set through declaration
			this.assertEquals(param.path, '/myPath',
				"Path was defined through declaration"
			);
			this.assertEquals(param.fullPath, "/myPath/%".format(name).asSymbol,
				"Full path was defined through declaration"
			);
			//enabled is set through declaration
			this.assert(param.enabled.not,
				"Parameter was disabled through declaration"
			);

			//action is set through declaration
			param.enable; //Reenable parameter
			param.doAction;
			this.assert(wasRun and: {param.action === anAction},
				"Parameter action was set through declaration"
			);
			param.free;
		});
	}
//
//	test_ParameterFree{
//		//Should free responders, internal parameters, mappings, and oscInterface
//	}
//
	test_GetAttributes{
		//Only testing for attributes relevant to VTMParameter class
		this.class.testClasses.do({arg testClass;
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
			param.free;
		});

	}
}
