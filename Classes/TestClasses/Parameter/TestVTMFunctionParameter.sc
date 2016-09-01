TestVTMFunctionParameter : TestVTMValueParameter {

	*makeRandomValue{arg params;
		var result;
		result = "{arg param; param;}";//temporary pass-through function
		^result;
	}
}
