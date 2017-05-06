TestVTMMapping : TestVTMElement {
	*makeRandomAttribute{arg key, params;
		var result;
		result = super.makeRandomAttribute(key, params);
		result = switch(key,
			\source, { this.makeRandomPath; },
			\destination, { this.makeRandomPath; },
			\when, {this.makeRandomString},//TODO: Change this to whatever we use to express conditionals
			\settings, {this.makeRandomDictionary;}
		);
		^result;
	}
}