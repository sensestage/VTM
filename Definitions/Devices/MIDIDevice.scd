(
	prototypes: [\mappable],
	prepare: {arg context;
		var declaration = context.declaration;
		var inDeviceName, inPortName, outDeviceName, outPortName;
		inDeviceName = declaration[\input][\deviceName];
		inPortName = declaration[\input][\portName];
		outDeviceName = declaration[\output][\deviceName];
		outPortName = declaration[\output][\portName];
		MIDIOut.connect();

		~midiController = MIDIDevice.new(inDeviceName, inPortName, outDeviceName, outPortName, context.name);
	},
	run: {},
	free: {arg module;
		~midiController.free;
	}
)
