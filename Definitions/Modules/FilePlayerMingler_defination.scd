~prototypes = ['AudioSource'];

~prepare = {arg module, condition;
	~server;

	~buffer = Buffer.readChannel(s,Platform.resourceDir +/+ "sounds/birds.WAV",
		action: {
			condition.unhang;
		}
	);
	condition.hang;

	~source = {|rate, tiltAmount|
        var rateTilt, sPlayer, sig;
        rateTilt=LFNoise0.ar(rate).lag(0.1).range(0.1,10);
        sig=PlayBuf.ar(b.numChannels, ~buffer, rate*rateTilt, 1, 0, 1);
        sPlayer=Splay.ar([SinOsc.ar(rate*sig), Pulse.ar(sig*pi*rate)]);
        sig=Convolution.ar(sig, sPlayer);
        sig=Limiter.ar(sig, 0.99);
        sig=Splay.ar(sig, level: 1);
	};
};
~free = {arg module, condition;
	~buffer.free({condition.unhang});
	condition.hang;
};
~parameters = [
	(
		name: \rate,
		type: \decimal,
		action: {|p| ~output.set(\freq, p.value);
		},
		minVal: 0.01,
		maxVal: 100,
		clipmode: \both,
		defaultValue: 2,
	)
];