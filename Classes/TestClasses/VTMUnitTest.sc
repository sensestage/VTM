VTMUnitTest : UnitTest {
	classvar <>reportAllErrors = true;
	classvar <>checkFreeingResponderFuncs = true;
	classvar <>forceFreeingResponderFuncs = true;

	*testclassForType{arg val;
		^"TestVTM%Parameter".format(val.asString.capitalize).asSymbol.asClass;
	}

	*runAll{arg runScripts = true;
		[
			VTMParameter,
			VTMContext,
			VTMNamedList
		].do({arg cl;
			this.runTestForClass(cl, recursive: true);
		});
		if(runScripts, {
			VTMUnitTestScript.findTestScripts;
			VTMUnitTestScript.runAll;
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

		//This checks if any Responders have not been removed.
		if(this.class.checkFreeingResponderFuncs, {
			if(AbstractResponderFunc.allFuncProxies.isEmpty.not, {
				this.failed(currentMethod,
					"Some AbstractResponderFunc proxies were not freed after test."
				);
				if(this.class.forceFreeingResponderFuncs, {
					AbstractResponderFunc.allFuncProxies.do({arg funcs;
						funcs.do(_.free)
					});
				});
			});
		});
	}


	run { | reset = true, report = true|
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

				this.tearDown;
			};
			if(report) { this.class.report };
			nil
		};
	}

	*makeRandomSymbol{arg params;
		^this.makeRandomString(params).asSymbol;
	}

	*makeRandomString{arg params;
		var chars;
		var minLength, maxLength, noSpaces, noNumbers, noAlphas, onlyAlphaNumeric;
		minLength = 1;
		maxLength = 15;
		noSpaces = true;
		noAlphas = false;
		noNumbers = false;
		onlyAlphaNumeric = true;

		if(params.notNil and: { params.isKindOf(Dictionary) }, {
			minLength = params[\minLength] ? minLength;
			maxLength = params[\maxLength] ? maxLength;
			noSpaces = params[\noSpaces] ? noSpaces;
			noAlphas = params[\noAlphas] ? noAlphas;
			noNumbers = params[\noNumbers] ? noNumbers;
			onlyAlphaNumeric = params[\onlyAlphaNumeric] ? onlyAlphaNumeric;
		});

		chars = (0..127).collect(_.asAscii);

		if(onlyAlphaNumeric, {
			chars = chars.select({arg it; it.isAlphaNum || (it == Char.space); });
		});
		if(noSpaces, {
			chars = chars.reject({arg it; it == Char.space; });
		});
		if(noNumbers, {
			chars = chars.reject({arg it; it.isDecDigit; });
		});
		if(noAlphas, {
			chars = chars.reject({arg it; it.isAlpha; });
		});
		^String.newFrom(
			rrand(minLength, maxLength).collect({
				chars.choose;
			})
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
