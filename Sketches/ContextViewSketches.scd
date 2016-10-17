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

~mod.parameterOrder
~mod.parameters[\freq].makeView.front;
~mod.parameters[\freq].rampValue(rrand(200, 1000), 5)

~mod.parameters[\phase].makeView.front;
~mod.setParameter(\freq, 780);

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