{|test|
	var highBits, lowBits, testVal, shouldBe;

	//test 64-bit float parse
	#highBits, lowBits = { Integer.makeRandom32Bits; } ! 2;
	shouldBe = Float.from64Bits(highBits, lowBits);

	testVal = Dictionary[
		\mykey -> "<float64>%%".format(*[highBits, lowBits].collect(_.asHexString(8)))
	].changeScalarValuesToDataTypes[\mykey];

	test.assertEquals(
		testVal, shouldBe,
		"VTM extDictionary:changeScalarValuesToDataTypes parsed 64-bit float correctly"
	);

	//test 32-bit float parse
	lowBits = Integer.makeRandom32Bits;
	shouldBe = Float.from32Bits(lowBits);
	testVal = Dictionary[
		\mykey -> "<float32>%".format(lowBits.asHexString(8))
	].changeScalarValuesToDataTypes[\mykey];

	test.assertEquals(
		testVal, shouldBe,
		"VTM extDictionary:changeScalarValuesToDataTypes parsed 32-bit float correctly"
	);
}
