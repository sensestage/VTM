VTMFunctionParameter : VTMStringParameter {
	isValidType{arg val;
		var result = false;
		if(val.isKindOf(String), {
			if(val.interpret.isKindOf(Function), {
				result = true;
			});
		});
		^result;
	}

	*type{ ^\function; }

	prDefaultValueForType{
		//pass through function
		^"{arg val; val;}";
	}
}
