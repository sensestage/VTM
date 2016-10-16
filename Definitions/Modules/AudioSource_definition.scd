~prototypes = [\AudioModule];
~prepare = {arg module, cond;
	var numChannels;
	~output = NodeProxy.audio(~server, 2);
};