+ Dictionary {
	//converts all string values from string to either Integer, Float or Boolean
	//this is practical when importing YAML and JSON files with parseYAML
	//uses its own type angle bracket notation , since the YAML-cpp
	//lib swallows the type tags before it enters sclang.
	changeScalarValuesToDataTypes {
		var changeValue, tempdict;

		changeValue = {|key,val|
			var result = val;
			if(val.isString, {
				result = VTMJSON.parseYAMLValue(val);
			}, {
				if(val.isCollection, {
					if(val.isKindOf(Dictionary), {
						result = val.keysValuesChange(changeValue);
					}, {
						result = val.collect({|item|
							changeValue.value(nil, item);
						});
					});
				});
			});
			result;
		};
		^this.deepCopy.keysValuesChange(changeValue).asIdentityDictionaryWithSymbolKeys;
	}
	asIdentityDictionaryWithSymbolKeys {
		var d = IdentityDictionary();
		var maybeRecurseValue = {|val|
			if(val.isCollection) {
				if(val.isKindOf(Dictionary)) {
					val.asIdentityDictionaryWithSymbolKeys;
				} {
					val.collect(maybeRecurseValue);
				};
			} {
				val;
			};
		};
		this.pairsDo {|key,val|
			d[key.asSymbol] = maybeRecurseValue.value(val);
		};
		^d;
	}
}