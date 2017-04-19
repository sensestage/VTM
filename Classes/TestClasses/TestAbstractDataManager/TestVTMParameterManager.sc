TestVTMParameterManager : TestVTMAbstractDataManager {

	//make one of each type for testing
	*makeTestAttributes{
		^TestVTMParameter.testTypes.collect{arg item;
			TestVTMParameter.makeRandomAttributes(item)
		};
	}
}