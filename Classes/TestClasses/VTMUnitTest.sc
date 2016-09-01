VTMUnitTest : UnitTest {

	*testclassForType{arg val;
		^"TestVTM%Parameter".format(val.asString.capitalize).asSymbol.asClass;
	}

	*runAll{
		[
			VTMParameter,
			VTMContext
		].do({arg cl;
			this.runTestForClass(cl, recursive: true);
		});
	}

	*runTestForClass{arg what, recursive = false;
		var testClasses;
		testClasses = what.asArray;

		if(recursive, {
			what.allSubclasses.do({arg subclass;
				testClasses = testClasses.add(subclass);
			});
		});
		testClasses.do({arg testClass;
			super.runTestClassForClass(testClass);
		})
	}

	setUp{
		"Setting up a VTMTest".postln;
	}

	tearDown{
		"Tearing down a VTMTest".postln;
	}

	*makeRandomString{arg params;
		var minLength, maxLength, makeSpaces;
		if(params.notNil and: { params.isKindOf(Dictionary) }, {
			minLength = params[\minLength] ? 1;
			maxLength = params[\maxLength] ? 15;
			makeSpaces = params[\makeSpaces] ? false;
		}, {
			minLength = 1; maxLength = 15; makeSpaces = false;
		});
		^String.newFrom(
			rrand(minLength, maxLength).collect({
				(0..127).collect(_.asAscii).select({arg it;
					var v;
					if(makeSpaces, {
						v = it.isAlphaNum || (it == Char.space);
					}, {
						v = it.isAlphaNum;
					});
					v;
				}).choose;
			});
		);
	}

	*makeRandomBoolean {arg params;
		var chance = 0.5;
		if(params.notNil and: {params.isKindOf(Dictionary)}, {
			chance = params[\chance] ? 0.5;
		});
		^chance.coin;
	}

	*makeRandomInteger{arg params;
		^this.makeRandomDecimal(params).asInteger;
	}

	*makeRandomDecimal{arg params;
		var minVal = -2147483648.0;//32 bits random
		var maxVal = 2147483647.0;
		if(params.notNil, {
			minVal = params[\minVal] ? minVal;
			maxVal = params[\maxVal] ? maxVal;
		});
		^rrand(minVal, maxVal);
	}

}
