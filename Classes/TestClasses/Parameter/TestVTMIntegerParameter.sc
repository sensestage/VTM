TestVTMIntegerParameter : TestVTMNumberParameter {

	*makeRandomValue{arg params;
		^this.makeRandomInteger(params);
	}

	setUp{
		"Setting up a VTMIntegerParameterTest".postln;
	}

	tearDown{
		"Tearing down a VTMIntegerParameterTest".postln;
	}

	test_ConvertDecimalNumbersToInteger{
		var testValue;
		var desc = (
			defaultValue: 11.1, value: 21.5,
			clipmode: \both, minVal: -23.1, maxVal: 88.9,
			stepsize: 8.8
		);
		var param = VTMIntegerParameter.new('myInteger', desc);
		this.assertEquals(
			param.value, desc[\value].asInteger,
			"IntegerParameter changed decimal value in declaration to Integer"
		);
		this.assertEquals(
			param.defaultValue, desc[\defaultValue].asInteger,
			"IntegerParameter changed decimal defaultValue in declaration to Integer"
		);
		this.assertEquals(
			param.minVal, desc[\minVal].asInteger,
			"IntegerParameter changed decimal minVal in declaration to Integer"
		);
		this.assertEquals(
			param.maxVal, desc[\maxVal].asInteger,
			"IntegerParameter changed decimal maxVal in declaration to Integer"
		);
		this.assertEquals(
			param.stepsize, desc[\stepsize].asInteger,
			"IntegerParameter changed decimal stepsize in declaration to Integer"
		);

		//Test the setter methods also
		testValue = 22.02;
		param.value = testValue;
		this.assertEquals(
			param.value, testValue.asInteger,
			"IntegerParameter changed decimal value in setter to Integer"
		);

		testValue = 0.01;
		param.defaultValue = testValue;
		this.assertEquals(
			param.defaultValue, testValue.asInteger,
			"IntegerParameter changed decimal defaultValue in setter to Integer"
		);

		testValue = -22.0144;
		param.minVal = testValue;
		this.assertEquals(
			param.minVal, testValue.asInteger,
			"IntegerParameter changed decimal minVal in setter to Integer"
		);

		testValue = 1232.9191;
		param.maxVal = testValue;
		this.assertEquals(
			param.maxVal, testValue.asInteger,
			"IntegerParameter changed decimal maxVal in setter to Integer"
		);

		testValue = 12.2;
		param.stepsize = testValue;
		this.assertEquals(
			param.stepsize, testValue.asInteger,
			"IntegerParameter changed decimal stepsize in setter to Integer"
		);
	}
}
