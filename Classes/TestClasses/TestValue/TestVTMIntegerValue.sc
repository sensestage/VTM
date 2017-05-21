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
		var valueObj = VTMIntegerValue.new(desc);
		this.assertEquals(
			valueObj.value, desc[\value].asInteger,
			"IntegerValue changed decimal value in properties to Integer"
		);
		this.assertEquals(
			valueObj.defaultValue, desc[\defaultValue].asInteger,
			"IntegerValue changed decimal defaultValue in properties to Integer"
		);
		this.assertEquals(
			valueObj.minVal, desc[\minVal].asInteger,
			"IntegerValue changed decimal minVal in properties to Integer"
		);
		this.assertEquals(
			valueObj.maxVal, desc[\maxVal].asInteger,
			"IntegerValue changed decimal maxVal in properties to Integer"
		);
		this.assertEquals(
			valueObj.stepsize, desc[\stepsize].asInteger,
			"IntegerValue changed decimal stepsize in properties to Integer"
		);

		//Test the setter methods also
		testValue = 22.02;
		valueObj.value = testValue;
		this.assertEquals(
			valueObj.value, testValue.asInteger,
			"IntegerValue changed decimal value in setter to Integer"
		);

		testValue = 0.01;
		valueObj.defaultValue = testValue;
		this.assertEquals(
			valueObj.defaultValue, testValue.asInteger,
			"IntegerValue changed decimal defaultValue in setter to Integer"
		);

		testValue = -22.0144;
		valueObj.minVal = testValue;
		this.assertEquals(
			valueObj.minVal, testValue.asInteger,
			"IntegerValue changed decimal minVal in setter to Integer"
		);

		testValue = 1232.9191;
		valueObj.maxVal = testValue;
		this.assertEquals(
			valueObj.maxVal, testValue.asInteger,
			"IntegerValue changed decimal maxVal in setter to Integer"
		);

		testValue = 12.2;
		valueObj.stepsize = testValue;
		this.assertEquals(
			valueObj.stepsize, testValue.asInteger,
			"IntegerValue changed decimal stepsize in setter to Integer"
		);
	}
}
