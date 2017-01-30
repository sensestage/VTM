VTMContext {
	var <name;
	var <parent;
	var definition;
	var attributes;
	var children;
	var path; //an OSC valid path.
	var fullPathThunk;
	var <envir;
	var <addr; //the address for this object instance.
	var oscInterface;
	var <state;
	var parameterManager;
	var cueManager;

	classvar <contextLevelSeparator = $/;
	classvar <subcontextSeparator = $.;
	classvar <viewClassSymbol = 'VTMContextView';

	*new{arg name, definition, attributes, parent;
		if(name.isNil, {
			Error("Context must have name").throw;
		});
		^super.new.initContext(name, definition, attributes, parent);
	}

	initContext{arg name_, definition_, attributes_, parent_;
		name = name_.asSymbol;
		if(attributes_.isNil, {
			attributes = IdentityDictionary.new;
		}, {
			attributes = IdentityDictionary.newFrom(attributes_);
			if(attributes.includesKey(\addr), {
				addr = NetAddr.newFromIPString(attributes[\addr]).asString;
			});
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

		parameterManager = VTMContextParameterManager(this);
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

			//load and build parameters
			if(definition.includesKey(\parameters), {
				parameterManager.loadParameterAttributes(definition[\parameters]);
			});
			if(definition.includesKey(\buildParameters), {
				parameterManager.loadParameterAttributes(
					this.execute(\buildParameters, cond);
				);
			});

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
			parameterManager.free;
			this.prChangeState(\didFree);
			action.value(this);
			this.release; //Release this as dependant from other objects.
			definition = nil;
			attributes = nil;
		};
	}

	reset{
		//set all parameters to default and evaluate the action
		parameterManager.parameters.do(_.reset(true));
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


	//Interface to presetManager
	presets{
		^parameterManager.presets;
	}

	getPreset{arg presetName;
		^parameterManager.getPreset(presetName);
	}

	addPreset{arg data, presetName, slot;
		parameterManager.addPreset(data, presetName, slot);
	}

	removePreset{arg presetName;
		parameterManager.removePreset(presetName);
	}

	presetAttributes{
		^parameterManager.presetAttributes;
	}

	cues{
		var result = [];
		if(envir.includesKey(\cues), {
			result = result.addAll(envir[\cues].collect({arg assoc; assoc.key;}));
		});
		if(cueManager.notNil, {
			result.addAll(cueManager.names);
		});
		^result;
	}

	getCue{arg cueName;
		^cueManager.at(cueName);
	}

	addCue{arg data, cueName, slot;
		//if this is the first cue to be added we have to create
		//a cueManager first
		if(cueManager.isNil, {
			cueManager = VTMNamedList.new;
		});
		cueManager.addItem(data, cueName, slot);
		this.changed(\cueAdded, cueName);
	}

	removeCue{arg cueName;
		var removedCue;
		if(cueManager.notNil, {
			removedCue = cueManager.removeItem(cueName);
			if(removedCue.notNil, {
				"Context: % - removed cue '%'".format(this.fullPath, cueName).postln;
			});
		}, {
			"Context: % - no cues to remove".format(this.fullPath).warn
		});
	}
	//END cue methods

	loadPreset{arg presetName, ramping;
		if(envir.includesKey(\presets), {
			var newPreset;
			newPreset = envir[\presets].detect({arg item; item.key == presetName;});
			if(newPreset.notNil, {
				newPreset = newPreset.value;
				newPreset.removeAt(\comment);
				this.set(*newPreset.asKeyValuePairs);
				if(ramping.isNil, {
					this.set(*newPreset.asKeyValuePairs);
				}, {
					newPreset = newPreset.asKeyValuePairs.flop.collect({arg item;
						item.add(ramping);
					}).flatten;
					this.ramp(*newPreset);
				});
			}, {
				"Preset '%' for '%' not found".format(presetName, this.fullPath).warn;
			});
		}, {
			"No preset stored in '%' not found".format(presetName, this.fullPath).warn;
		});
	}



	prChangeState{ arg val;
		var newState;
		if(state != val, {
			state = val;
			this.changed(\state, state);
		});
	}

	parameters{ ^parameterManager.order; }

	set{arg ...args;
		if(args.size > 2, {
			args.pairsDo({arg paramName, paramVal;
				if(this.parameters.includesKey(paramName), {
					this.set(paramName, paramVal);
				});
			});
		}, {
			var param = parameterManager.parameters[args[0]];
			if(param.notNil, {
				param.valueAction_(*args[1]);
			});
		});
	}

	get{arg parameterName;
		^parameterManager.parameters[parameterName].value;
	}

	getParameter{arg parameterName;
		^parameterManager.parameters[parameterName];
	}

	ramp{arg ...args; // paramName, val, rampTime, paramName, val ...etc.
		var param;
		if(args.size > 3, {
			args.clump(3).do({arg item;

			})
		}, {

		});
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
			oscInterface = VTMContextOSCInterface.new(this);
		});
		parameterManager.enableOSC;
		oscInterface.enable;
	}

	disableOSC{
		oscInterface.free;
		parameterManager.disableOSC;
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
		result.put(\parameters, Array.new);
		result.put(\children, this.children);
		this.parameters.do({arg item;
			var param = this.getParameter(item);
			result[\parameters] = result[\parameters].addAll([
				param.name,
				param.attributes
			]);
		});
		if(this.presets.isEmpty.not, {
			result.put(\presets, this.presetAttributes);
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
				context.parameters;
			},
			'presets?' -> {arg context;
				context.presets;
			},
			'state?' -> {arg context; context.state; },
			'attributes?' -> {arg context; VTMJSON.stringifyAttributes(context.attributes); },
			'reset!' -> {arg context; context.reset; },
			'free!' -> {arg context; context.free; }
		];
	}
}
