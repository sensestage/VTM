VTMNumberParameter : VTMValueParameter {
	var <minVal;
	var <maxVal;
	var <stepsize = 0;
	var <clipmode = \none;
	var <dataspace;//Optional instance of VTMDataspace
	var <scheduler;//Where instances of VTMNumberInterpolator will be

	prDefaultValueForType{ ^0.0; }

	type{ ^\number; }

	//this class will accept numbers, either Integers or Decimals
	*isValidType{arg val; ^val.isKindOf(SimpleNumber); }


	*new{arg name, declaration;
		^super.new(name, declaration).initNumberParameter;
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
			if(this.class.isValidType(val), {
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
			if(this.class.isValidType(val), {
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
		if(this.class.isValidType(val), {
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

	value_{arg val, omitTypecheck = false;
		if(typecheck or: {omitTypecheck.not}, {
			if(this.class.isValidType(val), {
				super.value_(
					this.prCheckRangeAndClipValue(val),
					omitTypecheck: true
				);
			}, {
				"NumberParameter:value_ '%' - ignoring val because of invalid type: '%[%]'".format(
					this.fullPath, val, val.class
				).warn;
			});
		}, {
			super.value_( this.prCheckRangeAndClipValue(val) );
		});
	}

	defaultValue_{arg val;
		if(typecheck, {
			if(this.class.isValidType(val), {
				defaultValue = this.prCheckRangeAndClipValue(val);
			}, {
				"NumberParameter:defaultValue_ '%' - ignoring val because of invalid type: '%[%]'".format(
					this.fullPath, val, val.class
				).warn;
			});
		}, {
			defaultValue = val;
		});
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

	attributes{
		var result;
		result = super.attributes.putAll(IdentityDictionary[
			\minVal -> this.minVal,
			\maxVal -> this.maxVal,
			\stepsize -> this.stepsize,
			\clipmode -> this.clipmode
		]);
		if(dataspace.notNil, {
			result.put(
				\dataspace, dataspace.attributes
			);
		});
		^result;
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
