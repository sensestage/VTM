VTMDecimalParameter : VTMScalarParameter {
	prDefaultValueForType{ ^0.0; }

	minVal_{arg val;
		if(val.class == Integer, {
			val = val.asFloat;
		});
		super.minVal_(val);
	}

	maxVal_{arg val;
		if(val.class == Integer, {
			val = val.asFloat;
		});
		super.maxVal_(val);
	}

	stepsize_{arg val;
		if(val.class == Integer, {
			val = val.asFloat;
		});
		super.stepsize_(val);
	}

	value_{arg val;
		if(val.class == Integer, {
			val = val.asFloat;
			super.value_(val, omitTypecheck: true);
		}, {
			super.value_(val);
		});
	}

	defaultValue_{arg val;
		if(val.class == Integer, {
			val = val.asFloat;
		});
		super.defaultValue_(val);
	}
}