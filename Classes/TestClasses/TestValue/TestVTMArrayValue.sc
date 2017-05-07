TestVTMArrayValue : TestVTMCollectionValue {
	*makeRandomValue{arg params;
		^3.collect({1.0.rand2});// float list return here for now.
	}
}
