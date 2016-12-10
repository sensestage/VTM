VTMNumberParameter : VTMValueParameter {
	var <minVal;
	var <maxVal;
	var <stepsize = 0;
	var <clipmode = \none;
	var <dataspace;//Optional instance of VTMDataspace
	var <scheduler;//Where instances of VTMNumberInterpolator will be

	*new{arg name, declaration;
		^super.new(name, declaration).initNumberParameter;
	}

	isValidType{arg val;
		^val.isKindOf(SimpleNumber);
	}

	initNumberParameter{
		if(declaration.notEmpty, {
			if(declaration.includesKey(\clipmode), {
				this.clipmode = declaration[\clipmode];
			});
			if(declaration.includesKey(\minVal), {
				this.minVal = declaration[\minVal];
			});
			if(declaration.includesKey(\maxVal), {
				this.maxVal = declaration[\maxVal];
			});
			if(declaration.includesKey(\stepsize), {
				this.stepsize = declaration[\stepsize];
			});
		});
	}

	minVal_{ arg val;
		if(val.isNil, {
			minVal = nil;
			this.changed(\minVal);
		}, {
			if(this.isValidType(val), {
				minVal = val;
				this.changed(\minVal);
				this.value_(this.value);//update the value, might be clipped in the value set method
			}, {
				"NumberParameter:minVal_ '%' - ignoring val because of invalid type: '%[%]'".format(
					this.fullPath, val, val.class
				).warn;
			});
		});
	}

	maxVal_{ arg val;
		if(val.isNil, {
			maxVal = nil;
			this.changed(\maxVal);
		}, {
			if(this.isValidType(val), {
				maxVal = val;
				this.changed(\maxVal);
				this.value_(this.value);//update the value, might be clipped in the value set method
			}, {
				"NumberParameter:maxVal_ '%' - ignoring val because of invalid type: '%[%]'".format(
					this.fullPath, val, val.class
				).warn;
			});

		});
	}

	stepsize_{ arg val;
		var newVal = val;
		if(this.isValidType(val), {
			if(newVal.isNegative, {
				newVal = newVal.abs;
				"NumberParameter:stepsize_ '%' - val converted to positive value".format(
					this.fullPath
				).warn;
			});
			stepsize = newVal;
			this.changed(\stepsize);
		}, {
			"NumberParameter:stepsize_ '%' - ignoring val because of invalid type: '%[%]'".format(
				this.fullPath, val, val.class
			).warn;
		});
	}

	clipmode_{ arg val;
		if(#['none', 'low', 'high', 'both'].includes(val.asSymbol), {
			var newVal;
			clipmode = val.asSymbol;
			this.value_(this.value);//update the value, might be clipped in the value set method
			this.changed(\clipmode);
		}, {
			"NumberParameter:clipmode_ '%' - ignoring val because of invalid type: '%[%]'".format(
				this.fullPath, val, val.class
			).warn;
		});
	}

	value_{arg val;
		super.value_(
			this.prCheckRangeAndClipValue(val)
		);
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
				// "Setting '%' ramped value: %".format(this.fullPath, val).postln;
				this.valueAction_(val);
				0.05.wait;
				val = stream.next;
			};
		};
	}


	defaultValue_{arg val;
		defaultValue = this.prCheckRangeAndClipValue(val);
	}

	increment{arg doAction = true;
		if(doAction, {
			this.valueAction_(value + stepsize);
		}, {
			this.value_(value + stepsize);
		});
	}

	decrement{ arg doAction = true;
		if(doAction, {
			this.valueAction_(value - stepsize);
		}, {
			this.value_(value - stepsize);
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
		^(super.attributeKeys ++ [\minVal, \maxVal, \stepsize, \clipmode, \dataspace]);
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
				if(minVal.notNil and: {val < this.minVal}, {
					result = val.max(this.minVal);
				});
			},
			\high, {
				// "HIGH CLIPPING".postln;
				if(maxVal.notNil and: {val > this.maxVal}, {
					result = val.min(this.maxVal);
				});
			},
			\both, {
				// "BOTH CLIPPING".postln;
				if(minVal.notNil and: {val < this.minVal}, {
					result = val.max(this.minVal);
				}, {
					if(maxVal.notNil and: {val > this.maxVal}, {
						result = val.min(this.maxVal);
					});
				});
			}
		);
		^result;
	}

	*defaultViewType{ ^\slider; }

}
