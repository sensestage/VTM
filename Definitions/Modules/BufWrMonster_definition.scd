~prototypes = ['AudioSource'];

~prepare = {arg module, condition;
	~server;
	~source = {|magi, freq, unstabl, rate, grains, dur|
        var buf, sines, sweeper, bufWr, bufRd, bus0, bus1;
        buf=LocalBuf(1e4,2).clear;
        sines=SinGrain.ar(Dust.ar(grains),dur,freq);
        sweeper=Sweep.ar(BufRd.ar(2,b,Saw.ar(freq,unstabl,0)),rate);
        bufWr=BufWr.ar(SinOsc.ar([SinOsc.ar(0.1*b)*0.1,magi]),b,j);
        bufRd=BufRd.ar(2,b,j)/2;
        bus0=Splay.ar(l);
        bus1=Splay.ar(l%i)*0.2;
        bus0+bus1;
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
		defaultValue: 0.05,
	),
    (
		name: \freq,
		type: \decimal,
		action: {|p| ~output.set(\freq, p.value);
		},
		minVal: 0,
		maxVal: 2000,
		defaultValue: 2,
	),
    (
		name: \unstabl,
		type: \decimal,
		action: {|p| ~output.set(\unstabl, p.value);
		},
		minVal: 1,
		maxVal: 500,
		defaultValue: 200,
	),
    (
		name: \rate,
		type: \decimal,
		action: {|p| ~output.set(\rate, p.value);
		},
		minVal: 1,
		maxVal: 2e4,
		defaultValue: 4e3
	),
    (
		name: \grains,
		type: \decimal,
		action: {|p| ~output.set(\grains, p.value);
		},
		minVal: 0.1,
		maxVal: 10,
		defaultValue: 10,
        clipmode: \both
	),
    (
		name: \dur,
		type: \decimal,
		action: {|p| ~output.set(\dur, p.value);
		},
		minVal: 0.01,
		maxVal: 5,
		defaultValue: 2,
        clipmode: \both
	)
];

