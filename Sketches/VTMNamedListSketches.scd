VTMUnitTest.runTestForClass(VTMNamedList, true);

a = VTMNamedList.new([\myAA -> \aa,\bb,\cc]);
(
a = VTMNamedList.newFromKeyValuePairs(
	[\myAA, \aa, 2, \bb, \myCC, \cc]);
)
a[1]
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
VTMUnitTest.runTestForClass(VTMParameter);

(
(3..8).choose.collect({arg i;
	[
		[i, "absgc".scramble.asSymbol].choose,
		100.rand
	];
}).flatten;
)