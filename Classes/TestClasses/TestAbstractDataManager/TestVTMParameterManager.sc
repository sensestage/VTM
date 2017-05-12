TestVTMParameterManager : TestVTMAbstractDataManager {

	*makeRandomPresetForParameterAttributes{arg attributes;
		var result = IdentityDictionary.new;
		attributes.do({arg attr;
			result.put(
				attr[\name],
				TestVTMParameter.testclassForType(attr[\type]).makeRandomValue
			);
		});
		^result.asKeyValuePairs;
	}

	//make one of each type for testing
	*makeTestAttributes{
		^TestVTMParameter.testTypes.collect{arg item;
			TestVTMParameter.makeRandomAttributes(item)
		};
	}

	*makeRandomPresetForElement{arg element;
		var result = IdentityDictionary.new;
		element.parameters.do({arg paramName;
			var param;
			param = element.getParameter(paramName);
			result.put(paramName, TestVTMParameter.testclassForType(param.type).makeRandomValue);
		});
		^result.asKeyValuePairs;
	}

	*makeRandomPresetAttributesForElement{arg element;
		var result = IdentityDictionary.new;
		rrand(3,8).do{arg i;
			result.put(this.makeRandomString, this.makeRandomPresetForElement(element));
		};
		^result.asKeyValuePairs;
	}

	*makeRandomPresetAttributesForParameterAttributes{arg attributes;
		var result = IdentityDictionary.new;
		rrand(3,8).do{arg i;
			result.put(this.makeRandomString, this.makeRandomPresetForParameterAttributes(attributes));
		};
		^result.asKeyValuePairs;
	}


	test_AddingAndRemovingPresets{
		var element, testPresets;
		var testPresetNames;
		element = TestVTMElement.makeRandomElement;
		element.prepare;

		//make some random presets
		testPresets = this.class.makeRandomPresetAttributesForElement(element);
		testPresetNames = testPresets.clump(2).flop[0];

		testPresets.pairsDo({arg presetName, presetData;
			element.addPreset(presetData, presetName);
		});

		this.assertEquals(
			element.presets, testPresetNames,
			"ParameterManager added preset names correctly"
		);

		this.assertEquals(
			element.presetAttributes, testPresets,
			"ParameterManager returned preset attributes correctly"
		);

		element.free;
	}

	test_InitElementWithPresets{}


}