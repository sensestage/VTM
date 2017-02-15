TestVTMContextParameterManager : VTMUnitTest {

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

	*makeRandomPresetForContext{arg context;
		var result = IdentityDictionary.new;
		context.parameters.do({arg paramName;
			var param;
			param = context.getParameter(paramName);
			result.put(paramName, TestVTMParameter.testclassForType(param.type).makeRandomValue);
		});
		^result.asKeyValuePairs;
	}

	*makeRandomPresetAttributesForContext{arg context;
		var result = IdentityDictionary.new;
		rrand(3,8).do{arg i;
			result.put(this.makeRandomString, this.makeRandomPresetForContext(context));
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
		var context, testPresets;
		var testPresetNames;
		context = TestVTMContext.makeRandomContext;
		context.prepare;

		//make some random presets
		testPresets = this.class.makeRandomPresetAttributesForContext(context);
		testPresetNames = testPresets.clump(2).flop[0];

		testPresets.pairsDo({arg presetName, presetData;
			context.addPreset(presetData, presetName);
		});

		this.assertEquals(
			context.presets, testPresetNames,
			"ContextParameterManager added preset names correctly"
		);

		this.assertEquals(
			context.presetAttributes, testPresets,
			"ContextParameterManager returned preset attributes correctly"
		);

		context.free;
	}

	test_InitContextWithPresets{}


}