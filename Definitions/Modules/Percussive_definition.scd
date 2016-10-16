// HELLO IM NOT REAL YET ....

~prototypes = ['AudioSource'];

~prepare = {arg module, condition;
	~server;
	~source = {|i|

	};
};
~free = {arg module, condition;
	b.free({condition.unhang});
	condition.hang;
};
~parameters = [
	(
		name: \magi,
		type: \decimal,
		action: {|p| ~output.set(\magi, p.value);
		},
		minVal: 0.01,
		maxVal: 20,
		defaultValue: 0.5,
	)
];

//
//
// (
// Ndef(\e, {var max,delay,trigger,dChange,dTime,delayed;
//     var fadeTime = 0.1;
//
//     var freq = 300;
//     var tempo = 1;
//     dTime = MouseX.kr(0.1,10);
//     max = 0.1;
//     delay = 0.1;
//
//     i=BPF.ar(Impulse.ar({ rrand(1.0, 2.0) }*tempo ! 2), (0..1).nthPrime * freq, 0.05)*30;
//
//     trigger = Impulse.kr( 1/fadeTime );
//     delay = Latch.kr( delay, trigger );
//     dChange = Trig.kr( HPZ1.kr( delay ).abs, fadeTime ); // only change at actual change
//     dChange = ToggleFF.kr( dChange );
//     dTime = Latch.kr( delay, [ 1-dChange, dChange ] );
//     delayed = dTime.collect({ |dTime, j| i = DelayN.ar(RLPF.ar(i, Rand(100, 3000), 0.03), max, dTime / (2**j), 1, i * 0.5); });
//
//     XFade2.ar( delayed[0], delayed[1],
//     Delay1.kr( Slew.kr( dChange, 1/fadeTime, 1/fadeTime ) ).linlin(0,1,-1,1) );
// }).play
// )