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

	*makeRandomAttributes{
		var result = IdentityDictionary.new;
		VTMAbstractData.attributeKeys.do({arg item;
			result.put( item, this.makeRandomAttribute(item) );
		});
		^result;
	}

	*makeRandomAttribute{arg key, params;
		var result;
		if(result.isNil, {
			switch(key,
				\name, { result = this.makeRandomString(params); }
			);
		});
		^result;
	}

	test_initAbstractData{
		var obj, testAttributes, managerObj;
		this.class.classesForTesting.do({arg class;
			var testClass = VTMUnitTest.findTestClass(class);
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
			obj = class.newFromAttributes(
				testAttributes,
				managerObj
			);

			//check if name initialized
			this.assertEquals(
				obj.name,
				testAttributes.at(\name),
				"[%] - init 'name' correctly".format(class)
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
}