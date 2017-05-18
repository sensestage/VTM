TestVTMIntegerValue : TestVTMNumberValue {

	*makeRandomValue{arg params;
		^this.makeRandomInteger(params);
	}

	setUp{
		"Setting up a VTMIntegerValueTest".postln;
	}

	tearDown{
		"Tearing down a VTMIntegerValueTest".postln;
	}

	test_ConvertDecimalNumbersToInteger{
		var testValue;
		var desc = (
			defaultValue: 11.1, value: 21.5,
			clipmode: \both, minVal: -23.1, maxVal: 88.9,
			stepsize: 8.8
		);
		var param = VTMIntegerValue.new(desc);
		this.assertEquals(
			param.value, desc[\value].asInteger,
			"IntegerValue changed decimal value in properties to Integer"
		);
		this.assertEquals(
			param.defaultValue, desc[\defaultValue].asInteger,
			"IntegerValue changed decimal defaultValue in properties to Integer"
		);
		this.assertEquals(
			param.minVal, desc[\minVal].asInteger,
			"IntegerValue changed decimal minVal in properties to Integer"
		);
		this.assertEquals(
			param.maxVal, desc[\maxVal].asInteger,
			"IntegerValue changed decimal maxVal in properties to Integer"
		);
		this.assertEquals(
			param.stepsize, desc[\stepsize].asInteger,
			"IntegerValue changed decimal stepsize in properties to Integer"
		);

		//Test the setter methods also
		testValue = 22.02;
		param.value = testValue;
		this.assertEquals(
			param.value, testValue.asInteger,
			"IntegerValue changed decimal value in setter to Integer"
		);

		testValue = 0.01;
		param.defaultValue = testValue;
		this.assertEquals(
			param.defaultValue, testValue.asInteger,
			"IntegerValue changed decimal defaultValue in setter to Integer"
		);

		testValue = -22.0144;
		param.minVal = testValue;
		this.assertEquals(
			param.minVal, testValue.asInteger,
			"IntegerValue changed decimal minVal in setter to Integer"
		);

		testValue = 1232.9191;
		param.maxVal = testValue;
		this.assertEquals(
			param.maxVal, testValue.asInteger,
			"IntegerValue changed decimal maxVal in setter to Integer"
		);

		testValue = 12.2;
		param.stepsize = testValue;
		this.assertEquals(
			param.stepsize, testValue.asInteger,
			"IntegerValue changed decimal stepsize in setter to Integer"
		);
	}
}
