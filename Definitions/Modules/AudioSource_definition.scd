~prototypes = [\AudioModule];
~prepare = {arg module, cond;
	var numChannels;
	~group = ~engine.groups[\sources];
	~output = NodeProxy.audio(~server, 2);
};