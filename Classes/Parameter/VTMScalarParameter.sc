VTMScalarParameter : VTMValueParameter {
	var <minVal = 0;//FIXME: this default value is yet to be decided upon
	var <maxVal = 100;//FIXME: this default value is yet to be decided upon
	var <stepsize = 0;
	var <clipmode = \none;
	var <dataspace;//Optional instance of VTMDataspace
	var <scheduler;//Where instances of VTMScalarInterpolator will be

	//this class will accept numbers, either Integers or Decimals
	*isValidType{arg val; ^val.isKindOf(SimpleNumber); }


	*new{arg name, description;
		^super.new(name, description).initScalarParameter;
	}

	initScalarParameter{
		if(description.notNil, {
			if(description.includesKey(\minVal), {
				this.minVal = description[\minVal];
			});
			if(description.includesKey(\maxVal), {
				this.maxVal = description[\maxVal];
			});
			if(description.includesKey(\stepsize), {
				this.stepsize = description[\stepsize];
			});
			if(description.includesKey(\clipmode), {
				this.clipmode = description[\clipmode];
			});
		});
	}

	minVal_{ arg val;
		if(this.class.isValidType(val), {
			minVal = val;
			this.changed(\minVal);
		}, {
			"ScalarParameter:minVal_ '%' - ignoring val because of invalid type: '%[%]'".format(
				this.fullPath, val, val.class
			).warn;
		});
	}

	maxVal_{ arg val;
		if(this.class.isValidType(val), {
			maxVal = val;
			this.changed(\maxVal);
		}, {
			"ScalarParameter:maxVal_ '%' - ignoring val because of invalid type: '%[%]'".format(
				this.fullPath, val, val.class
			).warn;
		});
	}

	stepsize_{ arg val;
		var newVal = val;
		if(this.class.isValidType(val), {
			if(newVal.isNegative, {
				newVal = newVal.abs;
				"ScalarParameter:stepsize_ '%' - val converted to positive value".format(
					this.fullPath
				).warn;
			});
			stepsize = newVal;
			this.changed(\stepsize);
		}, {
			"ScalarParameter:stepsize_ '%' - ignoring val because of invalid type: '%[%]'".format(
				this.fullPath, val, val.class
			).warn;
		});
	}

	clipmode_{ arg val;
		if(#['none', 'low', 'high', 'both'].includes(val.asSymbol), {
			clipmode = val.asSymbol;
			this.changed(\clipmode);
		}, {
			"ScalarParameter:clipmode_ '%' - ignoring val because of invalid type: '%[%]'".format(
				this.fullPath, val, val.class
			).warn;
		});
	}

	value_{arg val;
		var newVal;
		if(this.class.isValidType(val), {
			switch(clipmode,
				\none, {
					newVal = val;
				},
				\low, {
					newVal = val.max(minVal);
				},
				\high, {
					newVal = val.min(maxVal);
				},
				\both, {
					newVal = val.clip(minVal, maxVal);
				}
			);
			super.value_(newVal, omitTypecheck: true);
		}, {
			"ScalarParameter:value_ '%' - ignoring val because of invalid type: '%[%]'".format(
				this.fullPath, val, val.class
			).warn;
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

}