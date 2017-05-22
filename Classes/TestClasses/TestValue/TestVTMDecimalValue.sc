TestVTMDecimalValue : TestVTMNumberValue {

	*makeRandomValue{arg params;
		^this.makeRandomDecimal(params);
	}

	setUp{
		"Setting up a VTMDecimalValueTest".postln;
	}

	tearDown{
		"Tearing down a VTMDecimalValueTest".postln;
	}

	test_ConvertIntegerNumbersToFloat{
		var testValue;
		var desc = (
			defaultValue: 111, value: 215,
			clipmode: \both, minVal: -231, maxVal: 889,
			stepsize: 88
		);
		var valueObj = VTMDecimalValue.new(desc);
		this.assertEquals(
			valueObj.value.class, Float,
			"DecimalValue changed integer value in properties to Float"
		);
		this.assertEquals(
			valueObj.defaultValue.class, Float,
			"DecimalValue changed integer defaultValue in properties to Float"
		);
		this.assertEquals(
			valueObj.minVal.class, Float,
			"DecimalValue changed integer minVal in properties to Float"
		);
		this.assertEquals(
			valueObj.maxVal.class, Float,
			"DecimalValue changed integer maxVal in properties to Float"
		);
		this.assertEquals(
			valueObj.stepsize.class, Float,
			"DecimalValue changed integer stepsize in properties to Float"
		);

		//Test the setter methods also
		testValue = 22;
		valueObj.value = testValue;
		this.assertEquals(
			valueObj.value.class, Float,
			"DecimalValue changed decimal value in setter to Float"
		);

		testValue = 0;
		valueObj.defaultValue = testValue;
		this.assertEquals(
			valueObj.defaultValue.class, Float,
			"DecimalValue changed decimal defaultValue in setter to Float"
		);

		testValue = -22;
		valueObj.minVal = testValue;
		this.assertEquals(
			valueObj.minVal.class, Float,
			"DecimalValue changed decimal minVal in setter to Float"
		);

		testValue = 1232;
		valueObj.maxVal = testValue;
		this.assertEquals(
			valueObj.maxVal.class, Float,
			"DecimalValue changed decimal maxVal in setter to Float"
		);

		testValue = 122;
		valueObj.stepsize = testValue;
		this.assertEquals(
			valueObj.stepsize.class, Float,
			"DecimalValue changed decimal stepsize in setter to Float"
		);
	}
}
