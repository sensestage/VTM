~prototypes = ['AudioSource']; //List of prototypes in reverse order of presedence

//Prepare function is run before the module starts
~prepare = {arg module, condition;
	//The ~server environment variable holds the reference to the Server object
	//that is running within the application.
	~server;

	// The condition argument is used for halting the execution thread when
	// e.g. buffers need to allocated.
	~buffer = Buffer.read(~server, "lyd.wav",
		action: {
			// Notify the condition  (condition.hang below)
			// that we are ready to continue
			condition.unhang;
		}
	);
	//Wait until the buffer is loaded
	condition.hang;

	//All AudioSource modules need to define a ~source function.
	//This will be wrapped into a NodeProxy when the module is built.
	~source = {|freq, phase|
		SinOsc.ar(freq, phase);
	};
};
//The free function releases and free whatever the module designer
//makes in terms of buffer allocation, playing Routines, osc responders etc.
~free = {arg module, condition;
	//I did allocated a ~buffer in the ~prepare function, so it
	//is important that I free this here
	~buffer.free({condition.unhang});
	condition.hang;
};
~parameters = [
	(
		name: \freq,
		type: \decimal,
		action: {|p|
			//To which variable the nodeProxy playing is to be sent is
			//not yet clearly defined. Using ~output for now.
			~output.set(\freq, p.value);
		},
		minVal: 20,
		maxVal: 20000,
		clipmode: \both, //low, high, both, none(default)
		filterRepetitions: true,//prevents running action when repeated values are set, false by default
		defaultValue: 200, //the value that will set upon 'reset'
		stepsize: 20 //increment value used in 'increment'/'decrement' messages
	),
	(
		name: \phase,
		type: \decimal,
		action: {|p|
			~output.set(\phase, p.value);
		},
		minVal: 0,
		maxVal: 2pi
	)
];