TestVTMMapping : TestVTMElement {
	*makeRandomParameter{arg key, params;
		var result;
		result = super.makeRandomParameter(key, params);
		result = switch(key,
			\source, { this.makeRandomPath; },
			\destination, { this.makeRandomPath; },
			\when, {this.makeRandomString},//TODO: Change this to whatever we use to express conditionals
			\settings, {this.makeRandomDictionary((classSymbol: \Dictionary));}
		);
		^result;
	}
}
