VTMLibrary.at(\definitions, \Percussive);
VTMLibrary.loadGlobalLibrary;


(
~qwe = VTMModule.new('bi', definition: \Percussive);
~qwe.prepare;
)

~qwe.loadDeclaration(\space)
~qwe.loadDeclaration(\nap)
~qwe.loadDeclaration(\test)
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
~mod.setParameter(\magi, 0.5, \freq, 2, \unstabl, 300, \rate, 200, \grains, 10, \dur, 2, \lag, 1);
)
(
~mod.setParameter(\magi, 9.5, \freq, 19000, \unstabl, 100, \rate, 20000, \grains, 1, \dur, 0.3, \lag, 2);
)
(
~mod.setParameter(\magi, 90.5, \freq, 1000, \unstabl, 10, \rate, 2000, \grains, 1, \dur, 2.3, \lag, 5);
)
(
~mod.setParameter(\magi, 0.1, \freq, 2.1, \unstabl, 4, \rate, 20000, \grains, 1, \dur, 0.1, \lag, 1);
)
(
~mod.setParameter(\magi, 0.1, \freq, 200, \unstabl, 1000, \rate, 20000, \grains, 1, \dur, 10, \lag, 0.3);
)


(
w = Window.new();
v = ~mod.makeView(w, Rect(0,0,250,250));
w.front;
)


~mod.rampParameter(\freq, 780, 3);

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

~mod.setParameter(\magi, rrand(2, 200));
~mod.setParameter(\magi, rrand(2, 200));

~mod.parameters[\freq].valueAction_(rrand(220, 440));


~mod.parameters[\freq].value


VTMLibrary.at(\definitions, \SinOsc, \declarations).do(_.postln);
(
~mod = VTMModule.new('testModule', declaration: 'CoolSound', definition: \SinOsc);
~mod.prepare;
)


Platform.userAppSupportDir