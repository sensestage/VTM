TestVTMTupleParameter : TestVTMValueParameter {

	getRandom{arg val, desc, obj;
		var result;
		switch(val,
			\value, {result = [11, 9.0, \hei]},
			\defaultValue, { result = this.getRandom(\value, desc, obj); },
			{ result = super.getRandom(val, desc, obj); }
		);
		^result;
	}

}