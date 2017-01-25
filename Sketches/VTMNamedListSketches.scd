VTMUnitTest.runTestForClass(VTMNamedList, true);

a = VTMNamedList.new([\myAA -> \aa,\bb,\cc]);
(
a = VTMNamedList.newFromKeyValuePairs(
	[\myAA, \aa, 2, \bb, \myCC, "abcdef"]);
)
a[3].class
a.items
a.getItems
a.names
a.asKeyValuePairs

[\myAA -> \aa] == [\myAA -> \aa];
[\myAA -> \aa] == [\myAA -> nil]

(\myAA -> \aa) == (\myAA -> \aa)
(\myAA -> \aa) == (\myAA -> nil)

Association(\myAA, 111) == Association(\myAA, 222)

Association(\myAA, 111).trulyEqual( Association(\myAA, 222) );
Association(\myAA, 111).trulyEqual( Association(\myAA, 111) );

VTMUnitTest.runTestForClass(VTMValueParameter);

VTMUnitTest.reportAllErrors = true;
VTMUnitTest.runTestForClass(VTMValueParameter, true);
VTMUnitTest.runTestForClass(VTMNumberParameter,true);
VTMUnitTest.runTestForClass(VTMParameter, true);

TestVTMParameter.makeRandomAttributes(\integer)


VTMUnitTest.runTest("TestVTMParameter:test_GetAttributes")
VTMUnitTest.listUntestedMethods(VTMNamedList)

VTMUnitTest.runTest("TestVTMValueParameter:test_Enum")
(
3.collect({arg i;
	[
		[1 + i, "absgc".scramble.asSymbol].choose,
		"abcdef"
	];
}).flatten;
)
[999, "absgc".scramble.asSymbol].choose
