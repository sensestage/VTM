TestVTMCommand : VTMUnitTest {

	*makeRandomAttributes{arg parameterAttributes;
		var result;
		result = {arg env...args;
			"I am command '%' in env: \n\t%\n\tand these are my args: %".format(name, env, args).postln;
			env.put(name, (
				lastTimeEvaluated: Main.elapsedTime
			));
		};
		^result;
	}
}