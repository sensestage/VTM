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

	test_DefaultAttributes{
		var param = VTMTimecodeValue.new;
		//Should return 0 as defaultValue
		this.assertEquals(
			param.defaultValue, 0,
			"TimecodeValue has defaultValue 0"
		);
		//Should return 0 as value
		this.assertEquals(
			param.value, 0,
			"TimecodeValue has value 0"
		);
	}

	test_SettingAttributesThroughAttributes{
		var desc = (
			value: 1000, defaultValue: 3000,
		);
		var param = VTMTimecodeValue.new(desc);
		this.assertEquals(
			param.defaultValue, desc[\defaultValue],
			"TimecodeValue set defaultValue through attributes"
		);
		this.assertEquals(
			param.value, desc[\value],
			"TimecodeValue set value through attributes"
		);
	}

	test_GetValueInUnitFormats{
		var param = VTMTimecodeValue.new();

		//(275128022.8 * 0.001).asTimeString(0.000001);
		param.value = 275128022.8;//3 days, 4 hours, 25 minutes 28 seconds and 22.8 ms
		this.assertFloatEquals(
			param.milliseconds, 22.8,
			"TimecodeValue returned correct milliseconds"
		);
		this.assertEquals(
			param.seconds, 28,
			"TimecodeValue returned correct seconds"
		);
		this.assertEquals(
			param.minutes, 25,
			"TimecodeValue returned correct minutes"
		);
		this.assertEquals(
			param.hours, 4,
			"TimecodeValue returned correct hours"
		);
		this.assertEquals(
			param.days, 3,
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
			var param = VTMTimecodeValue.new;

			param.milliseconds_(testValue[\milliseconds]);
			this.assertFloatEquals(
				param.milliseconds, testValue[\milliseconds],
				"TimecodeValue set the value in milliseconds correctly"
			);

			//add seconds
			param.seconds_(testValue[\seconds]);
			this.assertFloatEquals(
				param.seconds, testValue[\seconds],
				"TimecodeValue set the value in seconds correctly"
			);

			//add minutes
			param.minutes_(testValue[\minutes]);
			this.assertFloatEquals(
				param.minutes, testValue[\minutes],
				"TimecodeValue set the value in minutes correctly"
			);

			//add hours
			param.hours_(testValue[\hours]);
			this.assertFloatEquals(
				param.hours, testValue[\hours],
				"TimecodeValue set the value in hours correctly"
			);

			//add days
			param.days_(testValue[\days]);
			this.assertFloatEquals(
				param.days, testValue[\days],
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
				param.value, testSum,
				"TimecodeValue summed all unit values into correct sum"
			);
		};


	}
}
