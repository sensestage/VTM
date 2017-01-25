~prototypes = [\AudioModule];
~prepare = {arg module, cond;
	var numInputs, numOutputs;
	var attributes = module.attributes;
	~group = ~engine.groups[\filters];

	if(attributes.includesKey(\numInputs), {
		~numInputs = attributes[\numInputs];
	}, { ~numInputs = 2; });
	if(attributes.includesKey(\numOutputs), {
		~numOutputs = attributes[\numOutputs];
	}, { ~numOutputs = 2;});

	~input = NodeProxy.audio(~server, ~numInputs);
	~output = NodeProxy.audio(~server, ~numOutputs);

	~output.source = {
		XOut.ar(~input.ar(~numInputs), \mix.kr(1.0));
	};
};
~parameters = [
	(
		name: \mix, type: \decimal, defaultValue: 1.0,
		action: {|p| ~output.set(\mix, p.value); }
	)
];
