{|test|
	var highBits, lowBits, testVal, shouldBe;
	var makeRandomHexString = { arg bytes = 8;
		String.newFrom({"0123456789ABCDEF".choose} ! bytes);
	};
	var makeRandomBits = {arg numWords = 1;
		"0x%".format(String.newFrom(numWords.collect({makeRandomHexString.value}).flat )).interpret;
	};
	//test 64-bit float parse
	#highBits, lowBits = { makeRandomBits.value } ! 2;
	shouldBe = Float.from64Bits(highBits, lowBits);

	testVal = Dictionary[
		\mykey -> "<float64>%%".format(*[highBits, lowBits].collect(_.asHexString(8)))
	].changeScalarValuesToDataTypes[\mykey];

	test.assertEquals(
		testVal, shouldBe,
		"VTM extDictionary:changeScalarValuesToDataTypes parsed 64-bit float correctly"
	);

	//test 32-bit float parse
	lowBits = makeRandomBits.value;
	shouldBe = Float.from32Bits(lowBits);
	testVal = Dictionary[
		\mykey -> "<float32>%".format(lowBits.asHexString(8))
	].changeScalarValuesToDataTypes[\mykey];

	test.assertEquals(
		testVal, shouldBe,
		"VTM extDictionary:changeScalarValuesToDataTypes parsed 32-bit float correctly"
	);
}
