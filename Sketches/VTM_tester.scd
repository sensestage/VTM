VTMUnitTest.runAll;

VTMUnitTest.runTestForClass(VTMParameter, true);
VTMUnitTest.runTestForClass(VTMContext, true);
VTMUnitTest.runTestForClass(VTMApplication, true);
VTMUnitTest.runTestForClass(VTMContextParameterManager, true);

VTMApplication.test;

VTMContext.test;
VTMParameter.test;

VTMContextParameterManager.test;

TestVTMParameter.testTypes

VTMUnitTest.runTest("TestVTMContext:test_NewAndInitWithAttributes");
VTMUnitTest.runTest("TestVTMContext:test_DefaultConstruction");


TestVTMCommandManager.dataClass
TestVTMCommandManager.dataTestClass



~params = TestVTMParameterManager.makeTestAttributes;
~commands = TestVTMCommandManager.makeRandomAttributes;
~mappings = TestVTMMappingManager.makeRandomAttributes(nil, ~params);

~envy = Environment[\bone -> \dry]
~commands[1].value(~envy, 44,55,66,77)










(
a = rrand(3,7).collect({arg i; TestVTMParameter.makeRandomAttributes([\integer, \decimal, \string, \boolean].choose);});
)

(
TestVTMPreset.makeRandomAttributes(a).keysValuesDo({arg presetName, presetData;
	"Preset: %".format(presetName).postln;
	presetData.do({arg paramName, paramValue;
		"\t% - %".format(paramName, paramValue).postln;
	});
});
)



// a = TestVTMContext.makeRandomContext((parent: TestVTMContext.makeRandomContext));
a = TestVTMContext.makeRandomContext;
a.prepare;
a.dump
a.parameters
a.parent.children

TestVTMContextParameterManager.makeRandomPresetForContext(a);
TestVTMContextParameterManager.makeRandomPresetAttributesForContext(a);
TestVTMContextParameterManager.makeRandomPresetAttributesForParameterAttributes(a.attributes);

a.parameters
a.attributes[\parameters].pairsDo({arg item; item.postln});


VTMLibrary.at(\definitions, \Percussive);
VTMLibrary.loadGlobalLibrary;


(
~qwe = VTMModule.new('bi', definition: \Percussive);
~qwe.prepare;
)

~qwe.loadPreset(\space)
~qwe.loadPreset(\nap)
~qwe.loadPreset(\test)
~qwe.reset;
~qwe.play(1);
~qwe.stop(2);


(
w = Window.new();
v = ~qwe.makeView(w, Rect(0,0,250,250));
w.front;
)

~qwe.play(1);
~qwe.stop(2);
~qwe.free;
~qwe.envir[\output].scope

~qwe.parameters
~qwe.parameterOrder

(
~mod = VTMModule.new('ils', definition: \BufWrMonster);
~mod.prepare;
)

~mod.play(1);
~mod.stop(10);
~mod.free;
~mod.envir[\output].scope

~mod.parameters
~mod.parameterOrder


(
~mod.set(\magi, 0.5, \freq, 2, \unstabl, 300, \rate, 200, \grains, 10, \dur, 2, \lag, 1);
)
(
~mod.set(\magi, 9.5, \freq, 19000, \unstabl, 100, \rate, 20000, \grains, 1, \dur, 0.3, \lag, 2);
)
(
~mod.set(\magi, 90.5, \freq, 1000, \unstabl, 10, \rate, 2000, \grains, 1, \dur, 2.3, \lag, 5);
)
(
~mod.set(\magi, 0.1, \freq, 2.1, \unstabl, 4, \rate, 20000, \grains, 1, \dur, 0.1, \lag, 1);
)
(
~mod.set(\magi, 0.1, \freq, 200, \unstabl, 1000, \rate, 20000, \grains, 1, \dur, 10, \lag, 0.3);
)


(
w = Window.new();
v = ~mod.makeView(w, Rect(0,0,250,250));
w.front;
)


~mod.ramp(\freq, 780, 3);

v.refresh;
v.front;
v.close;
v.sizeHint;

VTMContext.viewClass;
VTMModule.viewClass

~mod.play();
~mod.stop;
~mod.free(onFreed: {"DODODO".postln;});

(hei: 3)[\heif]








/////////



(
~ert = VTMModule.new('fik', definition: \SinOsc);
~ert.prepare;
)

~ert.play;
~ert.stop;
~ert.free;

~mod.set(\magi, rrand(2, 200));
~mod.set(\magi, rrand(2, 200));

~mod.set(\freq, rrand(220, 440));


~mod.get(\freq);


VTMLibrary.at(\definitions, \SinOsc, \attributes).do(_.postln);
(
~mod = VTMModule.new('testModule', attributes: 'CoolSound', definition: \SinOsc);
~mod.prepare;
)


Platform.userAppSupportDir
