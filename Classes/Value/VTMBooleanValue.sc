VTMBooleanValue : VTMValue {
	var <doActionOn = \change; //\change | \rising | \falling

	*prDefaultValueForType{ ^false; }

	*type{ ^\boolean; }

	isValidType{arg val;
		^val.isKindOf(Boolean);
	}

	*new{arg properties;
		^super.new(properties).initBooleanValue;
	}

	initBooleanValue{

	}

	toggle{
		this.valueAction_(this.value.not);
	}

	doActionOn_{arg when;
		if([\rising, \falling, \change].includes(when), {
			doActionOn = when;
		}, {
			"%:% - Uknown option %.\n\tAlternatives are 'rising', 'falling', and 'change'".format(
				this.class.name,
				thisMethod.name,
				when
			).warn;
		});
	}

	*defaultViewType{ ^\toggle; }
}
