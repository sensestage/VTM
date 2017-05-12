VTMElement : VTMAbstractData {
	var oscInterface;
	var commands;
	var path;
	var parameters;
	var commands;
	var queries;

	*new{arg name, attributes, manager;
		//Element objects must have 'name' in order to generate address path.
		if(name.isNil, {
			Error("% must have name".format(this.class.name)).throw;
		});
		^super.new(name, attributes, manager).initElement;
	}

	initElement{
		this.prInitQueries(attributes[\queries]);
		this.prInitCommands(attributes[\commands]);
		this.prInitParameters(attributes[\parameters]);
	}

	prInitParameters{arg attr;
		parameters = VTMParameterManager(this, attr);
	}

	prInitQueries{arg attr;
		queries = VTMQueryManager(this, attr );
	}

	prInitCommands{arg attr;
		commands = VTMCommandManager(this, attr);
	}

	components{
		^super.components ++ [parameters, queries, commands];
	}

	free{
		this.disableOSC;
		this.components.do(_.free);
		super.free;
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

	*commandNames{
		^[];
	}

	*queryNames{
		^[];
	}
}
