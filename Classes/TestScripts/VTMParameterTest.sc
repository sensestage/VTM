TestVTMParameter : UnitTest {
	setUp{
		"Setting up a VTMParameterTest".postln;
	}

	tearDown{
		"Tearing down a VTMParameterTest".postln;
	}

	test_MakingFromDescription{
		var newParameter;
		var description = (
			type: \integer, default: 3, action: {|p| p.value.postln;}
		);
		this.assertEquals(11, 11, "11 is indeed 11", onFailure: {"11 is apparently 11".postln;});
	}
}

TestVTMIntegerParameter : TestVTMParameter {
	setUp{
		"Setting up a VTMIntegerParameterTest".postln;
	}

	tearDown{
		"Tearing down a VTMIntegerParameterTest".postln;
	}

	// test_MakingFromDescription{
	// 	var newParameter;
	// 	var description = (
	// 		type: \integer, default: 3, action: {|p| p.value.postln;}
	// 	);
	// 	this.assertEquals(11, 11, "11 is indeed 11", onFailure: {"11 is apparently 11".postln;});
	// }
}