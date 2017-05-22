TestVTMValueElement : TestVTMAbstractData {
	*makeRandomParameter{arg key, params;
		var result = super.makeRandomParameter(key, params);
		result = switch(key,
			\type, {TestVTMValue.classesForTesting.collect(_.type).choose; },
			{result}
		);
		^result;
	}
}
