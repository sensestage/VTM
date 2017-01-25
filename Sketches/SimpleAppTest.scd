//Start simple app with alternative server options
(
var definition, attributes;
attributes = (
	startServer: true,
	serverOptions: (
		outDevice: "Soundflower (64ch)", inDevice: "Soundflower (64ch)"
	)
);
definition = Environment.make({
	~prepare = {arg app, cond;
		"Preparing test app % %".format(app, cond).postln;
	};
	~run = {arg app, cond;
		"running test app % %".format(app, cond).postln;
	};
	~free = {arg app, cond;
		"Freeing test app % %".format(app, cond).postln;
	};
});
~app = VTMApplication.new('test',
	definition, attributes
);
)

//Load a simple module

~app.moduleHost.loadModule();
