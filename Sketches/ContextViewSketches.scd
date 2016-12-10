//Make a simple module that is running all by itself.
(
~mod = VTMModule.new('testModule', definition: \SinOsc);
~mod.prepare(onReady: {"JAJAJAJ".postln;});
)
(
w = Window.new();
v = ~mod.makeView(w, Rect(0,0,250,250));
w.front;
)

~mod.parameterManager.addParameter(VTMDecimalParameter('volume', (minVal: -96, maxVal: 6, defaultValue: -96)), 0);

~mod.parameterOrder
~mod.parameters[\freq].makeView.front;
~mod.ramp(\freq, rrand(200, 1000), 5);
~mod.reset;
~mod.parameters[\phase].makeView.front;
~mod.set(\freq, 780);

~mod.ramp(\freq, 780, 3);

v.refresh;
v.front;
v.close;
v.sizeHint;

VTMContext.viewClass;
VTMModule.viewClass

~mod.play(3);
~mod.stop(3);
~mod.free(onFreed: {"DODODO".postln;});

(hei: 3)[\heif]
