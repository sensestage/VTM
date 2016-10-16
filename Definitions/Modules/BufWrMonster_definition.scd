~prototypes = ['AudioSource'];

~prepare = {arg module, condition;
	~server;
	~source = {|magi, freq, unstabl, rate, grains, dur|
        b=LocalBuf(1e4,2).clear;
        i=SinGrain.ar(Dust.ar(grains),dur,freq);
        j=Sweep.ar(BufRd.ar(2,b,Saw.ar(freq,unstabl,0)),rate);
        k=BufWr.ar(SinOsc.ar([SinOsc.ar(0.1*b)*0.1,magi]),b,j);
        l=BufRd.ar(2,b,j)/2;
        //Splay.ar(l/i); // yepp
        Splay.ar(l); // clean
        //Splay.ar(l*i); // cheese
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
	),
    (
		name: \freq,
		type: \decimal,
		action: {|p| ~output.set(\freq, p.value);
		},
		minVal: 0,
		maxVal: 2000,
		defaultValue: 5,
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
		defaultValue: 1,
        clipmode: \both
	),
    (
		name: \dur,
		type: \decimal,
		action: {|p| ~output.set(\dur, p.value);
		},
		minVal: 0.01,
		maxVal: 5,
		defaultValue: 1,
        clipmode: \both
	)
];

