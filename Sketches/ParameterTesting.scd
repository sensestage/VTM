a = VTMUnitTest.runAll
a.failures[0]

VTMUnitTest.runTest("TestVTMParameter:test_GetAttributes");

VTMUnitTest.runTest("TestVTMContext:test_OSCCommunication");


VTMUnitTest.makeRandomString((noNumbers: true))
TestVTMValueParameter.makeRandomString((noNumbers: true))
TestVTMBooleanParameter.makeRandomEnum

VTMUnitTest.runTestForClass(VTMParameter, true);
VTMUnitTest.runTestForClass(VTMContext, true);

VTMUnitTest.reportAllErrors = true;



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
(
var attributes, response;
"CHILDREN EQUALITY: %".format(~attributes[\children] == ~response[\children]).postln;
"\tAttributes: %".format([~attributes[\children], ~attributes[\children].collect(_.class)].lace).postln;
"\tResponse: %".format([~response[\children], ~response[\children].collect(_.class)].lace).postln;
"PARAMETERS EQUALITY: %".format(~attributes[\parameters] == ~response[\parameters]).postln;
~response[\parameters].keysValuesDo({arg paramName, param;
	var paramA, paramB;
	paramA = ~attributes[\parameters][paramName];
	paramB = ~response[\parameters][paramName];
	"%: (type: A: % B: % match: %)".format(paramName, paramA[\type], paramB[\type], paramA == paramB).postln;
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
				"\t% - %\n\t\tA[%]:\t%\n\t\tB[%]:\t%".format(
					key,
					match,
					val.class, val.collect({arg item, i; [item, item.class.asSymbol, item == paramB[key][i]]}),
					paramB[key].class, paramB[key].collect({arg item, i; [item, item.class.asSymbol, item == val[i]]})
				).postln;
			}, {
				"\t% - %\n\t\tA[%]:\t%\n\t\tB[%]:\t%".format(
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