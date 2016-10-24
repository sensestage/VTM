~prototypes =  [\MIDIDevice];
~prepare = {arg device, cond;
	"Preparing Behringer BCF2000".postln;
	~device.setMappings(
		(
			fader: [number: (0..7), chan: 0, msgType: \control14].flop.collect(Event.newFrom(_)),
			rotaryA: [number: (80..87), chan: 0].flop.collect(Event.newFrom(_)),
			rotaryB: [number: (8..15), chan: 0].flop.collect(Event.newFrom(_)),
			rotaryC: [number: (16..23), chan: 0].flop.collect(Event.newFrom(_)),
			rotaryD: [number: (24..31), chan: 0].flop.collect(Event.newFrom(_)),
			rotaryButtonA: [number: (40..47), chan: 0].flop.collect(Event.newFrom(_)),
			rotaryButtonB: [number: (48..55), chan: 0].flop.collect(Event.newFrom(_)),
			rotaryButtonC: [number: (56..63), chan: 0].flop.collect(Event.newFrom(_)),
			rotaryButtonD: [number: (64..71), chan: 0].flop.collect(Event.newFrom(_)),
			buttonA: [number: (88..95), chan: 0].flop.collect(Event.newFrom(_)),
			buttonB: [number: (72..79), chan: 0].flop.collect(Event.newFrom(_)),
			buttonSide: [number: (96..99), chan: 0].flop.collect(Event.newFrom(_))
		)
	);
	// ~device.trace(true);
};
