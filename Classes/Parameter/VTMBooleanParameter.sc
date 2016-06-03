VTMBooleanParameter : VTMValueParameter {

	prDefaultValueForType{ ^false; }

	*isValidType{arg val;
		^(val.isKindOf(Boolean));
	}

	*new{arg name, description;
		^super.new(name, description).initBooleanParameter;
	}

	initBooleanParameter{

	}

	toggle{
		this.value_(this.value.not);
	}
}