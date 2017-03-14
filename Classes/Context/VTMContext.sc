VTMContext : VTMElement {
	var definition;
	var buildFunction;
	var fullPathThunk;
	var <envir;
	var <addr; //the address for this object instance.
	var <state;
	var <parameters;
	var <presets;
	var <cues;
	var <mappings;
	var <scores;
	var condition;

	classvar <viewClassSymbol = 'VTMContextView';

	*new{arg name, attributes, manager, definition;
		^super.new(name, attributes, manager).initContext(definition);
	}

	initContext{arg definition_ ;
		definition = VTMContextDefinition.new(definition_, this);
		envir = definition.makeEnvir;
		condition = Condition.new;

		parameters = VTMParameterManager(this,
			definition.parameters,
			attributes[\parameters]
		);
		presets = VTMPresetManager(this,
			definition.presets,
			attributes[\presets]
		);
		cues = VTMCueManager(this,
			definition.cues,
			attributes[\cues]
		);
		mappings = VTMMappingManager(this,
			definition.mappings,
			attributes[\mappings]
		);
		scores = VTMScoreManager(this,
			definition.scores,
			attributes[\scores]
		);
		this.prChangeState(\didInitialize);
	}

	prComponents{ ^[parameters, presets, cues, mappings, scores]; }	

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
			this.prComponents.do({arg it; it.prepare(cond)});
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
			this.prComponents.do({arg it; it.run(cond)});
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
//			children.keysValuesDo({arg key, child;
//				child.free(key, cond);
//			});
			this.prComponents.do({arg it; it.free(cond)});
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

	//If path is not defined the name is returned with a leading slash
	fullPath{
		^fullPathThunk.value;
	}

//	//Some objects need to define special separators, e.g. subscenes, submodules etc.
//	leadingSeparator{ ^$/;	}
//
//	//Determine if this is a root context, i.e. having no parent.
//	isRoot{
//		//^parent.isNil;
//	}
//
//	//Determine is this a lead context, i.e. having no children.
//	isLeaf{
//		^children.isEmpty;
//	}
//
//	children{
//		if(children.isEmpty, {
//			^nil;
//		}, {
//			^children.keys.asArray;// safer to return only the children. not the dict.
//		});
//	}
	//Find the root for this context.
//	root{
//		var result;
//		//search for context root
//		result = this;
//		while({result.isRoot.not}, {
//			result = result.parent;
//		});
//		^result;
//	}

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

	update{arg theChanged, whatChanged, theChanger ...args;
		// "[%] Update: %".format(this.name, [theChanged, whatChanged, theChanger, args]).postln;
	}

	enableOSC{
		super.enableOSC;
		this.prComponents.do(_.enableOSC);
	}

	disableOSC{
		this.prComponents.do(_.disableOSC);
		super.disableOSC;
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
