TestVTMFunctionParameter : TestVTMValueParameter {

	getRandom{arg val, desc, obj;
		var result;
		switch(val,
			\value, {result = "{ 1.0.rand }"},
			\defaultValue, { result = this.getRandom(val, desc, obj); }
		);
		^result;
	}
}