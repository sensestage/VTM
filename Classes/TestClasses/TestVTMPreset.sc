TestVTMPreset : VTMUnitTest {

	*makeRandomAttribute{arg key, params;
		var result;
		result = super.makeRandomAttribute(key, params);
		switch(key,
			\values, {
				var parameterDeclarationArray = params;
				if(parameterDeclarationArray.isNil, {
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
					parameterDeclarationArray.do({arg parameterDeclaration;
						if(parameterDeclaration.isKindOf(Dictionary).not, {
							parameterDeclaration = (
								name: this.makeRandomSymbol,
								type: parameterDeclaration
							);
						});
						result = result.addAll([
							parameterDeclaration[\name] ? this.makeRandomSymbol,
							TestVTMValue.testclassForType(parameterDeclaration[\type]).makeRandomValue(
								parameterDeclaration
							)
						]);
					});
				});
			}
		);
		^result;
	}
}
