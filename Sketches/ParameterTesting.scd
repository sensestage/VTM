a = VTMUnitTest.runAll
a.failures[0]

"hei".asCompileString.drop(1).drop(-1)

VTMUnitTest.runTest("TestVTMParameter:test_GetAttributes");

VTMUnitTest.runTest("TestVTMContext:test_OSCCommunication");


VTMUnitTest.makeRandomString((noNumbers: true))
TestVTMValueParameter.makeRandomString((noNumbers: true))
TestVTMBooleanParameter.makeRandomEnum

VTMUnitTest.runTestForClass(VTMParameter, true);
VTMUnitTest.runTestForClass(VTMContext, true);

VTMUnitTest.reportAllErrors = true;

Platform.defaultTempDir


VTMUnitTest.runTest("TestVTMValueParameter:test_SetVariablesFromAttributes");



VTMUnitTest.makeRandomString()
VTMUnitTest.makeRandomString((minLength: 30, maxLength: 50, noNumbers: true))


(
var attributes, response;

var	paramA = ~attributes;
var	paramB = ~response;
"PARAMETERS EQUALITY: %".format(~attributes == ~response).postln;
paramA.keysValuesDo({arg key, val;
	var match;
	if(paramA[key].isKindOf(Float), {
		match = paramA[key].equalWithPrecision( paramB[key] );
	}, {
		match = paramA[key] == paramB[key];
	});
	//only print the unmatched ones
	if(match.not, {
		if(val.isKindOf(Array), {
			"\t% - %\n\t\tAttributes[%]:\t%\n\t\tResponse[%]:\t%".format(
				key,
				match,
				val.class, val.collect({arg item, i; [item, item.class.asSymbol, item == paramB[key][i]]}),
				paramB[key].class, paramB[key].collect({arg item, i; [item, item.class.asSymbol, item == val[i]]})
			).postln;
		}, {
			"\t% - %\n\t\ttAttributes[%]:\t%\n\t\tResponse[%]:\t%".format(
				key,
				match,
				val.class, val,
				paramB[key].class, paramB[key]
			).postln;
		});
	});
});
)




~response == ~attributes
~response[\parameters] == ~attributes[\parameters]
~response[\children] == ~attributes[\children]
~response[\children] == ~attributes[\children]
a = ~json.asString.parseYAML;
b = a.changeScalarValuesToDataTypes;
a["children"][0].class
b['children'][0].class


a["parameters"]["V"]["name"].class
b['parameters']['DGzXuFZUBER']['name'].class


a["parameters"]["DGzXuFZUBER"]["defaultValue"].class
b['parameters']['DGzXuFZUBER']['defaultValue'].class


(
var attributes, response;
"CHILDREN EQUALITY: %".format(~attributes[\children] == ~response[\children]).postln;
"\tAttributes: %".format([~attributes[\children], ~attributes[\children].collect(_.class)].lace).postln;
"\tResponse: %".format([~response[\children], ~response[\children].collect(_.class)].lace).postln;
"PARAMETERS EQUALITY: %".format(~attributes[\parameters] == ~response[\parameters]).postln;
~response[\parameters].do({arg paramA, i;
	var paramB, paramName;
	// paramA = ~attributes[\parameters][i];
	paramB = ~response[\parameters][i];
	paramName = paramA[\name];
	"%: (type: A: % B: % match: %)".format(paramName, paramA[\type], paramB[\type], paramA == paramB).postln;
	paramA.keysValuesDo({arg key, val;
		var match;
		if(paramA[key].isKindOf(Float), {
			match = paramA[key].equalWithPrecision( paramB[key] );
			}, {
				match = paramA[key] == paramB[key];
		});
		// only print the unmatched ones
		if(match.not, {
			if(val.isKindOf(Array), {
				"\t% - %\n\t\tAttributes[%]:\t%\n\t\tResponse[%]:\t%".format(
					key,
					match,
					val.class, val.collect({arg item, i; [item, item.class.asSymbol, item == paramB[key][i]]}),
					paramB[key].class, paramB[key].collect({arg item, i; [item, item.class.asSymbol, item == val[i]]})
				).postln;
				}, {
					"\t% - %\n\t\tAttributes[%]:\t%\n\t\tResponse[%]:\t%".format(
						key,
						match,
						val.class, val,
						paramB[key].class, paramB[key]
					).postln;
			});
		});
	});
});
)


(
~paramTest.keysValuesDo({arg paramName, data;
	var attributes, attributes;
	attributes = data['attributes'];
	attributes = data['attributes'];
	attributes.keysValuesDo({arg key, value;
		var match = value == attributes[key];
		if(match.not, {
			"MISMATCH: % - %\n\tAttributes: %[%]\n\tAttributes: %[%]".format(
				paramName, key,
				value, value.class,
				attributes[key], attributes[key].class
			).postln;
		});
	});
});
)

(
r = OSCFunc({arg msg, time, addr, port;
	["AAAA", msg[1].class, msg[1]].postln;
}, '/test');
)

NetAddr.localAddr.sendMsg('/test', "oijerogijer 444 44 4")