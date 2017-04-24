TestVTMAbstractData : VTMUnitTest {

	*classesForTesting{
		^[
			VTMPreset,
			VTMCue,
			VTMMapping,
			VTMDefinitionLibrary,
			VTMCommand,
			VTMContextParameter,
			VTMLocalNetworkNode,
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
			testAttributes = testClass.makeRandomAttributes;
			obj = class.newFromAttributes(
				testAttributes
			);

			//check if name initialized
			this.assertEquals(
				obj.name,
				testAttributes.at(\name),
				"[%] - init 'name' correctly".format(class)
			);

			obj.free;

		});
	}
}