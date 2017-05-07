VTMIntegerValue : VTMNumberValue {
	*type{ ^\integer; }

	*prDefaultValueForType{ ^0; }
	//this class will accept numbers, either Integers or Floats
	//but it will convert Float numbers to Integers


	minVal_{arg val;
		if(val.class == Float, {
			val = val.asInteger;
		});
		super.minVal_(val);
	}

	maxVal_{arg val;
		if(val.class == Float, {
			val = val.asInteger;
		});
		super.maxVal_(val);
	}

	stepsize_{arg val;
		if(val.class == Float, {
			val = val.asInteger;
		});
		super.stepsize_(val);
	}

	value_{arg val;
		if(val.class == Float, {
			val = val.asInteger;
		});
		super.value_(val);
	}

	defaultValue_{arg val;
		if(val.class == Float, {
			val = val.asInteger;
		});
		super.defaultValue_(val);
	}
}
