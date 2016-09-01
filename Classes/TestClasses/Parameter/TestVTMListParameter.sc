TestVTMListParameter : TestVTMValueParameter {
	*makeRandomValue{arg params;
		^[
			this.makeRandomInteger,
			this.makeRandomString,
			this.makeRandomDecimal;
		];
	}
}
