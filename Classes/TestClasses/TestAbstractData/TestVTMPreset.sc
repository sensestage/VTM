TestVTMPreset : TestVTMAbstractData {

	*makeRandomAttribute{arg key, params;
		var result;
		result = super.makeRandomAttribute(key, params);
		switch(key,
			\values, {
				var parameterAttributesArray = params;
				if(parameterAttributesArray.isNil, {
					[
						\boolean,
						\timecode,
						\string,
						\integer,
						\decimal,
						\dictionary,
						\list,
						\array,
						\schema,
						\tuple
					].do({arg paramType;
						result = result.addAll([
							this.makeRandomSymbol,
							TestVTMValue.testclassForType(paramType).makeRandomValue
						]);
					});
				}, {
					parameterAttributesArray.do({arg parameterAttribute;
						if(parameterAttribute.isKindOf(Dictionary).not, {
							parameterAttribute = (
								name: this.makeRandomSymbol,
								type: parameterAttribute
							);
						});
						result = result.addAll([
							parameterAttribute[\name] ? this.makeRandomSymbol,
							TestVTMValue.testclassForType(parameterAttribute[\type]).makeRandomValue(
								parameterAttribute
							)
						]);
					});
				});
			}
		);
		^result;
	}
}