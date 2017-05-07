TestVTMBooleanValue : TestVTMValue {

	*makeRandomValue{arg params;
		^this.makeRandomBoolean(params);
	}

	test_DefaultAttributes{
		var param = VTMBooleanValue.new('myBoolean');

		this.assertEquals(
			param.value, false,
			"BooleanValue value false by default"
		);

		this.assertEquals(
			param.defaultValue, false,
			"BooleanValue defaultValue false by default"
		);
	}

	test_SetAttributesThroughAttributes{
		var desc = (
			value: true,
			defaultValue: false
		);
		var param = VTMBooleanValue.new('myBoolean', desc);
		this.assert(
			param.value == desc[\value] and: {param.defaultValue == desc[\defaultValue]},
			"BooleanValue set correct values from attributes, test A"
		);

		//test the inversion
		desc = (
			value: false,
			defaultValue: true
		);
		param = VTMBooleanValue.new('myBoolean', desc);
		this.assert(
			param.value == desc[\value] and: {param.defaultValue == desc[\defaultValue]},
			"BooleanValue set correct values from attributes, test B"
		);
	}

	test_ToggleInvertsValue{
		var param = VTMBooleanValue.new('myBoolean');
		param.value = true;
		//toogle should make value false
		param.toggle;
		this.assertEquals(
			param.value, false,
			"BooleanValue toggling value true makes it false."
		);

		//Should toggle back to true
		param.toggle;
		this.assertEquals(
			param.value, true,
			"BooleanValue toggling value back to true."
		);

	}

	test_TypecheckingValueSetting{
		var desc = ( typecheck: true, value: true );
		var param = VTMBooleanValue.new('myBoolean', desc);
		var testVal = 11;

		//Should detect values of of wrong type
		if(param.isValidType(testVal).not, {
			this.passed(thisMethod, "Detected wrong value type: %".format(testVal));
		}, {
			this.failed(thisMethod, "Did not detect wrong value type: %".format(testVal));
		});

	}
}
