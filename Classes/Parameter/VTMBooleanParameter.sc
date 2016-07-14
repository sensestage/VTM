VTMBooleanParameter : VTMValueParameter {

	prDefaultValueForType{ ^false; }

	type{ ^\boolean; }

	*isValidType{arg val;
		^(val.isKindOf(Boolean));
	}

	*new{arg name, declaration;
		^super.new(name, declaration).initBooleanParameter;
	}

	initBooleanParameter{

	}

	toggle{
		this.value_(this.value.not);
	}

	*defaultViewType{ ^\toggle; }
}
