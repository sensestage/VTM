{arg test;
	test.postln;
	test.assertEquals(11, 11, "No problem withthe elevens", onFailure: {
		"Ai the test failed miserably".postln;
	});
};