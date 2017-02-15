TestVTMAbstractDataManager : VTMUnitTest {

	*dataClass{ ^this.findTestedClass.dataClass; }

	*makeRandomAttributes{arg settings, attributes;
		if(attributes.notNil, {
			attributes.collect({arg attr;
				this.dataClass.makeRandomAttributes(attr);
			});
		}, {
			var numItems;
			if(settings.isNil, {
				numItems = rrand(1,7);
			}, {
				var minItems = settings[\minItems] ? 1;
				var maxItems = settings[\maxItems] ? 7;
				numItems = rrand(minItems, maxItems);
			});
			numItems.do({arg i;
				this.dataClass.makeRandomAttributes;
			});

		});
		^rrand(1,7).collect({arg i;
			[
				[i + 1, this.makeRandomString].choose,
				this.makeRandomData(attributes);
			]
		}).flatten;
	}

	*makeRandomData{arg attributes;
		this.subclassResponsibility(thisMethod);
	}
}