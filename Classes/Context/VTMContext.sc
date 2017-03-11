VTMContext : VTMElement {
	var <parent;
	var definition;
	var children;
	var path; //an OSC valid path.
	var fullPathThunk;
	var <envir;
	var <addr; //the address for this object instance.
	var <state;
	var <parameters;
	var <presets;
	var <cues;
	var <mappings;
	var <scores;
	var <commands;

	classvar <contextLevelSeparator = $/;
	classvar <subcontextSeparator = $.;
	classvar <viewClassSymbol = 'VTMContextView';

	*new{arg name, definition, attributes, parent;
		^super.new(name, attributes).initContext(definition, parent);
	}

	initContext{arg definition_, parent_;

		if(attributes.includesKey(\addr), {
			addr = NetAddr.newFromIPString(attributes[\addr]).asString;
		});

		if(definition_.isNil, {
			definition = Environment.new;
		}, {
			definition = Environment.newFrom(definition_);
		});

		if(addr.isNil, {
			addr = NetAddr.localAddr;
		}, {
			// a different address is used. Check if UPD port needs to be opened
			if(thisProcess.openPorts.includes(addr.port), {
				"VTMContext - UDP port number already opened for address: %".format(addr).postln;
			}, {
				//try to open this port
				if(thisProcess.openUDPPort(addr.port), {
					"VTMContext - Opened UDP port number for address: %".format(addr).postln;
				}, {
					Error("VTMContext failed to open UDP port number for address: %".format(addr)).throw;
				});
			});
		});

		parent = parent_;
		children = IdentityDictionary.new;
		envir = Environment.newFrom(definition.deepCopy);
		envir.put(\self, this);

		if(parent.notNil, {
			//Make parent add this to its children.
			parent.addChild(this);
			//derive path from parent context
			path = parent.fullPath;
		}, {
			//Can use attributes specified path if context has no parent
			if(attributes.includesKey(\path), {
				path = attributes[\path].asSymbol;
				//force leading slash
				if(path.asString.first != $/, {
					path = "/%".format(path).asSymbol;
					"Context '%/%' forced leading slash in path".format(path, name).warn
				});
			});
		});
		fullPathThunk = Thunk({
			if(path.isNil, {
				"/%".format(name).asSymbol;
			}, {
				"%%%".format(path, this.leadingSeparator, name).asSymbol;
			});
		});

		parameters = VTMParameterManager(this,
			definition !? {definition[\parameters]},
			attributes !? {attributes[\parameters]},//parameters defined in attributes doesn't make sense really, as no code (i.e. 'action') can be defined in data..?
			definition !? {definition[\buildParameters]}
		);
		presets = VTMPresetManager(this,
			definition !? {definition[\presets]},
			attributes !? {attributes[\presets]},
			definition !? {definition[\buildPresets]}
		);
		cues = VTMCueManager(this,
			definition !? {definition[\cues]},
			attributes !? {attributes[\cues]},
			definition !? {definition[\buildCues]}
		);
		mappings = VTMMappingManager(this,
			definition !? {definition[\mappings]},
			attributes !? {attributes[\mappings]},
			definition !? {definition[\buildMappings]}
		);
		scores = VTMScoreManager(this,
			definition !? {definition[\scores]},
			attributes !? {attributes[\scores]},
			definition !? {definition[\buildScores]}
		);

		//TODO: Load commands
		// commands = VTMCommandManager(this,
		// 	definition !? {definition[\commands]},
		// 	attributes !? {attributes[\commands]},//commands defined in attributes doesn't make sense really, as no code (i.e. 'action') can be defined in data..?
		// 	definition !? {definition[\buildCommands]}
		// );

		this.prChangeState(\didInitialize);
	}

	//The context that calls prepare can issue a condition to use for handling
	//asynchronous events. If no condition is passed as argument the context will
	//make its own condition instance.
	//The ~prepare stage is where the module definition defines and creates its
	//parameters.
	prepare{arg condition, action;
		forkIfNeeded{
			var cond = condition ?? {Condition.new};
			this.prChangeState(\willPrepare);
			if(envir.includesKey(\prepare), {
				this.execute(\prepare, cond);
			});
			[parameters, presets, cues, mappings].do({arg it; it.prepare(cond)});

			this.enableOSC;
			this.prChangeState(\didPrepare);
			action.value(this);
		};
	}

	run{arg condition, action;
		forkIfNeeded{
			var cond = condition ?? {Condition.new};
			this.prChangeState(\willRun);
			if(envir.includesKey(\run), {
				this.execute(\run, cond);
			});
			[parameters, presets, cues, mappings].do({arg it; it.run(cond)});
			this.prChangeState(\didRun);
			action.value(this);
		};
	}

	free{arg condition, action;
		forkIfNeeded{
			var cond = condition ?? {Condition.new};
			this.prChangeState(\willFree);
			if(envir.includesKey(\free), {
				this.execute(\free, cond);
			});
			this.disableOSC;
			children.keysValuesDo({arg key, child;
				child.free(key, cond);
			});
			[parameters, presets, cues, mappings].do({arg it; it.free(cond)});
			this.prChangeState(\didFree);
			action.value(this);
			this.release; //Release this as dependant from other objects.
			definition = nil;
			super.free;
		};
	}

	reset{
		//set all parameters to default and evaluate the action
		parameters.reset(true)
	}

	addChild{arg context;
		children.put(context.name, context);
		context.addDependant(this);
		this.changed(\addedChild, context.name);
	}

	removeChild{arg key;
		var removedChild;
		removedChild = children.removeAt(key);
		//"[%] Removing child '%'".format(this.name, key).postln;
		removedChild.removeDependant(this);
		this.changed(\removedChild, key);
		^removedChild;
	}

	//If path is not defined the name is returned with a leading slash
	fullPath{
		^fullPathThunk.value;
	}

	//Can only set path on init.
	//Return only the path, not the name.
	path{
		^path;
		//Search parents until root node is found and construct path from that
	}

	//Some objects need to define special separators, e.g. subscenes, submodules etc.
	leadingSeparator{ ^$/;	}

	//Determine if this is a root context, i.e. having no parent.
	isRoot{
		^parent.isNil;
	}

	//Determine is this a lead context, i.e. having no children.
	isLeaf{
		^children.isEmpty;
	}

	children{
		if(children.isEmpty, {
			^nil;
		}, {
			^children.keys.asArray;// safer to return only the children. not the dict.
		});
	}

	//Find the root for this context.
	root{
		var result;
		//search for context root
		result = this;
		while({result.isRoot.not}, {
			result = result.parent;
		});
		^result;
	}

	//Search namespace for context path.
	//Search path can be relative or absolute.
	find{arg searchPath;

	}

	//Get the whole child context tree.
	childTree{
		var result;
		if(this.children.notNil, {
			this.children.do({arg item;
				result = result.add(item.childTree);
			});
		});
		^result;
	}

	prChangeState{ arg val;
		var newState;
		if(state != val, {
			state = val;
			this.changed(\state, state);
		});
	}

	//since parameter values are often set and get we have special methods for
	//these directly in the context interface
	set{arg ...args;
		if(args.size > 2, {
			args.pairsDo({arg paramName, paramVal;
				if(parameters.includes(paramName), {
					this.set(paramName, paramVal);
				});
			});
		}, {
			var param = parameters[args[0]];
			if(param.notNil, {
				param.valueAction_(*args[1]);
			});
		});
	}

	get{arg parameterName;
		^parameters[parameterName].value;
	}

	//Call functions in the runtime environment with this module as first arg.
	//Module definition functions always has the module as its first arg.
	//The method returns the result from the called function.
	execute{arg selector ...args;
		// "EXECUTE -envir: % \n\thasProto: %".format(envir, envir.proto).postln;
		if(envir.proto.notNil, {
			this.executeWithPrototypes(selector, *args);
		}, {
			^envir[selector].value(this, *args);
		});
	}

	executeWithPrototypes{arg selector ...args;
		var funcList, nextProto, result;
		//Make a function stack of the proto functions
		// "EVAL STACK FUNCS: sel: % args: %".format(selector, args).postln;
		nextProto = envir;
		while({nextProto.notNil}, {
			if(nextProto.includesKey(selector), {
				funcList = funcList.add(nextProto[selector]);
			});
			nextProto = nextProto.proto;
		});
		envir.use{
			funcList.reverseDo({arg item;
				//last one to evaluate is the the one that returns result
				result = item.valueEnvir(this, *args);
			});
		};
		^result;
	}

	makeView{arg parent, bounds, definition, attributes;
		var viewClass = this.class.viewClassSymbol.asClass;
		//override class if defined in attributes.
		if(attributes.notNil, {
			if(attributes.includesKey(\viewClass), {
				viewClass = attributes[\viewClass];
			});
		});
		^viewClass.new(parent, bounds, definition, attributes, this);
	}

	update{arg theChanged, whatChanged, theChanger ...args;
		// "[%] Update: %".format(this.name, [theChanged, whatChanged, theChanger, args]).postln;
		if(theChanged.isKindOf(VTMContext), {
			if(this.children.includes(theChanged), {
				switch(whatChanged,
					\freed, {
						this.removeChild(theChanged.name);
					}
				);
			});
		});
	}

	enableOSC{
		//make OSC interface if not already created
		if(oscInterface.isNil, {
			oscInterface = VTMOSCInterface.new(this);
		});
		[parameters, presets, cues, mappings].do(_.enableOSC);
		oscInterface.enable;
	}

	disableOSC{
		oscInterface.free;
		[parameters, presets, cues, mappings].do(_.disableOSC);
		oscInterface = nil;
	}

	oscEnabled{
		^if(oscInterface.notNil, {
			oscInterface.enabled;
		}, {
			^nil;
		});
	}

	attributes{
		var result = IdentityDictionary.new;

		result.put(\children, this.children);

		if(parameters.isEmpty.not, {
			result.put(\parameters, parameters.attributes);
		});
		if(presets.isEmpty.not, {
			result.put(\presets, presets.attributes);
		});
		if(cues.isEmpty.not, {
			result.put(\cues, cues.attributes);
		});
		if(mappings.isEmpty.not, {
			result.put(\mappings, mappings.attributes);
		});
		if(scores.isEmpty.not, {
			result.put(\scores, scores.attributes);
		});

		^result;
	}

	//command keys ending with ? are getters (or more precisely queries),
	//which function will return a value that can be sent to the application
	//that sends the query.
	//keys with ! are action commands that cause function to be run
	*makeOSCAPI{arg context;
		^IdentityDictionary[
			'children?' -> {arg context;
				var result;
				if(context.isLeaf.not, {
					result = context.children;
				}, {
					result = nil;
				});
				result;
			},
			'parameters?' -> {arg context;
				context.parameters.names;
			},
			'presets?' -> {arg context;
				context.presets.names;
			},
			'cues?' -> {arg context;
				context.cues.names;
			},
			'mappings?' -> {arg context;
				context.mappings.names;
			},
			'state?' -> {arg context; context.state; },
			'attributes?' -> {arg context; VTMJSON.stringifyAttributes(context.attributes); },
			'reset!' -> {arg context; context.reset; },
			'free!' -> {arg context; context.free; }
		];
	}
}
