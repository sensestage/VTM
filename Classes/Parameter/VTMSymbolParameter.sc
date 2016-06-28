VTMSymbolParameter : VTMValueParameter {

	type{ ^\symbol; }

	prDefaultValueForType{
		^'';
	}

	isValidType{arg val;
		^val.isKindOf(Symbol);
	}
}