VTMTupleParameter : VTMListParameter {
	isValidType{arg val;
		var result = false;
		if(super.isValidType(val), {
			result = this.validate(val);
		});
		^result;
	}

	*type{ ^\tuple; }
	prDefaultValueForType{
		^[];
	}
}
