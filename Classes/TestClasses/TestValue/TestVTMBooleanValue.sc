TestVTMBooleanValue : TestVTMValue {

	*makeRandomValue{arg params;
		^this.makeRandomBoolean(params);
	}

	test_ToggleInvertsValue{
		var param = VTMBooleanValue.new();
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
}
