VTMElement : VTMAbstractData {
	var oscInterface;
	var path;
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
		this.disableOSC;
		super.free;
	}

	*declarationKeys{
		var result;
		result = super.declarationKeys;
		result = result.addAll(
			this.parameterDefinitions.collect({arg it; it[\name]})
		);
		^result;
	}

	*parameterDefinitions{	^[]; }
	*commandDefinitions{ ^[]; }
	*queryDefinitions{ ^[]; }

	description{
		var result = super.description;
		result.putAll(VTMOrderedIdentityDictionary[
			\parameters -> this.class.parameterDefinitions,
			\commands -> this.class.commandDefinitions,
			\queries -> this.class.queryDefinitions
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

	fullPath{
		^(this.path ++ this.leadingSeparator ++ this.name).asSymbol;
	}

	path{
		if(manager.isNil, {
			^path;
		}, {
			^manager.fullPath;
		});
	}

	path_{arg val;
		if(manager.isNil, {
			if(val.notNil, {
				if(val.asString.first != $/, {
					val = ("/" ++ val).asSymbol;
				});
				path = val.asSymbol;
			}, {
				path = nil;
			});

			//TODO: update/rebuild responders upon changed path, if manually set.
			//osc interface will be an observer of this object and update its responders.
			this.changed(\path, path);
		}, {
			"'%' - Can't set path manually when managed".format(this.fullPath).warn;
		});
	}

	hasDerivedPath{
		^manager.notNil;
	}

	leadingSeparator{ ^'/'; }

	enableOSC{
		//make OSC interface if not already created
		if(oscInterface.isNil, {
			oscInterface = VTMOSCInterface.new(this);
		});
		oscInterface.enable;
	}

	disableOSC{
		if(oscInterface.notNil, { oscInterface.free;});
		oscInterface = nil;
	}

	oscEnabled{
		^if(oscInterface.notNil, {
			oscInterface.enabled;
		}, {
			^nil;
		});
	}
}
