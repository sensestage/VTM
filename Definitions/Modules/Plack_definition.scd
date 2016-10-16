~prototypes = ['AudioSource'];
~prepare = {arg module, condition;
	~source = {|freq, lag, decay, wild|
		HPF.ar(
			Pluck.ar(
				PinkNoise.ar(1),
				Impulse.ar(LFDNoise1.kr(wild).exprange(0.005, 10*wild)),
				1,
				1/freq.clip(0.0001, 1).lag(lag),
				decay
			),
			9
		)!2
	};
	~initSynthArgs= [\freq, \lag, \decay, \wild];
};
~parameters = [
	(
		name: \freq,
		type: \decimal,
		action: {|p| ~output.set(\freq, p.value); },
		minVal: 1,
		maxVal: 4000,
		clipmode: \low,
		defaultValue: 400
	),
	(
		name: \lag,
		type: \decimal,
		action: {|p| ~output.set(\lag, p.value); },
		minVal: 0,
		maxVal: 1,
		clipmode: \low,
		defaultValue: 0
	),
	(
		name: \decay,
		type: \decimal,
		action: {|p| ~output.set(\decay, p.value); },
		minVal: 0,
		maxVal: 10,
		clipmode: \low,
		defaultValue: 3
	),
	(
		name: \wild,
		type: \decimal,
		action: {|p| ~output.set(\wild, p.value); },
		minVal: 1,
		maxVal: 100,
		clipmode: \low,
		defaultValue: 5
	)
];
~declarations= [
	\banjo -> (freq: 300, wild: 2, lag: 1, decay: 1, comment: "country&western"),
	\noise -> (freq: 3, wild: 4, comment: "noisy irregular rhythm"),
	\metal -> (freq: 40, wild: 90, decay: 0.9, lag: 10, comment: "metallic sounding")
];
