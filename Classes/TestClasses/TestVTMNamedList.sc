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
		var testList = [\aa, \bb, \cc];
		var is, shouldBe;
		obj = VTMNamedList(testList, [1 -> \myAA, 3 -> 'myCC']);
		//check order of values
		this.assertEquals(
			obj.getItems, testList,
			"ListEnumerator returned the correct items."
		);

		//check order of values
		this.assertEquals(
			obj.names, [\myAA, 2, \myCC],
			"ListEnumerator returned the correct names."
		);

		//check associations array
		is = obj.associations;
		shouldBe = ['myAA' -> \aa, 2 -> \bb, 'myCC' -> \cc];

		this.assert(
			this.class.equalAssociations(
				is, shouldBe),
			"ListEnumerator returned the correct associations." ++
			"\nIs:\n\t" + is + "\nShould be:\n\t" + shouldBe + "\n"
		);
	}

	test_AddRemoveItems{
		var obj;
		var is, shouldBe;
		var testList = [\aa, \bb, \cc];
		obj = VTMNamedList(testList, [1 -> \myAA, 3 -> 'myCC']);

		//should add item to end without name
		obj.addItem(\dd);
		is = obj.associations;
		shouldBe = [
			\myAA -> \aa, 2 -> \bb, \myCC -> \cc, 4 -> \dd
		];
		this.assert(
			this.class.equalAssociations(is, shouldBe),
			"ListEnumerator should add item to end without name." ++
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
			"ListEnumerator should add item to third slot without name." ++
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
			"ListEnumerator should add named item to first slot." ++
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
			"ListEnumerator should remove the second item." ++
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
			"ListEnumerator should remove named item." ++
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
			"ListEnumerator should move named item to third slot." ++
			"\nIs:\n\t" + is + "\nShould be:\n\t" + shouldBe + "\n"
		);
	}
}
