TestVTMDictionaryParameter : TestVTMListParameter {
	*makeRandomValue{arg params;
		//This is a temporary solution
		^Dictionary[
			this.makeRandomString -> this.makeRandomInteger,
			this.makeRandomString -> this.makeRandomString,
			this.makeRandomString -> this.makeRandomDecimal;
		];
	}

}
