VTMUnitTest.runAll

VTMUnitTest.runTestForClass(VTMParameter, true);
VTMUnitTest.runTestForClass(VTMContext, true);

VTMUnitTest.reportAllErrors = true;


VTMUnitTest.runTest("TestVTMContext:test_OSCCommunication")


OSCFunc.trace

NetAddr.localAddr.sendMsg('/hei', *[89, 99])
NetAddr.localAddr.sendMsg('/hei', [89, 99])


[12, 123].unbubble
