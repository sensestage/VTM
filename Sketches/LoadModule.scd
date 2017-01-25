~node = VTMNode('bbb', NetAddr("127.0.0.1", 57120));

(
var name, desc, def;
name = 'jeger';
desc = (
	//def: \temple,
	ene: 11,
	tow: [3,6,1],
	bing: 9.01,
	rasky: \jensen,
	hellosn: "eiirik"
);
def = Environment.make {
	~prepare = {arg desc; "Preparing: %".format(desc).postln; };
	~start = {arg desc; "Starting: %".format(desc).postln; };
	~free = {arg desc; "Freeing: %".format(desc).postln; };
	~buildParameters = {arg env, module;
		"Building parameters inside: %\n\tfor module: %".format(env, module).postln;
		[
			\tow: [
				\type: \floatArray,
				range: [0.0, 50.0],
				default: [2.0, 3.0],
				action: {arg p;
					"% - %".format(p.name, p.value).postln;
				}
			]
		];
	};
	~parameterattributes = [
		ene: (
			type: \integer, range: [0, 100], default: 50, action: {arg p;
				"% - %".format(p.name, p.value).postln;
			}
		),
		tow: (
			type: \integerArray, range: [0, 10], default: 5, action: {arg p;
				"% - %".format(p.name, p.value).postln;
			}
		),
		bing: (
			type: \float, range: [0.0, 10.0], default: 5.1, action: {arg p;
				"% - %".format(p.name, p.value).postln;
			}
		),
		rasky: (
			type: \string, default: "ABCD", action: {arg p;
				"% - %".format(p.name, p.value).postln;
			}
		),
		rasky: (
			type: \symbol, range: [\jensen, \hansen], default: \hansen, action: {arg p;
				"% - %".format(p.name, p.value).postln;
			}
		),
	];
};
~node.moduleHost.loadModuleattributes(name, def, desc);
)

~node.moduleHost.modules[\jeger].parameters[\rasky].attributes;
~node.moduleHost.modules[\jeger].parameters[\tow].attributes;
~node.moduleHost.modules[\jeger].orderedParameters.collect({arg item; item.name;});
~node.moduleHost.modules[\jeger].attributes;
~node.moduleHost.modules[\jeger].definition;
~node.moduleHost.modules[\jeger].defName;
~node.moduleHost.modules[\jeger].parameters;


