VTMLibrary.at(\definitions, \SinOsc);

VTMLibrary.loadGlobalLibrary;

//Make a simple module that is running all by itself.
(
~mod = VTMModule.new('testModule', definition: \SinOsc);
~mod.prepare(onReady: {"JAJAJAJ".postln;});
w = Window.new();
v = ~mod.makeView(w);
w.front;
)

v.refresh;
v.front;
v.close;

VTMContext.viewClass;
VTMModule.viewClass

~mod.play();
~mod.stop;
~mod.free(onFreed: {"DODODO".postln;});

~mod.parameters[\freq].rampValue(rrand(220, 740), rrand(0.2, 4));

~mod.parameters[\sideeffect].rampValue(false, 1);

~mod.set(\freq, rrand(220, 430));

~mod.set(\freq, rrand(220, 430), \phase, 2pi.rand);

~mod.rampParameter();

~mod.loadDeclaration(\coolSound);
~mod.loadDeclaration(\evenMoreCoolSound);


~mod.execute(\hey, \88, 2221);

~mod.parameters[\freq].valueAction_(rrand(220, 440));



~mod.parameters[\freq].value


VTMLibrary.at(\definitions, \SinOsc, \declarations).do(_.postln);
(
~mod = VTMModule.new('testModule', declaration: 'CoolSound', definition: \SinOsc);
~mod.prepare;
)


Platform.userAppSupportDir

'/myModule/freq:ramp', 440, 3000

a = fork {5.wait; "heu".postln;};
a.isPlaying
