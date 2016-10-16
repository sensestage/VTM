~prototypes = ['AudioSource'];
~prepare = {arg module, condition;
	var i_num= 8;
	~source = {|rate, cutoff|
		Splay.ar(
			Resonz.ar(
				ClipNoise.ar(LFNoise2.kr(rate!i_num).max(0.1)),
				LFNoise2.kr(rate/i_num!i_num).max(0)+1*cutoff,
				0.1
			)
		);
	};
	~initSynthArgs= [\rate, \cutoff];
};
~parameters = [
	(
		name: \rate,
		type: \decimal,
		action: {|p| ~output.set(\rate, p.value); },
		minVal: 0,
		maxVal: 1000,
		clipmode: \low,
		defaultValue: 1
	),
	(
		name: \cutoff,
		type: \decimal,
		action: {|p| ~output.set(\cutoff, p.value); },
		minVal: 10,
		maxVal: 10000,
		clipmode: \both,
		defaultValue: 1000
	)
];
~declarations= [
	\dark-> (rate: 0.1, cutoff: 40, comment: "oktober storm"),
	\mikro -> (rate: 100, cutoff: 400, comment: "irregular bubbly wind"),
	\train -> (rate: 10, cutoff: 4000, comment: "supersonic speed train")
];
