VTMSymbolParameter : VTMValueParameter {

	defaultValueForType{
		^'';
	}

	isValidType{arg val;
		^val.isKindOf(Symbol);
	}
}