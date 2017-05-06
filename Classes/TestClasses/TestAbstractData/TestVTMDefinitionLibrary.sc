TestVTMDefinitionLibrary : TestVTMElement {
	*makeRandomAttribute{arg key, params;
		var result;
		result = super.makeRandomAttribute(key, params);
		result = switch(key,
			\includedPaths, { { this.makeRandomPath } ! rrand(1,7)},
			\excludedPaths, { { this.makeRandomPath } ! rrand(1,7)}
		);
		^result;
	}

}