VTMUnitTest.runTestForClass(VTMParameter, true);
VTMUnitTest.runTestForClass(VTMContext, true);
VTMUnitTest.runAll

VTMUnitTest.reportAllErrors = true;

VTMUnitTest.runTest("TestVTMContext:test_OSCCommunication")

VTMUnitTest.runTest("TestVTMParameter:test_GetAttributes")
VTMParameter.test
VTMValueParameter.test

AbstractResponderFunc.allEnabled['OSC unmatched'].do({arg it; it.path.postln})



AbstractResponderFunc.allEnabled['OSC unmatched'][0].free
