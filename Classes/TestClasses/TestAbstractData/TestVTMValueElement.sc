TestVTMValueElement : TestVTMElement {
	//this method overides the superclass because wee need to define
	//a type for the ValueElement class to be able to generate a random
	//parameters.
	*makeRandomParameters{arg params;
		var result = super.makeRandomParameters(params);
		var valueType;

		//use random value type if not defined
		if(params.notNil and: {params.includesKey(\type)}, {
			valueType = params[\type];
		}, {
			valueType = TestVTMValue.classesForTesting.collect(_.type).choose;
		});
		result.put(\type, valueType);
		^result;
	}
}
