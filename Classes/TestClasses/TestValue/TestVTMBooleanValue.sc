TestVTMBooleanValue : TestVTMValue {

	*makeRandomValue{arg params;
		^this.makeRandomBoolean(params);
	}

	test_ToggleInvertsValue{
		var attr = VTMBooleanValue.new();
		attr.value = true;
		//toogle should make value false
		attr.toggle;
		this.assertEquals(
			attr.value, false,
			"BooleanValue toggling value true makes it false."
		);

		//Should toggle back to true
		attr.toggle;
		this.assertEquals(
			attr.value, true,
			"BooleanValue toggling value back to true."
		);

	}
}
