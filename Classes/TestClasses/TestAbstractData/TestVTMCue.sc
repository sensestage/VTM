TestVTMCue : TestVTMAbstractData {

	*makeRandomParameter{arg key, params;
		var result;
		result = super.makeRandomParameter(key, params);
		result = switch(key,
			\preDelay, {rrand(0.0, 10.0)},
			\duration, {rrand(0.0, 10.0)},
			\postDelay, {rrand(0.0, 10.0)},
			\points, {
				{|i| {arg ...args; "Cue point: %".format(i+1).postln;} } ! rrand(1,10);
			},
			\hangBeforeStart, {0.5.coin},
			\maxStartHangTime, {rrand(0.0, 10.0)},
			\hangBeforeEnd, {0.5.coin},
			\maxEndHangTime, {rrand(0.0, 10.0)},
			\pointOrder, {
				[\normal, \random, \reverse].choose;//TODO: add order array possibility
			},
			\hangBetweenPoints, {0.5.coin},
			\delayBetweenPoints, {rrand(0.0, 10.0)}
		);
		^result;
	}
}
