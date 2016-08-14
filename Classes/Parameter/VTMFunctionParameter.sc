VTMFunctionParameter : VTMStringParameter {
	isValidType{arg val;
		^(val.isKindOf(String) and: val.interpret.isKindOf(Function));
	}
}
