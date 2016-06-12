VTMSymbolParameter : VTMValueParameter {

	type{ ^\symbol; }

	defaultValueForType{
		^'';
	}

	isValidType{arg val;
		^val.isKindOf(Symbol);
	}
}