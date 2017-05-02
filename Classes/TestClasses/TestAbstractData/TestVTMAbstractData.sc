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
		var result = [];
		this.findTestedClass.attributeKeys.do({arg item;
			var attrParams;
			if(params.notNil and: {params.includesKey(item)}, {
				attrParams = params.at(item);
			});
			result = result.addAll([item, this.makeRandomAttribute(item, attrParams)]);
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
}