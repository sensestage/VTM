TestVTMAbstractData : VTMUnitTest {

	*classesForTesting{
		^[
			// VTMPreset,
			// VTMCue,
			VTMMapping,
			// VTMDefinitionLibrary,
			// VTMCommand,
			// VTMContextParameter,
			// VTMRemoteNetworkNode,
			// VTMModule,
			// VTMApplication,
			// VTMHardwareDevice,
			// VTMScore,
			// VTMScene
		];
	}

	*makeRandomAttributes{arg params;
		var result = [];
		this.findTestedClass.attributeKeys.do({arg item;
			var attrParams, randAttr;
			if(params.notNil and: {params.includesKey(item)}, {
				attrParams = params.at(item);
			});
			randAttr = this.makeRandomAttribute(item, attrParams);
			if(randAttr.notNil, {
				result = result.addAll([item, randAttr]);
			});
		});
		^result;
	}

	*makeRandomAttribute{arg key, params;
		^nil;
	}

	*makeRandomManagerObject{
		var result;
		var managerClass;
		managerClass = this.findTestedClass.managerClass;

		result = managerClass.new();
		^result;
	}

	test_initAbstractData{
		var obj, testAttributes, managerObj;
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

			testAttributes = testClass.makeRandomAttributes;
			obj = class.new(
				testName,
				testAttributes,
				managerObj
			);

			//check if name initialized
			this.assertEquals(
				obj.name,
				testName,
				"[%] - init 'name' correctly".format(class)
			);

			//check attributes equal
			this.assertEquals(
				obj.attributes,
				testAttributes,
				"[%] - init 'attributes' correctly".format(class)
			);

			//the manager object must be identical
			this.assert(
				obj.manager === managerObj,
				"[%] - init 'manager' correctly".format(class)
			);

			obj.free;
			managerObj.free;
		});
	}

	test_attributesNil{
		var obj, testAttributes, managerObj;
		this.class.classesForTesting.do({arg class;
			var testClass = VTMUnitTest.findTestClass(class);
			var testName = VTMUnitTest.makeRandomSymbol;
			var managerClass = class.managerClass;

			managerObj = managerClass.new;

			testAttributes = nil;
			obj = class.new(
				testName,
				testAttributes,
				managerObj
			);

			//attributes should be empty array
			this.assertEquals(
				obj.attributes,
				[],
				"[%] - init nil 'attributes' to empty array".format(class)
			);

			obj.free;
			managerObj.free;

		});
	}

	test_DefaultAttributes{

	}

	test_AttributesSetGet{
		var obj;
		this.class.classesForTesting.do({arg class;
			var testClass = VTMUnitTest.findTestClass(class);
			var testName = VTMUnitTest.makeRandomSymbol;
			var appendString;

			//Should work with both initlialized uninitialized(nil) attributes.
			[
				[testClass.makeRandomAttributes,	"[pre-initialized attributes]"],
				[nil,	"[attributes init to nil]"],
			].do({arg items, i;
				var testAttributes, appendString;
				#testAttributes, appendString = items;
				//All classes should implement set and get methods for
				//every attribute.
				testAttributes = nil;
				obj = class.new(
					testName,
					testAttributes
				);

				class.attributeKeys.do({arg attrKey;
					var testVal;
					//does it respond to getter and setters for every attribute?
					this.assert(
						obj.respondsTo(attrKey),//test getter
						"[%] - responded to attribute getter '%'".format(
							class, attrKey) ++ appendString
					);
					this.assert(
						obj.respondsTo(attrKey.asSetter),//test getter
						"[%] - responded to attribute setter '%'".format(
							class, attrKey.asSetter) ++ appendString
					);

					//check if test class has implemented random generation method for it
					try{
						testVal = testClass.makeRandomAttribute(attrKey);
					} {|err|
						this.failed(thisMethod,
							Error("[%] - Error making random attribute value for '%'".format(class, attrKey)).throw;
						);
					};
					this.assert(
						testVal.notNil,
						"[%] - test class generated non-nil random value for attr '%'".format(
							class, attrKey) ++ appendString
					);

					//test setting attribute value
					obj.set(attrKey, testVal);
					this.assertEquals(
						obj.get(attrKey),
						testVal,
						"[%] - setting and getting attribute '%' worked".format(
							class, attrKey) ++ appendString
					);
				});
				obj.free;
			});
		});
	}
}