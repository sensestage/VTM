VTMBooleanParameter : VTMValueParameter {
	var whenToDoAction = \change;

	prDefaultValueForType{ ^false; }

	*type{ ^\boolean; }

	isValidType{arg val;
		^val.isKindOf(Boolean);
	}

	*new{arg name, declaration;
		^super.new(name, declaration).initBooleanParameter;
	}

	initBooleanParameter{

	}

	toggle{
		this.valueAction_(this.value.not);
	}

	doActionOn{arg when;
		if([\rising, \falling, \change].includes(when), {
			whenToDoAction = when;
		}, {
			"%:% - Uknown option %.\n\tAlternatives are 'rising', 'fallin', and 'change'".format(
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
