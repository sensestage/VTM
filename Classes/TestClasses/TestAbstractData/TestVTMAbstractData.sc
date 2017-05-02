TestVTMAbstractData : VTMUnitTest {

	*classesForTesting{
		^[
			VTMPreset,
			VTMCue,
			VTMMapping,
			VTMDefinitionLibrary,
			VTMCommand,
			VTMContextParameter,
			VTMRemoteNetworkNode,
			VTMModule,
			VTMApplication,
			VTMHardwareDevice,
			VTMScore,
			VTMScene
		];
	}

	*makeRandomAttributes{arg params;
		var result;
		this.findTestedClass.attributeKeys.do({arg item;
			result = result.addAll([item, this.makeRandomAttribute(item, params.at(item))]);
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
				testAttributes.as(IdentityDictionary).at(\name),
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

			// {
			// 	var testManager = testClass.makeRandomManagerObject;
			// 	var testAttr = testClass.makeRandomAttributes;
			// 	var testName = testClass.makeRandomSymbol;
			//
			// 	obj = class.new( testName, testAttr, testManager );
			// 	[
			// 		[\name, testName],
			// 		[\attributes, testAttr],
			// 		[\manager, testManager]
			// 	].do({arg items, i;
			// 		var method, variable;
			// 		#method, variable = items;
			// 		this.assertEquals(
			// 			obj.perform(method), variable,
			// 			"[%] - init '%' correctly".format(class, method)
			// 		);
			// 	});
			// }.value;

		});
	}
}