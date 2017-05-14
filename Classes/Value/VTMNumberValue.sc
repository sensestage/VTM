VTMNumberValue : VTMValue {
	var <dataspace;//Optional instance of VTMDataspace
	var <scheduler;//Where instances of VTMNumberInterpolator will be

	*new{arg description;
		^super.new(description).initNumberParameter;
	}

	isValidType{arg val;
		^val.isKindOf(SimpleNumber);
	}

	initNumberParameter{
		if(description.notEmpty, {
			if(description.includesKey(\clipmode), {
				this.clipmode = description[\clipmode];
			});
			if(description.includesKey(\minVal), {
				this.minVal = description[\minVal];
			});
			if(description.includesKey(\maxVal), {
				this.maxVal = description[\maxVal];
			});
			if(description.includesKey(\stepsize), {
				this.stepsize = description[\stepsize];
			});
		});
	}



	ramp{arg targetValue, time, curve = \lin;
		if(scheduler.isPlaying, {
			scheduler.stop;
		});
		scheduler = fork{
			var stream, val;
			stream = Env([this.value, targetValue], [time]).asPseg.asStream;
			val = stream.next;
			loop{
				if(val.isNil, {
					this.valueAction_(targetValue);
					thisThread.stop;
				});
				this.valueAction_(val);
				0.05.wait;
				val = stream.next;
			};
		};
	}


	defaultValue_{arg val;
		super.defaultValue = this.prCheckRangeAndClipValue(val);
	}

	increment{arg doAction = true;
		if(doAction, {
			this.valueAction_(this.value + this.stepsize);
		}, {
			this.value_(this.value + this.stepsize);
		});
	}

	decrement{ arg doAction = true;
		if(doAction, {
			this.valueAction_(this.value - this.stepsize);
		}, {
			this.value_(this.value - this.stepsize);
		});
	}

	*descriptionKeys{
		^(super.descriptionKeys ++ [\minVal, \maxVal, \stepsize, \clipmode/*, \dataspace*/]);
	}


	prCheckRangeAndClipValue{arg val;
		var result;
		result = val;
		switch(this.clipmode,
			\none, {
				// "NONE CLIPPING".postln;
			},
			\low, {
				// "LOW CLIPPING".postln;
				if(this.minVal.notNil and: {val < this.minVal}, {
					result = val.max(this.minVal);
				});
			},
			\high, {
				// "HIGH CLIPPING".postln;
				if(this.maxVal.notNil and: {val > this.maxVal}, {
					result = val.min(this.maxVal);
				});
			},
			\both, {
				// "BOTH CLIPPING".postln;
				if(this.minVal.notNil and: {val < this.minVal}, {
					result = val.max(this.minVal);
				}, {
					if(this.maxVal.notNil and: {val > this.maxVal}, {
						result = val.min(this.maxVal);
					});
				});
			}
		);
		^result;
	}

	*defaultViewType{ ^\slider; }

	//Description setters and getters
	minVal_{ arg val;
		if(val.isNil, {
			this.set(\minVal, nil);
		}, {
			if(this.isValidType(val), {
				this.set(\minVal, val);
				this.value_(this.value);//update the value, might be clipped in the value set method
			}, {
				"NumberParameter:minVal_ - ignoring val because of invalid type: '%[%]'".format(
					val, val.class
				).warn;
			});
		});
	}
	minVal{ ^this.get(\minVal); }

	maxVal_{ arg val;
		if(val.isNil, {
			this.set(\maxVal, nil);
		}, {
			if(this.isValidType(val), {
				this.set(\maxVal, val);
				this.value_(this.value);//update the value, might be clipped in the value set method
			}, {
				"NumberParameter:maxVal_ - ignoring val because of invalid type: '%[%]'".format(
					val, val.class
				).warn;
			});

		});
	}
	maxVal{ ^this.get(\maxVal); }

	stepsize_{ arg val;
		var newVal = val;
		if(this.isValidType(val), {
			if(newVal.isNegative, {
				newVal = newVal.abs;
				"NumberParameter:stepsize_ - val converted to positive value".warn;
			});
			this.set(\stepsize, newVal);
		}, {
			"NumberParameter:stepsize_ - ignoring val because of invalid type: '%[%]'".format(
				val, val.class
			).warn;
		});
	}
	stepsize{ ^this.get(\stepsize) ? 0; }

	clipmode_{ arg val;
		if(#['none', 'low', 'high', 'both'].includes(val.asSymbol), {
			var newVal;
			this.set(\clipmode, val.asSymbol);
			this.value_(this.value);//update the value, might be clipped in the value set method
		}, {
			"NumberParameter:clipmode_ - ignoring val because of invalid type: '%[%]'".format(
				val, val.class
			).warn;
		});
	}
	clipmode{ ^this.get(\clipmode) ? \none; }

	value_{arg val;
		super.value_(
			this.prCheckRangeAndClipValue(val)
		);
	}

}
