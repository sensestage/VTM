TestVTMAbstractData : VTMUnitTest {

	*classesForTesting{
		^[
			VTMCue,
			VTMMapping,
			VTMDefinitionLibrary,
			VTMCommand,
			VTMParameter,
			VTMQuery,
			VTMRemoteNetworkNode,
			VTMModule,
			VTMApplication,
			VTMHardwareDevice,
			VTMScore,
			VTMScene
		];
	}

	*makeRandomDeclaration{arg params, makeNameAttribute = false;
		var result = VTMDeclaration[];
		if(makeNameAttribute, {
			result.put(\name, this.makeRandomAttribute(\name));
		});
		this.findTestedClass.declarationKeys.do({arg item;
			var attrParams, randAttr;
			if(params.notNil and: {params.includesKey(item)}, {
				attrParams = params.at(item);
			});
			randAttr = this.makeRandomAttribute(item, attrParams);
			if(randAttr.notNil, {
				result.put(item, randAttr);
			});
		});
		^result;
	}

	*makeRandomDeclarationForObject{arg object;
		var testClass, class;
		var result = VTMDeclaration[];
		class = object.class;
		testClass = this.findTestClass(class);

		//omit name
		object.declaration.keysValuesDo({arg attrKey, attrVal;
			"Object attr '%' - %".format(attrKey, attrVal).postln;
		});
	}

	*makeRandomAttribute{arg key, params;
		var result;
		result = switch(key,
			\name, {this.makeRandomSymbol},
		);
		^result;
	}

	*makeRandomManagerObject{
		var result;
		var managerClass;
		managerClass = this.findTestedClass.managerClass;

		result = managerClass.new();
		^result;
	}

	test_initAbstractData{
		var obj, testDeclaration, managerObj;
		this.class.classesForTesting.do({arg class;
			var testClass = VTMUnitTest.findTestClass(class);
			var testName = VTMUnitTest.makeRandomSymbol;
			var managerClass = class.managerClass;
			//managerClass shouldn not be nil
			this.assert(managerClass.notNil,
				"[%] - found manager class for test class".format(class)
			);
			managerObj = managerClass.new;
			this.assert(managerObj.notNil,
				"[%] - made manager obj for test class".format(class)
			);

			testDeclaration = testClass.makeRandomDeclaration;
			// obj = class.new(
			// 	testName,
			// 	testDeclaration,
			// 	managerObj
			// );
			//
			// //check if name initialized
			// this.assertEquals(
			// 	obj.name,
			// 	testName,
			// 	"[%] - init 'name' correctly".format(class)
			// );
			//
			// //check declaration equal
			// this.assertEquals(
			// 	obj.declaration,
			// 	testDeclaration,
			// 	"[%] - init 'declaration' correctly".format(class)
			// );
			//
			// //the manager object must be identical
			// this.assert(
			// 	obj.manager === managerObj,
			// 	"[%] - init 'manager' correctly".format(class)
			// );
			//
			// obj.free;
			// managerObj.free;
		});
	}

	test_declarationNil{
		var obj, testDeclaration, managerObj;
		this.class.classesForTesting.do({arg class;
			var testClass = VTMUnitTest.findTestClass(class);
			var testName = VTMUnitTest.makeRandomSymbol;
			var managerClass = class.managerClass;

			managerObj = managerClass.new;

			testDeclaration = nil;
			obj = class.new(
				testName,
				testDeclaration,
				managerObj
			);

			//declaration should be empty VTMDeclaration
			this.assertEquals(
				obj.declaration,
				VTMDeclaration[],
				"[%] - init nil 'declaration' to empty array".format(class)
			);

			obj.free;
			managerObj.free;

		});
	}

	test_DefaultDeclaration{

	}

	test_DeclarationSetGet{
		var obj;
		this.class.classesForTesting.do({arg class;
			var testClass = VTMUnitTest.findTestClass(class);
			var testName = VTMUnitTest.makeRandomSymbol;
			var appendString;

			//Should work with both initlialized uninitialized(nil) declaration.
			[
				[testClass.makeRandomDeclaration,	"[pre-initialized declaration]"],
				[nil,	"[declaration init to nil]"],
			].do({arg items, i;
				var testDeclaration, appendString;
				#testDeclaration, appendString = items;
				//All classes should implement set and get methods for
				//every declaration.
				testDeclaration = nil;
				obj = class.new(
					testName,
					testDeclaration
				);

				class.declarationKeys.do({arg attrKey;
					var testVal;
					//does it respond to getter and setters for every declaration?
					this.assert(
						obj.respondsTo(attrKey),//test getter
						"[%] - responded to declaration getter '%'".format(
							class, attrKey) ++ appendString
					);
					this.assert(
						obj.respondsTo(attrKey.asSetter),//test getter
						"[%] - responded to declaration setter '%'".format(
							class, attrKey.asSetter) ++ appendString
					);

					//check if test class has implemented random generation method for it
					try{
						testVal = testClass.makeRandomAttribute(attrKey);
					} {|err|
						this.failed(thisMethod,
							Error("[%] - Error making random declaration value for '%'".format(class, attrKey)).throw;
						);
					};
					this.assert(
						testVal.notNil,
						"[%] - test class generated non-nil random value for attr '%'".format(
							class, attrKey) ++ appendString
					);

					//test setting declaration value
					obj.set(attrKey, testVal);
					this.assertEquals(
						obj.get(attrKey),
						testVal,
						"[%] - setting and getting declaration '%' worked".format(
							class, attrKey) ++ appendString
					);
				});
				obj.free;
			});
		});
	}
}
