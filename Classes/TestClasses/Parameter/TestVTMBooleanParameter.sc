TestVTMBooleanParameter : TestVTMValueParameter {

	getRandom{arg val, randomParameters, obj;
		var result;
		switch(val,
			\value, { result = 0.5.coin; },
			{
				result = super.getRandom(val, randomParameters, obj);
			}
		);
		^result;
	}

	setUp{
		"Setting up a VTMDecimalParameterTest".postln;
	}

	tearDown{
		"Tearing down a VTMDecimalParameterTest".postln;
	}

	test_DefaultAttributes{
		var param = VTMBooleanParameter.new('myBoolean');

		this.assertEquals(
			param.value, false,
			"BooleanParameter value false by default"
		);

		this.assertEquals(
			param.defaultValue, false,
			"BooleanParameter defaultValue false by default"
		);
	}

	test_SetAttributesThroughDescription{
		var desc = (
			value: true,
			defaultValue: false
		);
		var param = VTMBooleanParameter.new('myBoolean', desc);
		this.assert(
			param.value == desc[\value] and: {param.defaultValue == desc[\defaultValue]},
			"BooleanParameter set correct values from description, test A"
		);

		//test the inversion
		desc = (
			value: false,
			defaultValue: true
		);
		param = VTMBooleanParameter.new('myBoolean', desc);
		this.assert(
			param.value == desc[\value] and: {param.defaultValue == desc[\defaultValue]},
			"BooleanParameter set correct values from description, test B"
		);
	}

	test_ToggleInvertsValue{
		var param = VTMBooleanParameter.new('myBoolean');
		param.value = true;
		//toogle should make value false
		param.toggle;
		this.assertEquals(
			param.value, false,
			"BooleanParameter toggling value true makes it false."
		);

		//Should toggle back to true
		param.toggle;
		this.assertEquals(
			param.value, true,
			"BooleanParameter toggling value back to true."
		);

	}

	test_TypecheckingValueSetting{
		var desc = ( typecheck: true, value: true );
		var param = VTMBooleanParameter.new('myBoolean', desc);

		//Should not accept other value types
		param.value = 11;
		this.assertEquals(
			param.value, desc[\value],
			"BooleanParameter ignored integer value"
		);

	}
}