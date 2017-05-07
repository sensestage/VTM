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
		var param = VTMDecimalValue.new('myDecimal', desc);
		this.assertEquals(
			param.value.class, Float,
			"DecimalValue changed integer value in attributes to Float"
		);
		this.assertEquals(
			param.defaultValue.class, Float,
			"DecimalValue changed integer defaultValue in attributes to Float"
		);
		this.assertEquals(
			param.minVal.class, Float,
			"DecimalValue changed integer minVal in attributes to Float"
		);
		this.assertEquals(
			param.maxVal.class, Float,
			"DecimalValue changed integer maxVal in attributes to Float"
		);
		this.assertEquals(
			param.stepsize.class, Float,
			"DecimalValue changed integer stepsize in attributes to Float"
		);

		//Test the setter methods also
		testValue = 22;
		param.value = testValue;
		this.assertEquals(
			param.value.class, Float,
			"DecimalValue changed decimal value in setter to Float"
		);

		testValue = 0;
		param.defaultValue = testValue;
		this.assertEquals(
			param.defaultValue.class, Float,
			"DecimalValue changed decimal defaultValue in setter to Float"
		);

		testValue = -22;
		param.minVal = testValue;
		this.assertEquals(
			param.minVal.class, Float,
			"DecimalValue changed decimal minVal in setter to Float"
		);

		testValue = 1232;
		param.maxVal = testValue;
		this.assertEquals(
			param.maxVal.class, Float,
			"DecimalValue changed decimal maxVal in setter to Float"
		);

		testValue = 122;
		param.stepsize = testValue;
		this.assertEquals(
			param.stepsize.class, Float,
			"DecimalValue changed decimal stepsize in setter to Float"
		);
	}
}
