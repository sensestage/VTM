TestVTMParameterManager : TestVTMAbstractDataManager {

	//Attributes is an array of parameter attributes that will
	//override the random generation.
	//E.g. an parameter manager attributes array can look like this:
	//[
	//  ['type', \integer]
	//  ['type', 'decimal', 'minVal', 0.1]
	//]
	//If attribtues is nil, an array of random size
	//with random parameter attributes is made.
	*makeRandomData{arg attributes;

	}

	//make one of each type for testing
	*makeTestAttributes{
		^TestVTMParameter.testTypes.collect{arg item;
			TestVTMParameter.makeRandomAttributes(item)
		};
	}
}