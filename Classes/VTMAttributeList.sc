VTMAttributeList {
	var keyOrder;
	var dict;

	*new{arg keyValPairs;
		^super.new.init(keyValPairs);
	}

	init{arg keyValPairs;
		if(keyValPairs.notNil, {
			keyOrder = keyValPairs.clump(2).flop.first;
			dict = IdentityDictionary.newFrom(keyValPairs);
		});
	}

	keys{
		^dict.keys;
	}

}
