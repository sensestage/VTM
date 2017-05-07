VTMNumberValue : VTMValue {
	var <dataspace;//Optional instance of VTMDataspace
	var <scheduler;//Where instances of VTMNumberInterpolator will be

	*new{arg attributes;
		^super.new(attributes).initNumberParameter;
	}

	isValidType{arg val;
		^val.isKindOf(SimpleNumber);
	}

	initNumberParameter{
		if(attributes.notEmpty, {
			if(attributes.includesKey(\clipmode), {
				this.clipmode = attributes[\clipmode];
			});
			if(attributes.includesKey(\minVal), {
				this.minVal = attributes[\minVal];
			});
			if(attributes.includesKey(\maxVal), {
				this.maxVal = attributes[\maxVal];
			});
			if(attributes.includesKey(\stepsize), {
				this.stepsize = attributes[\stepsize];
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

	*makeAttributeGetterFunctions{arg param;
		var result;
		result = super.makeAttributeGetterFunctions(param).putAll(
			IdentityDictionary[
				\minVal -> {param.minVal},
				\maxVal -> {param.maxVal},
				\stepsize -> {param.stepsize},
				\clipmode -> {param.clipmode},
				\dataspace -> {
					var val;
					if(param.dataspace.notNil, {
						val = param.dataspace.attributes;
					});
					val;
				}
			]
		);
		^result;
	}

	*makeAttributeSetterFunctions{arg param;
		var result;
		result = super.makeAttributeGetterFunctions(param).putAll(
			IdentityDictionary[
				\minVal -> {arg ...args; param.minVal_(args[0]); },
				\maxVal -> {arg ...args; param.maxVal_(args[0]); },
				\stepsize -> {arg ...args; param.stepsize_(args[0]); },
				\clipmode -> {arg ...args; param.clipmode_(args[0]); },
				\dataspace -> {arg ...args;
					if(param.dataspace.notNil, {
						param.dataspace_(*args);
					});
				}
			]
		);
		^result;
	}

	*makeOSCAPI{arg param;
		^super.makeOSCAPI(param).putAll(IdentityDictionary[
			'increment!' -> {param.increment;},
			'decrement!' -> {param.decrement;}
		]);
	}

	*attributeKeys{
		^(super.attributeKeys ++ [\minVal, \maxVal, \stepsize, \clipmode/*, \dataspace*/]);
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

	//Attributes setters and getters
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
