VTMAttributeList {
	var dict;

	*new{arg dict;
		^super.new.init(dict);
	}

	init{arg dict_;
		dict = dict_;
	}

	keys{
		^dict.keys;
	}

}