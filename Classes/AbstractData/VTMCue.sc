VTMCue : VTMAbstractData {
	var routine;
	var >startAction; //a function
	var >endAction; //a function
	var condition;
	var <>armed = true;


	*managerClass{ ^VTMCueManager; }

	*new{arg name, declaration, manager;
		^super.new(name, declaration, manager).initCue;
	}

	initCue{
		condition = Condition.new;
	}

	*declarationKeys{
		^super.declarationKeys ++ [
			\preDelay,
			\duration,
			\postDelay,
			\points,
			\hangBeforeStart,
			\maxStartHangTime,
			\hangBeforeEnd,
			\maxEndHangTime,
			\pointOrder,
			\hangBetweenPoints,
			\delayBetweenPoints
		];
	}

	go{
		if(armed, {
			routine !? {this.stop;};
			routine = Routine({
				startAction.value(this);
				if(this.hangBeforeStart, {
					var hangtime = this.maxStartHangTime;
					this.changed(\state, \hangBeforeStart, hangtime);
					condition.hang(hangtime);
				}, {
					var delaytime = this.preDelay;
					this.changed(\state, \preDelay, delaytime);
					this.preDelay !? {delaytime.wait;};
				});

				this.points.do({arg point, i;
					this.changed(\state, \executingPoint, i);
					point.value;
				});
				this.duration !? {this.duration.wait;};
				if(this.hangBeforeEnd, {
					var hangtime = this.maxEndHangTime;
					this.changed(\state, \hangBeforeStart, hangtime);
					condition.hang(hangtime);
				}, {
					this.preDelay !? {this.preDelay.wait;};
				});
				this.changed(\ended);
				endAction.value(this);
			});
		});
	}

	signal{
		condition.unhang;
	}

	stop{
		if(routine.notNil and: {routine.isPlaying}, {
			routine.stop;
		});
	}

	reset{
		this.stop;
		if(routine.notNil, {
			routine.reset;
		});
	}


	//Attribute getters
	preDelay{ ^this.get(\preDelay) ? 0.0; }
	preDelay_{arg val; this.set(\preDelay, val); }

	duration{ ^this.get(\duration) ? 0.0; }
	duration_{arg val; this.set(\duration, val); }

	postDelay{ ^this.get(\postDelay) ? 0.0; }
	postDelay_{arg val; this.set(\postDelay, val); }

	points{ ^this.get(\points); }
	points_{arg val; this.set(\points, val); }

	hangBeforeStart{ ^this.get(\hangBeforeStart) ? false; }
	hangBeforeStart_{arg val; this.set(\hangBeforeStart, val); }

	maxStartHangTime{ ^this.get(\maxStartHangTime) ? 10.0; }
	maxStartHangTime_{arg val; this.set(\maxStartHangTime, val); }

	hangBeforeEnd{ ^this.get(\hangBeforeEnd) ? false; }
	hangBeforeEnd_{arg val; this.set(\hangBeforeEnd, val); }

	maxEndHangTime{ ^this.get(\maxEndHangTime) ? 10.0; }
	maxEndHangTime_{arg val; this.set(\maxEndHangTime, val); }

	//'normal', 'reverse', 'random', <array of integers>
	pointOrder{ ^this.get(\pointOrder) ? \normal; }
	pointOrder_{arg val; this.set(\pointOrder, val); }

	hangBetweenPoints{ ^this.get(\hangBetweenPoints) ? false; }
	hangBetweenPoints_{arg val; this.set(\hangBetweenPoints, val); }

	maxBetweenPointsHangTime{ ^this.get(\maxBetweenPointsHangTime) ? 10.0; }
	maxBetweenPointsHangTime_{arg val; this.set(\maxBetweenPointsHangTime, val); }

	delayBetweenPoints{ ^this.get(\delayBetweenPoints) ? 0.0; }
	delayBetweenPoints_{arg val; this.set(\delayBetweenPoints, val); }
}
