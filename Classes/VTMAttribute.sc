VTMAttribute{
	var <>value;
	var <key;

	== {arg val;
		^(val.key == this.key and: {val.value == this.value});
	}

	key_{arg val;
		key = val.asSymbol;
	}
}