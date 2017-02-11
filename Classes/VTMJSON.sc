VTMJSON : JSON {
	*stringifyAttributes { arg obj;
		var out;

		if(obj.isString, {
			^"<string> " ++ obj.asCompileString.reject(_.isControl).replace("\n", JSON.nl).replace("\t", JSON.tab);
		});
		if(obj.class === Symbol, {
			^"<symbol> %".format(obj.asString);
		});

		if(obj.isKindOf(Dictionary), {
			out = List.new;
			obj.keysValuesDo({ arg key, value;
				out.add( key.asString.asCompileString ++ ":" + VTMJSON.stringifyAttributes(value) );
			});
			^("{" ++ (out.join(", ")) ++ "}");
		});

		if(obj.isNil, {
			^"null"
		});
		if(obj === true, {
			^"<bool> true"
		});
		if(obj === false, {
			^"<bool> false"
		});
		if(obj.isNumber, {
			if(obj.isNaN, {
				^"null"
			});
			if(obj === inf, {
				^"null"
			});
			if(obj === (-inf), {
				^"null"
			});
			if(obj.isFloat, {
				//using angle bracket for denoting the data type, 64 bit float
				^"<float> %%".format(*[obj.high32Bits, obj.low32Bits].collect(_.asHexString));
			});
			if(obj.isInteger, {
				^"<int> %".format(obj);
			});

			^obj.asString
		});
		if(obj.isKindOf(SequenceableCollection), {
			^"[" ++ obj.collect({ arg sub;
				VTMJSON.stringifyAttributes(sub)
			}).join(", ")
			++ "]";
		});

		// obj.asDictionary -> key value all of its members

		// datetime
		// "2010-04-20T20:08:21.634121"
		// http://en.wikipedia.org/wiki/ISO_8601

		("No JSON conversion for object" + obj).warn;
		^VTMJSON.stringifyAttributes(obj.asCompileString)
	}

	*parseAttributesString{arg str;
		var result;
		result = str.parseYAML(str);
		result = result.changeScalarValuesToDataTypes;
		^result;
	}
}