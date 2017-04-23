TestVTMListParameter : TestVTMCollectionParameter {
	*makeRandomValue{arg params;
		^[
			this.makeRandomInteger,
			this.makeRandomString,
			this.makeRandomDecimal;
		];
	}
}
