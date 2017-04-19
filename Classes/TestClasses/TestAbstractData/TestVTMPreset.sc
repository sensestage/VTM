TestVTMPreset : TestVTMAbstractData {

	*makeRandomData{arg parameterAttributes;
		^parameterAttributes.collect({arg parameterAttribute;
			[
				parameterAttribute[\name],
				TestVTMParameter.testclassForType(parameterAttribute[\type]).makeRandomValue(
					parameterAttribute
				)
			]
		}).flatten;
	}
}