VTMContext : VTMElement {
	var definition;
	var buildFunction;
	var fullPathThunk;
	var <envir;
	var <addr; //the address for this object instance.
	var <state;
	var <presets;
	var <cues;
	var <mappings;
	var <scores;
	var condition;

	classvar <viewClassSymbol = 'VTMContextView';

	*new{arg name, attributes, manager, definition;
		^super.new(name, attributes, manager).initContext(definition);
		//temp commented out
		//definition must be an object of type ContextDefinition
		// if(definition.isKindOf(VTMContextDefinition), {
		// 	^super.new(name, attributes, manager).initContext(definition);
		// 	}, {
		// 		Error("% - definition arg must have a kind of VTMContextDefinition".format(
		// 			this.class
		// 		)).throw;
		// 		^nil;
		// });
	}

	initContext{arg definition_;
		definition = definition_ ? VTMContextDefinition.new(nil, this);
		envir = definition.makeEnvir;
		condition = Condition.new;
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

	components{
		^super.components ++ [presets, cues, mappings, scores];
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
			this.components.do({arg it; it.prepare(cond)});
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
			this.components.do({arg it; it.run(cond)});
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
			this.components.do({arg it; it.free(cond)});
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

	//Call functions in the runtime environment with this context as first arg.
	execute{arg selector ...args;
		^envir[selector].value(this, *args);
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
		this.components.do(_.enableOSC);
	}

	disableOSC{
		this.components.do(_.disableOSC);
		super.disableOSC;
	}

	oscEnabled{
		^if(oscInterface.notNil, {
			oscInterface.enabled;
		}, {
			^nil;
		});
	}

	//recursive == true pulls attributes from components
	//recursive == false pulls only name attributes of components
	///TODO: Implement gettin gattributes from components
	attributes{arg recursive = false;
		var result = super.attributes;
		// var nonEmptyComps = this.components.select({arg item; item.isEmpty.not; });
		// if(recursive, {
		// 	nonEmptyComps.do({arg comp;
		// 		var val;
		// 		val = comp.attributes(recursive: true);
		// 		result.put(comp.name, val);
		// 	});
		// 	}, {
		// 		nonEmptyComps.do({arg comp;
		// 			result.put(comp.name, comp.names);
		// 		});
		// });
		^result;
	}

	*commandNames{
		^super.commandNames ++ [\prepare, \run, \free];
	}

	*queryNames{
		^super.queryNames ++ [\state,
			\parameters, \presets, \cues, \scores,
			\mappings, \commands, \definition
		];
	}
}
