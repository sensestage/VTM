//Make a simple module that is running all by itself.
(
var definition, declaration;
declaration = (
	parameters: (fee: 99, foo: 33)
);
definition = Environment.make({
	~prepare = {arg mod, cond;
		"Preparing test module % %".format(mod, cond).postln;
		"\tUsing this declaration: %".format(mod.declaration).postln;

		mod.on(\ready, {
			//run related stuff
		});
	};

	~free = {arg mod, cond;
		"Freeing test module % %".format(mod, cond).postln;
		"\tUsing this declaration: %".format(mod.declaration).postln;

		mod.on(\freed, {
			"I'm donne".postln;
		});
	};
	~parameters = [
		(
			name: \fee, type: \integer, defaultValue: 98,
			action: {|p| "%: %".format(p.fullPath, p.value).postln;}
		),
		(
			name: \foo, type: \integer, defaultValue: 32
		)
	];
});
~mod = VTMModule.new('testModule', definition, declaration);

~mod.prepare;
)


~mod.path
~mod.set(\fee, 22.rand);

~mod.children

~mod.run;
~mod.free;


a = Env([11, 22], [3]).postln.asPseg.asStream;
a.next;
