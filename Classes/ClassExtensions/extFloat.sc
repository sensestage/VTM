+ Float {
	*makeRandom64Bits{
		^Float.from64Bits(*{Integer.makeRandom32Bits} ! 2);
	}

	*makeRandom32Bits{
		^Float.from32Bits(Integer.makeRandom32Bits);
	}
}