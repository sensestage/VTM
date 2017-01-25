~prototypes = ['AudioFilter'];
~prepare = {arg module, condition;
	var irFilePath;
	var fftSize = 2048;
	var irSpectrumBufferSize, irBuffer, bufferLoaded = false;
	var attributes = module.attributes;

	if(attributes.includesKey(\irFilePath), {
		var irFile;
		if(File.exists(attributes.at(\irFilePath)), {
			irFilePath = attributes.at(\irFilePath);
		}, {
			Error("irReverb module failed to build: ir file not found '%'".format(attributes.at(\irFilePath))).throw;
		})
	}, {
		Error("irReverb module failed to build: Needs irFilePath").throw;
	});
	if(attributes.includesKey(\fftSize), {
		if(attributes[\fftSize].isNumber.not, {
			Error().throw;
		})
	});


	irBuffer = Buffer.read(
		~server,
		irFilePath,
		action: {
			condition.test = true;
			condition.signal;
		}
	);
	condition.wait;
	condition.test = false;
	//Calculate the size of the ir spectrum buffer
	irSpectrumBufferSize = PartConv.calcBufSize(fftSize, irBuffer);
	//Allocate buffer for irSpectrum data
	~irSpectrumBuffer = Buffer.alloc(
		~server,
		irSpectrumBufferSize,
		1,
		completionMessage: {
			condition.test = true;
			condition.unhang;
			bufferLoaded = true;
		}
	);
	condition.wait;
	condition.test = false;
	~irSpectrumBuffer = ~irSpectrumBuffer.preparePartConv(irBuffer, fftSize);
	~server.sync;
	irBuffer.free;
	if(bufferLoaded.not, {
		"KLANG: Error while loading ir response".warn;
	});

	// ~filterKlang[1] = \filter -> {|in|
	// 	PartConv.ar(
	// 		in,
	// 		2048,
	// 		~irSpectrumBufferKlang.bufnum;
	// 	);
	//
	// };

};
~run = {};
~free = {arg module, condition;
	~irSpectrumBuffer.free({arg buf;
		condition.unhang;
	});
	condition.hang;
};
