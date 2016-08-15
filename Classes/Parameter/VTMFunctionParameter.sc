VTMFunctionParameter : VTMStringParameter {
	isValidType{arg val;
		^(val.isKindOf(String) and: val.interpret.isKindOf(Function));
	}

	*type{ ^\function; }

	prDefaultValueForType{
		//pass through function
		^"{arg val; val;}";
	}
}
