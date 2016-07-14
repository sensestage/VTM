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

	// *makeRandomParameterOfType{arg type;
	// 	var result;
	// 	switch(
	// 		\string, {
	// 			result = VTMStringParameter.new('myString', (
	// 				pattern:
	// 			));
	// 		},
	// 		\timecode, {},
	// 		\integer, {},
	// 		\decimal, {},
	// 		\boolean, {}
	// 	);
	// }

}
