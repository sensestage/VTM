TestVTMTupleValue : TestVTMListValue {

	*makeRandomValue{arg params;
		var result;
		result = super.makeRandomValue(params);
		^result;
	}

}
