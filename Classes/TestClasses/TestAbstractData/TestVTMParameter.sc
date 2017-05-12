TestVTMParameter : TestVTMElement {

	//this method overides the superclass because wee need to define
	//a type for the Parameter clas to be able to generate random
	//attributes.
	*makeRandomAttributes{arg params, makeNameAttribute = false;
		var result = VTMAttributes[];
		if(makeNameAttribute, {
			result.put(\name, this.makeRandomAttribute(\name));
		});
		//use random value type if not defined
		if(params.notNil and: {params.includesKey(\type)}, {
			result.put(\type, params[\type]);
		}, {
			result.put(\type, TestVTMValue.classesForTesting.collect(_.type).choose);
		});

		this.findTestedClass.attributeKeys.do({arg item;
			var attrParams, randAttr;
			if(params.notNil and: {params.includesKey(item)}, {
				attrParams = params.at(item);
			});
			randAttr = this.makeRandomAttribute(item, attrParams, result[\type]);
			if(randAttr.notNil, {
				result.put(item, randAttr);
			});
		});
		^result;
	}


	*makeRandomAttribute{arg key, params, valueType;
		var valueTestClass = this.findTestClass(VTMValue.typeToClass(valueType));

		var result = valueTestClass.makeRandomAttribute(key, params);
		// result = switch(key,
		// 	\minVal, { valueTestClass.makeRandomAttribute(\minVal, params); },
		// 	\maxVal, { valueTestClass.makeRandomAttribute(\maxVal, params); },
		// 	\stepsize, { valueTestClass.makeRandomAttribute(\stepsize, params); },
		// 	\pattern, { valueTestClass.makeRandomAttribute(\pattern, params); },
		// 	\matchPattern, { valueTestClass.makeRandomAttribute(\matchPattern, params); },
		// 	\enum, { valueTestClass.makeRandomAttribute(\enum, params); },
		// 	\enabled, { valueTestClass.makeRandomAttribute(\enabled, params); },
		// 	\value, { valueTestClass.makeRandomAttribute(\value, params); },
		// 	\restrictValueToEnum, { valueTestClass.makeRandomAttribute(\restrictValueToEnum, params); },
		// 	\clipmode, { valueTestClass.makeRandomAttribute(\clipmode, params); }
		// );
		^result;
	}

}