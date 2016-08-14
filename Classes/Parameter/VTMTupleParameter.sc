VTMTupleParameter : VTMArrayParameter {
	isValidType{arg val;
		var result = false;
		if(super.isValidType(val), {
			result = this.validate(val);
		});
		^result;
	}

}
