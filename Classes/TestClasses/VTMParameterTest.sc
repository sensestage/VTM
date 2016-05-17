TestVTMParameter : UnitTest {
	setUp{
		"Setting up a VTMParameterTest".postln;
	}

	tearDown{
		"Tearing down a VTMParameterTest".postln;
	}

	test_ShouldErrorIfNameNotDefined{
		try{
			var aParameter;
			aParameter = VTMParameter.new();
			this.failed(
				thisMethod,
				"Parameter should fail if name not defined"
			);
		} {|err|
			this.passed(thisMethod,
				"Parameter failed correctly when name was not defined"
			)
		}
	}

	test_ReturnFullPathAsPrefixedNameByDefault{
		var aParameter;
		aParameter = VTMParameter.new(\myParam);
		this.assertEquals(
			aParameter.fullPath, '/myParam',
			"Parameter returned 'fullPath' with 'name' prefixed with slash"
		);
	}

	test_ReturnPathAsAsNilIfNotSet{
		var aParameter;
		aParameter = VTMParameter.new(\myParam);
		this.assertEquals(
			aParameter.path, nil,
			"Parameter returned 'path' as nil"
		);
	}

	test_RemoveLeadingSlashInNameIfDefined{
		var aParam = VTMParameter.new('/myName');
		this.assertEquals(
			aParam.name, 'myName',
			"Parameter: Uneccesary leading slash in name removed"
		);
	}

	test_SetGetPathAndFullPath{
		var aParameter = VTMParameter.new('myName');
		var aPath = '/myPath';
		aParameter.path = aPath;
		this.assertEquals(
			aParameter.path, '/myPath',
			"Parameter return correct path"
		);
		this.assertEquals(
			aParameter.fullPath, '/myPath/myName',
			"Parameter return correct fullPath"
		);
	}

	test_AddLeadingSlashToPathIfNotDefined{
		var aParameter = VTMParameter.new('myName');
		aParameter.path = 'myPath';
		this.assertEquals(
			aParameter.path, '/myPath',
			"Parameter leading path slash was added"
		);
		this.assertEquals(
			aParameter.fullPath, '/myPath/myName',
			"Parameter leading path slash was added"
		);
	}

	test_SetAndDoActionWithParamAsArg{
		var aParameter = VTMParameter.new('myName');
		var wasRun = false;
		var gotParamAsArg;
		aParameter.action = {arg param;
			wasRun = true;
			gotParamAsArg = param === aParameter;
		};
		aParameter.doAction;
		this.assert(
			wasRun and: {gotParamAsArg},
			"Parameter action was set, run and got passed itself as arg"
		)
	}

	test_SetGetRemoveEnableAndDisableAction{
		var aParam = VTMParameter.new('myName');
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
	}

	test_SetVariablesThroughDescription{
		var aParam, aDescription, anAction;
		var wasRun = false;
		anAction = {arg param; wasRun = true;};
		aDescription = (
			action: anAction,
			path: '/myPath',
			enabled: false,
		);
		aParam = VTMParameter.new('myName', aDescription);

		//path is set through description
		this.assertEquals(aParam.path, '/myPath',
			"Path was defined through description"
		);
		this.assertEquals(aParam.fullPath, '/myPath/myName',
			"Full path was defined through description"
		);
		//enabled is set through description
		this.assert(aParam.enabled.not,
			"Parameter was disabled through description"
		);

		//action is set through description
		aParam.enable; //Reenable parameter
		aParam.doAction;
		this.assert(wasRun and: {aParam.action === anAction},
			"Parameter action was set through description"
		);
	}

	test_ParameterFree{
		//Should remove action, path, name and description upon free
	}

	test_GetAttributes{
		//Should return an IdentityDictionary with the keys: name, path, action, enabled
	}
}