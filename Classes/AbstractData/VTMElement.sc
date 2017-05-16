VTMElement : VTMAbstractData {
	var parameters;
	var commands;
	var queries;

	*new{arg name, declaration, manager;
		//Element objects must have 'name' in order to generate address path.
		if(name.isNil, {
			Error("% must have name".format(this.class.name)).throw;
		});
		^super.new(name, declaration, manager).initElement;
	}

	initElement{
		this.prInitQueries;
		this.prInitCommands;
		this.prInitParameters;
	}

	prInitParameters{
		parameters = VTMParameterManager(this, declaration[\parameters]);
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

	*parameterDescriptions{	^[]; }
	*commandDescriptions{ ^[]; }
	*queryDescriptions{ ^[]; }

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
		^attributes.at(key).value;
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
