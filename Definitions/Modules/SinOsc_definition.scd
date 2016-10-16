~prototypes = ['AudioSource'];
~prepare = {arg module, condition;
	"Preparing SinOsc".postln;
	~source = {|freq, phase|
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