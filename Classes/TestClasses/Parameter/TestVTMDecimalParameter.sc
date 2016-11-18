TestVTMDecimalParameter : TestVTMNumberParameter {

	*makeRandomValue{arg params;
		^this.makeRandomDecimal(params);
	}

	setUp{
		"Setting up a VTMDecimalParameterTest".postln;
	}

	tearDown{
		"Tearing down a VTMDecimalParameterTest".postln;
	}

	test_ConvertIntegerNumbersToFloat{
		var testValue;
		var desc = (
			defaultValue: 111, value: 215,
			clipmode: \both, minVal: -231, maxVal: 889,
			stepsize: 88
		);
		var param = VTMDecimalParameter.new('myDecimal', desc);
		this.assertEquals(
			param.value.class, Float,
			"DecimalParameter changed integer value in declaration to Float"
		);
		this.assertEquals(
			param.defaultValue.class, Float,
			"DecimalParameter changed integer defaultValue in declaration to Float"
		);
		this.assertEquals(
			param.minVal.class, Float,
			"DecimalParameter changed integer minVal in declaration to Float"
		);
		this.assertEquals(
			param.maxVal.class, Float,
			"DecimalParameter changed integer maxVal in declaration to Float"
		);
		this.assertEquals(
			param.stepsize.class, Float,
			"DecimalParameter changed integer stepsize in declaration to Float"
		);

		//Test the setter methods also
		testValue = 22;
		param.value = testValue;
		this.assertEquals(
			param.value.class, Float,
			"DecimalParameter changed decimal value in setter to Float"
		);

		testValue = 0;
		param.defaultValue = testValue;
		this.assertEquals(
			param.defaultValue.class, Float,
			"DecimalParameter changed decimal defaultValue in setter to Float"
		);

		testValue = -22;
		param.minVal = testValue;
		this.assertEquals(
			param.minVal.class, Float,
			"DecimalParameter changed decimal minVal in setter to Float"
		);

		testValue = 1232;
		param.maxVal = testValue;
		this.assertEquals(
			param.maxVal.class, Float,
			"DecimalParameter changed decimal maxVal in setter to Float"
		);

		testValue = 122;
		param.stepsize = testValue;
		this.assertEquals(
			param.stepsize.class, Float,
			"DecimalParameter changed decimal stepsize in setter to Float"
		);
	}
}
