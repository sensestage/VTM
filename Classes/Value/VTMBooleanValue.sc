VTMBooleanValue : VTMValue {
	var <doActionOn = \change;

	*prDefaultValueForType{ ^false; }

	*type{ ^\boolean; }

	isValidType{arg val;
		^val.isKindOf(Boolean);
	}

	*new{arg description;
		^super.new(description).initBooleanParameter;
	}

	initBooleanParameter{

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

	*makeOSCAPI{arg param;
		^super.makeOSCAPI(param).putAll(IdentityDictionary[
			'toggle!' -> {param.toggle;}
		]);
	}
}
