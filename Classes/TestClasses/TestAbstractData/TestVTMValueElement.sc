TestVTMValueElement : TestVTMElement {
	//this method overides the superclass because wee need to define
	//a type for the ValueElement class to be able to generate random
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
		^result;
	}


}