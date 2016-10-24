


a = Scale.chromatic(\pythagorean);
a = Scale.chromatic(\just);

a.ratios


(
var definition, declaration;
definition = Environment.make{
	~prototypes = ['PolyphonicSynth'];
	~prepare = {arg module, cond;
		"Preparing voicer".postln;
		~maxVoices = module.declaration[\numVoices];
		~voices = [];

		~group =
		~scale = Tuning(\just);
		~playNote = {arg noteNum, vel;
			var freq, amp;
			freq = noteNum.midicps;
		};
		~stopNote = {arg noteNum;

		};
	};
};
declaration = (
	synthDef: \sine,
	numVoices: 4
);
~voicer = VTMModule.new('siner', nil, declaration, definition);
~voicer.prepare;
)

~scale = Scale.chromatic(\pythagorean);
~scale.ratios
~scale.at(24)
~scale.degreeToFreq(0, 1.midicps, 0);