TestVTMTimecodeValue : TestVTMValue {

	*makeRandomValue{arg params;
		var result;
		var minTime, maxTime;
		if(params.notNil, {
			minTime = params[\minTime] ? "0:0:0:0:0".asSecs;
			maxTime = params[\maxTime] ? "354:23:59:59:999".asSecs;
		}, {
			minTime = "0:0:0:0:0".asSecs;
			maxTime = "354:23:59:59:999".asSecs;
		});
		result = rrand(minTime, maxTime);
		^result;
	}

	setUp{
		"Setting up a VTMStringValueTest".postln;
	}

	tearDown{
		"Tearing down a VTMStringValueTest".postln;
	}

	test_DefaultProperties{
		var valueObj = VTMTimecodeValue.new;
		//Should return 0 as defaultValue
		this.assertEquals(
			valueObj.defaultValue, 0,
			"TimecodeValue has defaultValue 0"
		);
		//Should return 0 as value
		this.assertEquals(
			valueObj.value, 0,
			"TimecodeValue has value 0"
		);
	}

	test_SettingPropertiesThroughProperties{
		var desc = (
			value: 1000, defaultValue: 3000,
		);
		var valueObj = VTMTimecodeValue.new(desc);
		this.assertEquals(
			valueObj.defaultValue, desc[\defaultValue],
			"TimecodeValue set defaultValue through properties"
		);
		this.assertEquals(
			valueObj.value, desc[\value],
			"TimecodeValue set value through properties"
		);
	}

	test_GetValueInUnitFormats{
		var valueObj = VTMTimecodeValue.new();

		//(275128022.8 * 0.001).asTimeString(0.000001);
		valueObj.value = 275128022.8;//3 days, 4 hours, 25 minutes 28 seconds and 22.8 ms
		this.assertFloatEquals(
			valueObj.milliseconds, 22.8,
			"TimecodeValue returned correct milliseconds"
		);
		this.assertEquals(
			valueObj.seconds, 28,
			"TimecodeValue returned correct seconds"
		);
		this.assertEquals(
			valueObj.minutes, 25,
			"TimecodeValue returned correct minutes"
		);
		this.assertEquals(
			valueObj.hours, 4,
			"TimecodeValue returned correct hours"
		);
		this.assertEquals(
			valueObj.days, 3,
			"TimecodeValue returned correct days"
		);
	}

	test_SetValueInUnitFormats{

		//Testing with random numbers
		1.do{ //can change this to do more tests
			var testValue = (
				milliseconds: rrand(0.0, 999.999),
				seconds: rrand(0, 59),
				minutes: rrand(0, 59),
				hours: rrand(0, 23),
				days: rrand(0, 364)
			);
			var testSum;
			var valueObj = VTMTimecodeValue.new;

			valueObj.milliseconds_(testValue[\milliseconds]);
			this.assertFloatEquals(
				valueObj.milliseconds, testValue[\milliseconds],
				"TimecodeValue set the value in milliseconds correctly"
			);

			//add seconds
			valueObj.seconds_(testValue[\seconds]);
			this.assertFloatEquals(
				valueObj.seconds, testValue[\seconds],
				"TimecodeValue set the value in seconds correctly"
			);

			//add minutes
			valueObj.minutes_(testValue[\minutes]);
			this.assertFloatEquals(
				valueObj.minutes, testValue[\minutes],
				"TimecodeValue set the value in minutes correctly"
			);

			//add hours
			valueObj.hours_(testValue[\hours]);
			this.assertFloatEquals(
				valueObj.hours, testValue[\hours],
				"TimecodeValue set the value in hours correctly"
			);

			//add days
			valueObj.days_(testValue[\days]);
			this.assertFloatEquals(
				valueObj.days, testValue[\days],
				"TimecodeValue set the value in days correctly"
			);

			testSum = (
				testValue[\milliseconds] +
				(testValue[\seconds] * 1000.0) +
				(testValue[\minutes] * 1000.0 * 60.0) +
				(testValue[\hours] * 1000.0 * 60.0 * 60.0) +
				(testValue[\days] * 1000.0 * 60.0 * 60.0 * 24.0)
			);
			this.assertFloatEquals(
				valueObj.value, testSum,
				"TimecodeValue summed all unit values into correct sum"
			);
		};


	}
}
