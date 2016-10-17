~prototypes = ['AudioSource'];

~prepare = {arg module, condition;
	~server;
	~source = {|tempo, delayTimeChangeRate, maxDelayTime, lagTime, freq, resFreq|
        var sig, delaytime, init, trig, din, delayed;
        delaytime= LFNoise0.ar(delayTimeChangeRate).range(0.01,maxDelayTime).lag(lagTime);
        init=BPF.ar(Impulse.ar({ rrand(1.0, 2.0) }*tempo ! 2), (0..1).nthPrime * freq, 0.01)*40;
        trig= HPZ2.ar(delaytime);
        din= Latch.ar(delaytime, PulseDivider.ar(trig, 2, #[0, 1]));
        delayed= DelayN.ar([init,init], maxDelayTime, din, 1, 0);
        4.do { |i| delayed = DelayN.ar(RLPF.ar(delayed, resFreq, 0.3), 1, 1 / (2**i), 1, delayed * 0.5) };
        sig=XFade2.ar(delayed[0], delayed[1], Ramp.ar(ToggleFF.ar(trig), lagTime, 2, -1));
        sig*0.05;
        Limiter.ar(sig, 0.99);
	};
};

~free = {arg module, condition;
    "dododo dada didid".postln;
};

~initSynthArgs = [\tempo, \freq, \resFreq, \delayTimeChangeRate, \maxDelayTime, \lagTime];

~parameters = [
	(
		name: \tempo,
		type: \decimal,
		action: {|p| ~output.set(\tempo, p.value);
		},
		minVal: 0.5,
		maxVal: 10,
		defaultValue: 0.2,
        clipmode: \both
	),
    (
		name: \freq,
		type: \decimal,
		action: {|p| ~output.set(\freq, p.value);
		},
		minVal: 100,
		maxVal: 2000,
		defaultValue: 200,
	),
    (
		name: \resFreq,
		type: \decimal,
		action: {|p| ~output.set(\resFreq, p.value);
		},
		minVal: 300,
		maxVal: 12000,
		defaultValue: 1000,
	),
    (
		name: \delayTimeChangeRate,
		type: \decimal,
		action: {|p| ~output.set(\delayTimeChangeRate, p.value);
		},
		minVal: 0.01,
		maxVal: 10,
		defaultValue: 0.3,
        clipmode: \both
    ),
    (
		name: \maxDelayTime,
		type: \decimal,
		action: {|p| ~output.set(\maxDelayTime, p.value);
		},
		minVal: 0.01,
		maxVal: 10,
		defaultValue: 5,
        clipmode: \both
    ),
    (
		name: \lagTime,
		type: \decimal,
		action: {|p| ~output.set(\lagTime, p.value);
		},
		minVal: 0.1,
		maxVal: 40,
		defaultValue: 5
    )
];

~declarations= [
	\space -> (tempo: 4.23, freq: 263.79, resFreq: 1631.38, delayTimeChangeRate: 0.73, maxDelayTime: 4.73, lagTime:9.32, comment: "1970ies style"),
    \nap -> (tempo: 1.23, freq: 220, resFreq: 800, delayTimeChangeRate: 0.2, maxDelayTime: 2.12, lagTime:1.0, comment: "napnap")
];





    