VTMLibrary.at(\definitions, \SinOsc);

VTMLibrary.loadGlobalLibrary;

//Make a simple module that is running all by itself.
(
~mod = VTMModule.new('testModule', definition: \SinOsc);
~mod.prepare;
)

~mod.play;
~mod.stop;
~mod.free;

~mod.setParameter(\freq, rrand(220, 430));


~mod.parameters[\freq].valueAction_(rrand(220, 440));


~mod.parameters[\freq].value


VTMLibrary.at(\definitions, \SinOsc, \declarations).do(_.postln);
(
~mod = VTMModule.new('testModule', declaration: 'CoolSound', definition: \SinOsc);
~mod.prepare;
)


Platform.userAppSupportDir