VTMElement : VTMAbstractData {
	var <parameters;
	var <commands;
	var <queries;
	var initDeclarationKeys;//for separating init declaration with run-time changes

	*new{arg name, declaration, manager;
		//Element objects must have 'name' in order to generate address path.
		if(name.isNil, {
			Error("% must have name".format(this.class.name)).throw;
		});
		^super.new(name, declaration, manager).initElement;
	}

	initElement{
		initDeclarationKeys = declaration.keys;
		this.prInitParameters;
		this.prInitQueries;
		this.prInitCommands;
	}

	prInitParameters{
		var paramDeclaration = VTMOrderedIdentityDictionary.new;
		this.class.parameterDescriptions.keysValuesDo({arg paramKey, paramDesc;
			paramDeclaration.put(paramKey, paramDesc.deepCopy);
			if(declaration.includesKey(paramKey), {
				paramDeclaration.at(paramKey).put(\value, declaration[paramKey]);
			});
		});
		"Making with it: %".format(paramDeclaration).postln;
		parameters = VTMParameterManager(this, paramDeclaration);
	}

	prInitQueries{
		queries = VTMQueryManager(this, declaration[\queries]);
	}

	prInitCommands{
		commands = VTMCommandManager(this, declaration[\commands]);
	}

	components{
		^[parameters, queries, commands];
	}

	free{
		this.components.do(_.free);
		super.free;
	}

	*declarationKeys{
		var result;
		result = super.declarationKeys;
		result = result.addAll(
			this.parameterDescriptions.collect({arg it; it[\name]})
		);
		^result;
	}

	*parameterDescriptions{
		^VTMOrderedIdentityDictionary[
			\testParam -> (type: \integer)
		];
	}
	*commandDescriptions{ ^VTMOrderedIdentityDictionary[]; }
	*queryDescriptions{ ^VTMOrderedIdentityDictionary[]; }

	description{
		var result = super.description;
		result.putAll(VTMOrderedIdentityDictionary[
			\parameters -> this.class.parameterDescriptions,
			\commands -> this.class.commandDescriptions,
			\queries -> this.class.queryDescriptions
		]);
		^result;
	}

	//set parameter values.
	set{arg key, value;
		var param = parameters[key];
		if(param.notNil, {
			param.valueAction_(value);
		});
	}

	//get parameter(init or run-time) or attribute(init-time) values.
	get{arg key;
		var param = parameters[key];
		if(param.notNil, {
			^param.value;
		});
		//if no parameter found try getting an attribute. 
		^super.get(key);
	}

	//do command with possible value args. Only run-time.
	do{arg key ...args;
		commands[key].do(*args);
	}

	//get query results. Only run-time
	query{arg key;
		^queries[key].value;
	}

}
