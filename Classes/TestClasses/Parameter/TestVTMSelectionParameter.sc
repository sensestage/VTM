TestVTMSelectionParameter : VTMUnitTest {

	getRandom{arg val, desc, obj;
		var result;
		switch(val,
			\value, {
				if(obj.isNil, {
					Error("TestVTMSelectionParameter:getRandom needs an object argument to make random selection").throw;
				}, {
					result = obj.options.scramble.copyRange(0, rrand(0, obj.size));
				});
			},
			\defaultValue, { result = this.getRandom(\value, desc, obj); },
			{ result = super.getRandom(val, desc, obj); }
		);
		^result;
	}

	setUp{
		"Setting up a VTMSelectionParameterTest".postln;
	}

	tearDown{
		"Tearing down a VTMSelectionParameterTest".postln;
	}

	test_DefaultAttributes{
		var testValue, testOptions;
		var param = VTMSelectionParameter.new('mySelection');
		//value should be be nil
		this.assertEquals(
			param.value, [],
			"SelectionParameter value is empty array by default"
		);

		//defaultValue should be array
		this.assertEquals(
			param.defaultValue, [],
			"SelectionParameter defaultValue is empty array by default"
		);

		//options should be empty array
		this.assertEquals(
			param.options, [],
			"SelectionParameter options is empty array by default"
		);
	}

	test_SetAttributesThroughDeclaration{
		var desc = (
			options: [\aaa, \bbb, \ccc, \ddd],
			defaultValue: [\ddd, \ccc],
			value: [\ccc]
		);
		var param = VTMSelectionParameter.new('mySelection', desc);
		this.assertEquals(
			param.value, desc[\value],
			"SelectionParameter set value through declaration"
		);
		this.assertEquals(
			param.defaultValue, desc[\defaultValue],
			"SelectionParameter set defaultValue through declaration"
		);
		this.assertEquals(
			param.options, desc[\options],
			"SelectionParameter set options through declaration"
		);
	}

	test_DefaultValueAndValueWhenOptionsChange{

	}

	test_IgnoreNonMatchingSelectionValues{
		var testValue;
		var desc = (
			options: [\aa, \bb, \cc, \dd]
		);
		var param = VTMSelectionParameter.new('mySelection', desc);

		testValue = [\xx, \yy];
		param.value = testValue;
		this.assertEquals(
			param.value, [],
			"SelectionParameter ignored value where no items matched the parameter options"
		);

		testValue = [\bb, \tt];
		param.value = testValue;
		this.assertEquals(
			param.value, [],
			"SelectionParameter ignored value where some items didn't match the parameter options"
		);
	}

	test_ClearingOptionsUpdatesValue{
		var testOptions, testValue, testDefaultValue;
		var param = VTMSelectionParameter.new('mySelection', (options: [\aa, \bb, \cc, \dd]));
		param.value = [\dd, \bb];
		param.options = [];
		this.assertEquals(
			param.value, [],
			"SelectionParameter value becomes empty string when options are set to empty array."
		);
		this.assertEquals(
			param.defaultValue, [],
			"SelectionParameter defaultValue becomes empty string when options are set to empty array."
		);
	}

	test_IncrementalSelection{
		var param = VTMSelectionParameter.new('mySelection', (
			options: [11,22,33,44], value: [33]
		));

		param.addToSelection(11);
		param.addToSelection(44);
		this.assertEquals(
			param.value, [33,11,44],
			"SelectionParameter added items to selection correctly"
		);

		param.removeFromSelection(33);
		param.removeFromSelection(11);
		this.assertEquals(
			param.value, [44],
			"SelectionParameter incrementally removed items from selection correctly."
		);
	}

	test_ClearSelection{
		var param = VTMSelectionParameter.new('mySelection', (options: [11,22,33], value: [22,33]));
		param.clear;
		this.assertEquals(
			param.value, [],
			"SelectionParameter cleared selection upon correctly"
		);
	}
}
