TestVTMNamedList : VTMUnitTest {
	*equalAssociations{arg a, b;
		if(a.size == b.size, {
			^a.every({arg it, i;
				(a[i].key == b[i].key) && (a[i].value == b[i].value);
			});
		}, {
			^false;
		});
	}

	setUp{}
	tearDown{}

	test_DefaultConstruction{
		var obj;
		var testList = [\myAA -> \aa, \bb, \myCC -> \cc];
		var is, shouldBe;
		obj = VTMNamedList(testList);
		//check order of values
		this.assertEquals(
			obj.getItems, testList.collect(_.value),
			"NamedList returned the correct items."
		);

		//check order of values
		this.assertEquals(
			obj.names, [\myAA, 2, \myCC],
			"NamedList returned the correct names."
		);

		//check associations array
		is = obj.associations;
		shouldBe = ['myAA' -> \aa, 2 -> \bb, 'myCC' -> \cc];

		this.assert(
			this.class.equalAssociations(
				is, shouldBe),
			"NamedList returned the correct associations." ++
			"\nIs:\n\t" + is + "\nShould be:\n\t" + shouldBe + "\n"
		);
	}

	test_NewFromKeyValuePairs{
		var obj;
		var testList = [\myAA, \aa, 2, \bb, \myCC, \cc];
		obj = VTMNamedList.newFromKeyValuePairs(testList);

		this.assertEquals(
			obj.asKeyValuePairs,
			testList,
			"NamedList init and returned correct key value pairs"
		);
	}

	test_AddRemoveItems{
		var obj;
		var is, shouldBe;
		var testList = [\myAA -> \aa, \bb, \myCC -> \cc];
		obj = VTMNamedList(testList);

		//should add item to end without name
		obj.addItem(\dd);
		is = obj.associations;
		shouldBe = [
			\myAA -> \aa, 2 -> \bb, \myCC -> \cc, 4 -> \dd
		];
		this.assert(
			this.class.equalAssociations(is, shouldBe),
			"NamedList should add item to end without name." ++
			"\nIs:\n\t" + is + "\nShould be:\n\t" + shouldBe + "\n"
		);

		//should add item to third slot without name
		obj.addItem(\ee, slot: 3);
		is = obj.associations;
		shouldBe = [
			\myAA -> \aa, 2 -> \bb, 3 -> \ee, \myCC -> \cc, 5 -> \dd
		];
		this.assert(
			this.class.equalAssociations(is, shouldBe),
			"NamedList should add item to third slot without name." ++
			"\nIs:\n\t" + is + "\nShould be:\n\t" + shouldBe + "\n"
		);

		//should add named item to first slot
		obj.addItem(\ff, \myFF, 1);
		is = obj.associations;
		shouldBe = [
			\myFF -> \ff, \myAA -> \aa, 3 -> \bb,
			4 -> \ee, \myCC -> \cc, 6 -> \dd
		];
		this.assert(
			this.class.equalAssociations(is, shouldBe),
			"NamedList should add named item to first slot." ++
			"\nIs:\n\t" + is + "\nShould be:\n\t" + shouldBe + "\n"
		);

		//should remove the second item
		obj.removeItem(2);
		is = obj.associations;
		shouldBe = [
			\myFF -> \ff, 2 -> \bb,
			3 -> \ee, \myCC -> \cc, 5 -> \dd
		];
		this.assert(
			this.class.equalAssociations(is, shouldBe),
			"NamedList should remove the second item." ++
			"\nIs:\n\t" + is + "\nShould be:\n\t" + shouldBe + "\n"
		);

		//should remove named item
		obj.removeItem(\myCC);
		is = obj.associations;
		shouldBe = [
			\myFF -> \ff,
			2 -> \bb,
			3 -> \ee,
			4 -> \dd
		];
		this.assert(
			this.class.equalAssociations(is, shouldBe),
			"NamedList should remove named item." ++
			"\nIs:\n\t" + is + "\nShould be:\n\t" + shouldBe + "\n"
		);

		//should move named item to third slot
		obj.moveItem(\myFF, 3);
		is = obj.associations;
		shouldBe = [
			1 -> \bb,
			2 -> \ee,
			\myFF -> \ff,
			4 -> \dd
		];
		this.assert(
			this.class.equalAssociations(is, shouldBe),
			"NamedList should move named item to third slot." ++
			"\nIs:\n\t" + is + "\nShould be:\n\t" + shouldBe + "\n"
		);
	}

	test_OutOfBoundsErrorThrowing{
		var obj;
		var is, shouldBe;

		//should throw when items are not sequenceable collection
		try{
			obj = VTMNamedList(44);
			this.failed(thisMethod,
				"NamedList threw did not throw error when init with non sequenceable collection"
			);
		} {|err|
			this.passed(thisMethod,
				"NamedList threw error correctly when init with non sequencable collection"
			);
		};

		try{
			obj = VTMNamedList([\myAA -> \aa, \myArray -> [11,22,33], \cc]);

			//should return nil when removing non-existing item by symbol
			this.assertEquals(
				obj.removeItem(\nonExisting), nil,
				"NamedList returned nil when removing non-existing item by symbol"
			);
			//should return nil when removing non-existing item by integer
			this.assertEquals(
				obj.removeItem(-1), nil,
				"NamedList returned nil when removing non-existing item by integer"
			);
			//should return nil when accesing non-existing item by symbol
			this.assertEquals(
				obj[\nonExisting], nil,
				"NamedList returned nil when accessing non-existing item by symbol"
			);
			//should return nil when accesing non-existing item by integer
			this.assertEquals(
				obj[-1], nil,
				"NamedList returned nil when accessing non-existing item by integer"
			);
		} {|err|
			this.failed(thisMethod,
				"NamedList threw error when getting or removing non-existing items: \n\t%".format(err.errorString)
			);
		};

		//should throw error when moving item to slot number outside bounds


		//should return empty arrays for -associations, -names, and -getItems
		//when items is empty.
		try{
			obj = VTMNamedList();
			this.assert(
				(obj.getItems.isArray && obj.getItems.isEmpty) and:
				{obj.associations.isArray && obj.associations.isEmpty} and:
				{obj.names.isArray && obj.names.isEmpty},
				"NamedList returned empty arrays for -getItems, -names, and -associations"
			);
		} {|err|
			this.failed(thisMethod,
				"NamedList threw error when testing empty arrays"
			);
		};
	}

	test_ItemSettersAndGetters{
		var obj = VTMNamedList(
			[\aa, \myBB -> \bb, [11,22,33], \myDict -> (foo: 987, fum: 654)]
		);

		// //should return named item ny slot number argument
		this.assertEquals(
			obj[2], \bb,
			"NamedList returned named item by integer slot number argument."
		);

		//should return item name
		this.assertEquals(
			obj.getItemName(4), \myDict,
			"NamedList returned item name from integer slot number argument."
		);

		//should retun nil as item name for unnamed items
		this.assertEquals(
			obj.getItemName(1), nil,
			"NamedList returned nil as item name from unnamed item."
		);

		//should set item name for unnamed item
		obj.setItemName(2, \myBB);
		this.assertEquals(
			obj.getItemName(2), \myBB,
			"NamedList set item name from integer slot number argument."
		);

		//should rename item name for already named item
		obj.setItemName(4, \myRenamedDict);
		this.assertEquals(
			obj.getItemName(4), \myRenamedDict,
			"NamedList renamed item from integers slot number argument"
		);

		//should remove name by setting it to nil;
		obj.setItemName(4, nil);
		this.assertEquals(
			obj.getItemName(4), nil,
			"NamedList removed name from item."
		);

		//should return item slot number for named item
		// this.assertEquals(
		// 	obj.getItemSlotNumber(\myBB), 2,
		// 	"NamedList renamed item from integer slot number argument."
		// );
	}

	test_ChangingItem{
		var obj;
		var beforeChange, afterChange;
		obj = VTMNamedList([\aa,\myBB -> \bb,\cc]);

		//change unnamed item
		beforeChange = obj.getItemTimeLastChanged(3);
		obj.changeItem(3, \cccc);
		afterChange = obj.getItemTimeLastChanged(3);
		this.assertEquals(
			beforeChange, nil,
			"NamedList - item last time changed was nil upon init"
		);
		this.assert(
			afterChange.notNil and: {afterChange.isKindOf(Date)},
			"NamedList marked change time for changed item in the form of Date"
		);
		this.assertEquals(
			obj[3], \cccc,
			"NamedList changed item value"
		);
	}
}
