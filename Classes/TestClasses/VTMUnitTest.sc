VTMUnitTest : UnitTest {
	classvar <>reportAllErrors = false;

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


	run { | reset = true, report = true, reportAllErrors = false|
		var function;
		if(reset) { this.class.reset };
		if(report) { ("RUNNING UNIT TEST" + this).inform };
		this.class.forkIfNeeded {
			this.findTestMethods.do { |method|
				this.setUp;
				currentMethod = method;
				if(this.class.reportAllErrors, {
					try{
						this.perform(method.name);
					} {|err|
						this.failed(method,
							"ERROR: during test: \n\t%".format(err.errorString)
						)
					};
				}, {
					this.perform(method.name);
				});
				//{

				// unfortunately this removes the interesting part of the call stack
				//}.try({ |err|
				//	("ERROR during test"+method).postln;
				//	err.throw;
				//});

				this.tearDown;
			};
			if(report) { this.class.report };
			nil
		};
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
