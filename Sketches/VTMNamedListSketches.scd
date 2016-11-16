VTMUnitTest.runTestForClass(VTMNamedList, true);

a = VTMNamedList.new([\aa,\bb,\cc], [1 -> \myAA, 3 -> \myCC]);
a[1]
a.items
a.getItems
a.names

[\myAA -> \aa] == [\myAA -> \aa];
[\myAA -> \aa] == [\myAA -> nil]

(\myAA -> \aa) == (\myAA -> \aa)
(\myAA -> \aa) == (\myAA -> nil)

Association(\myAA, 111) == Association(\myAA, 222)

Association(\myAA, 111).trulyEqual( Association(\myAA, 222) );
Association(\myAA, 111).trulyEqual( Association(\myAA, 111) );