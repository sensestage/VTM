VTMElement : VTMAbstractData {
	var <attributes;
	var <commands;
	var <returns;
	var <signals;

	*new{arg name, declaration, manager;
		^super.new(name, declaration, manager).initElement;
	}

	initElement{
		this.prInitAttributes;
		this.prInitSignals;
		this.prInitReturns;
		this.prInitCommands;

		//TODO: register with LocalNetworkNode singleton.
	}

	prInitAttributes{
		var itemDeclarations = VTMOrderedIdentityDictionary.new;
		this.class.attributeDescriptions.keysValuesDo({arg attrKey, attrDesc;
			itemDeclarations.put(attrKey, attrDesc.deepCopy);
			if(declaration.includesKey(attrKey), {
				itemDeclarations.at(attrKey).put(\value, declaration[attrKey]);
			});
		});
		attributes = VTMAttributeManager(this, itemDeclarations);
	}

	prInitSignals{
		var itemDeclarations = this.class.signalDescriptions.deepCopy;
		signals = VTMSignalManager(this, itemDeclarations);
	}

	prInitReturns{
		var itemDeclarations = this.class.returnDescriptions.deepCopy;
		returns  = VTMReturnManager(this, itemDeclarations);
	}

	prInitCommands{
		var itemDeclarations = this.class.commandDescriptions.deepCopy;
		commands = VTMCommandManager(this, itemDeclarations);
	}

	components{
		^[attributes, returns, signals, commands];
	}

	free{
		this.components.do(_.free);
		super.free;
	}

	*attributeDescriptions{  ^VTMOrderedIdentityDictionary[]; }
	*commandDescriptions{ ^VTMOrderedIdentityDictionary[]; }
	*returnDescriptions{ ^VTMOrderedIdentityDictionary[]; }
	*signalDescriptions{ ^VTMOrderedIdentityDictionary[]; }

	description{
		var result = super.description;
		result.putAll(VTMOrderedIdentityDictionary[
			\attributes -> this.class.attributeDescriptions,
			\commands -> this.class.commandDescriptions,
			\signals -> this.class.signalDescriptions,
			\returns -> this.class.returnDescriptions
		]);
		^result;
	}

	//set attribute values.
	set{arg key, value;
		var attr = attributes[key];
		if(attr.notNil, {
			attr.valueAction_(value);
		});
	}

	//get attribute(init or run-time) or parameter(init-time) values.
	get{arg key;
		var attr = attributes[key];
		if(attr.notNil, {
			^attr.value;
		});
		//if no attribute found try getting a parameter
		^super.get(key);
	}

	//do command with possible value args. Only run-time.
	doCommand{arg key ...args;
		commands[key].do(*args);
	}

	//get return results. Only run-time
	query{arg key;
		^returns[key].value;
	}

	//emits a signal
	//should not be used outside the class.
	//TODO: How to make this method esily avilable from within a
	//context definition, and still protected from the outside?
	emit{arg key...args;
		signals[key].emit(*args);
	}

	return{arg key ...args;
		returns[key].value_(*args);
	}

	onSignal{arg key, func;
		//TODO: Warn or throw if signal not found
		if(signals.includes(key), {
			signals[key].action_(func);
		});
	}

}
