/*
Class for representing timecode strings in various formats.
Value can return in mulitple format, milliseconds being the default.
The value is internally stored as milliseconds in decimals: 1050.20 being 1 second and 50.2 milliseconds
*/
VTMTimecodeValue : VTMValue {
	*type{ ^\timecode; }

	*prDefaultValueForType{
		^0;
	}

	isValidType{arg val;
		^val.isKindOf(SimpleNumber);
	}

	*new{arg attributes;
		^super.new(attributes).initTimecodeParameter;
	}

	initTimecodeParameter{
	}

	//get only milliseconds as integer
	milliseconds{arg wrap = true;
		var result = this.value;
		if(wrap, {
			result = result % 1000.0;
		});
		^result;
	}
	//get only seconds as integer
	seconds{arg wrap = true;
		var result;
		result = this.milliseconds(false) * 0.001;
		if(wrap, {
			result = (result % 60);
		});
		^result.asInteger;
	}
	//get only minutes as integer
	minutes{arg wrap = true;
		var result;
		result = this.seconds(false) / 60;
		if(wrap, {
			result = (result % 60);
		});
		^result.asInteger;
	}
	//get only hours as integer
	hours{arg wrap = true;
		var result;
		result = this.minutes(false) / 60;
		if(wrap, {
			result = (result % 24);
		});
		^result.asInteger;
	}
	//get only days as integer
	days{arg wrap = true;
		var result;
		result = this.hours(false) / 24;
		if(wrap, {
			result = result % 365;
		});
		^result.asInteger;
	}
	//Get array of [days, hours, minutes, seconds, milliseconds]
	unitArray{
		^[ this.milliseconds, this.seconds, this.minutes, this.hours, this.days	];
	}

	timestring{arg precision, maxDays, dropDaysIfPossible;
		^this.value.asTimeString(precision, maxDays, dropDaysIfPossible);
	}

	milliseconds_{arg val;
		var result;
		//Should be a SimpleNumber maximum value 999.999999. Values outside this range will be clipped
		if(val.isKindOf(SimpleNumber), {
			//subtract the existing milliseconds from value
			result = this.value - this.milliseconds;
			//add back the new millisecond value
			this.value_(result + val.clip(0.0, 999.999999));
		}, {
			"TimecodeParameter:milliseconds_ '%' - ignoring val because of invalid type: '%[%]'".format(
				val, val.class
			).warn;
		});
	}

	seconds_{arg val;
		var result;
		//Should be a SimpleNumber within 0 - 59. Values outside this range will be clipped.
		//Floats will be converted into Integer
		if(val.isKindOf(SimpleNumber), {
			//subtract the existing seconds from value
			result = this.value - (this.seconds / 1000.0);
			//add back the new second value
			result = result + (val.clip(0, 59) * 1000.0);
			this.value_(result);
		}, {
			"TimecodeParameter:seconds_  - ignoring val because of invalid type: '%[%]'".format(
				val, val.class
			).warn;
		});
	}

	minutes_{arg val;
		var result;
		//Should be a SimpleNumber within 0 - 59. Values outside this range will be clipped
		//Floats will be converted into Integer
		if(val.isKindOf(SimpleNumber), {
			//subtract the existing minutes from value
			result = this.value - (this.minutes / 60000.0);
			//add back the new minutes value
			result = result + (val.clip(0, 59) * 60000.0);
			this.value_(result);
		}, {
			"TimecodeParameter:minutes_ - ignoring val because of invalid type: '%[%]'".format(
				val, val.class
			).warn;
		});
	}

	hours_{arg val;
		var result;
		//Should be a SimpleNumber within 0 - 23. Values outside this range will be clipped
		//Floats will be converted into Integer
		if(val.isKindOf(SimpleNumber), {
			//subtract the existing minutes from value
			result = this.value - (this.hours / 3600000);
			//add back the new minutes value
			result = result + (val.clip(0, 23) * 3600000);
			this.value_(result);
		}, {
			"TimecodeParameter:hours_ - ignoring val because of invalid type: '%[%]'".format(
				 val, val.class
			).warn;
		});
	}

	days_{arg val;
		var result;
		//Should be a SimpleNumber within 0 - 364. Values outside this range will be clipped
		//Floats will be converted into Integer
		if(val.isKindOf(SimpleNumber), {
			//subtract the existing minutes from value
			result = this.value - (this.days / 86400000.0);
			//add back the new minutes value
			result = result + (val.clip(0, 364) * 86400000.0);
			this.value_(result);
		}, {
			"TimecodeParameter:days_ - ignoring val because of invalid type: '%[%]'".format(
				val, val.class
			).warn;
		});
	}

}
