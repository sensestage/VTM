~prototypes = ['AudioSource'];
~prepare = {arg module, condition;
	~source = {|freq = 440, phase = 0|
		SinOsc.ar(freq, phase);
	};
};
~parameters = [
	(
		name: \freq,
		type: \decimal,
		action: {|p| ~output.set(\freq, p.value); },
		minVal: 20,
		maxVal: 20000,
	),
	(
		name: \phase,
		type: \decimal,
		action: {|p| ~output.set(\phase, p.value); },
		minVal: 0,
		maxVal: 2pi
	)
];
~declarations = [
	\coolSound -> (freq: 88, phase: 0.2, comment: "Denne er kul"),
	\evenMoreCoolSound -> (freq: 208, phase: 0.1, comment: "Denne er enda kulere!")
];