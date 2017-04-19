TestVTMAbstractDataManager : VTMUnitTest {

	*dataClass{ ^this.findTestedClass.dataClass; }

	*dataTestClass{ ^this.findTestClass(this.dataClass);}


	//settings: minItems, maxItems
	//if attributes are defined the settings are overriden, and the
	//attributes array size defines the number of data item attributes
	//that are generated.

	*makeRandomAttributes{arg settings ...args;
		var result;
		var numItems;
		if(settings.isNil, {
			numItems = rrand(1,7);
		}, {
			var minItems = settings[\minItems] ? 1;
			var maxItems = settings[\maxItems] ? 7;
			numItems = rrand(minItems, maxItems);
		});
		result = numItems.collect({arg i;
			[
				[i + 1, {this.makeRandomString}].choose.value,
				this.dataTestClass.makeRandomAttributes(*args)
			]
		}).flatten;
		^result;
	}
}