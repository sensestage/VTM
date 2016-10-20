~prototypes = ['AudioSource'];

~prepare = {arg module, condition;
	~server;
	~source = {|freq, mod, bits, hash|
        var buf, init, second, third, sum;
        buf = LocalBuf(1e2,1).clear;
        init = MantissaMask.ar(
            SinOsc.ar(
                Hasher.ar(hash*LFDNoise1.kr(mod).exprange(1,10)).range(0,1000)
            )*SawDPW.ar(freq)%Saw.ar(mod),
            bits
        );
        second = MantissaMask.ar(init*PinkNoise.ar(1)*SinOscFB.ar(freq,mod.range(0,1)), bits);
        third = DelayWr.ar(buf,second)%PinkNoise.ar(0.2)!2;
        sum = SmoothDecimator.ar(third, 4410*hash, mod.range(0,1));
        Limiter.ar(sum,0.99);
	};
};

~free = {arg module, condition;
    "mantissa hash out".postln;
};

~initSynthArgs = [\freq, \mod, \bits, \hash];

~parameters = [
	(
		name: \freq,
		type: \decimal,
		action: {|p| ~output.set(\freq, p.value);
		},
		minVal: 1,
		maxVal: 20000,
		defaultValue: 8000,
	),
    (
		name: \mod,
		type: \decimal,
		action: {|p| ~output.set(\mod, p.value);
		},
		minVal: 0.01,
		maxVal: 30,
		defaultValue: 5,
	),
    (
		name: \bits,
		type: \integer,
		action: {|p| ~output.set(\bits, p.value);
		},
		minVal: 0,
		maxVal: 23,
		defaultValue: 6,
        clipmode: \both
	),
    (
		name: \hash,
		type: \decimal,
		action: {|p| ~output.set(\hash, p.value);
		},
		minVal: 0.1,
		maxVal: 100,
		defaultValue: 20,
	)
];

~declarations= [
    \mantiss -> (freq: 18000, mod: 200, bits: 1, hash: 1, comment: "flop comma not")
];