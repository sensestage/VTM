(
	prototypes: [\mappable],
	prepare: {arg context;
		var attributes = context.attributes;
		var inDeviceName, inPortName, outDeviceName, outPortName;
		inDeviceName = attributes[\input][\deviceName];
		inPortName = attributes[\input][\portName];
		outDeviceName = attributes[\output][\deviceName];
		outPortName = attributes[\output][\portName];
		MIDIOut.connect();

		~midiController = MIDIDevice.new(inDeviceName, inPortName, outDeviceName, outPortName, context.name);
	},
	run: {},
	free: {arg module;
		~midiController.free;
	}
)
