TestVTMListEnumerator : VTMUnitTest {
	setUp{}
	tearDown{}

	test_DefaultConstruction{
		var obj;
		var testList = [\aa, \bb, \cc];
		obj = VTMListEnumerator(testList);
		//check order of values
		this.assertEquals(
			obj.items, testList,
			"ListEnumerator returned the correct items."
		);
//		//check default enum keys to be correct
//		this.assertEquals(
//			obj.keys, [1,2,3],
//			"ListEnumerator returned the correct keys"
//		);
//		//check associations array
//		this.assertEquals(
//			obj.associations, testList.collect({arg item, i; i -> item}),
//			"ListEnumerator returned the correct assiations."
//		);
	}

	test_SetRemoveSymbolEnumeration{}

	test_AddRemoveItems{
		//Item enumeration should be updated when adding removing items
	}

	test_MoveItemsInList{}
}
