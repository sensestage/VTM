TestVTMCommand : TestVTMElement {
	*makeRandomAttribute{arg key, params;
		var result = super.makeRandomAttribute(key, params);
		switch(key,
			\function, {
				var name;
				if(params.notNil and: {params.includesKey(\name)}, {
					name = params[\name];
				}, {
					name = this.makeRandomSymbol;
				});
				result = {arg env...args;
					"I am command '%' in env: \n\t%\n\tand these are my args: %".format(
						name, env, args).postln;
					env.put(name, (
						lastTimeEvaluated: Main.elapsedTime,
						args: args
					));
				};
		});

		^result;
	}
}