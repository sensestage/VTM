VTMAbstractData{
	var <name;
	var declaration;
	var <manager;
	var parameters;
	var commands;
	var queries;

	classvar viewClassSymbol = \VTMAbstractDataView;

	*managerClass{
		^this.subclassResponsibility(thisMethod);
	}

	*new{arg name, declaration, manager;
		^super.new.initAbstractData(name, declaration, manager);
	}

	initAbstractData{arg name_, declaration_, manager_;
		name = name_;
		manager = manager_;
		declaration = VTMDeclaration.newFrom(declaration_);
		this.prInitQueries(declaration[\queries]);
		this.prInitCommands(declaration[\commands]);
		this.prInitParameters(declaration[\parameters]);
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
		^[parameters, queries, commands];
	}

	free{
		this.components.do(_.free);
		this.releaseDependants;
		declaration = nil;
		manager = nil;
	}

	*parameterKeys{ ^[]; }
	*attributeKeys{ ^[]; }
	*commandKeys{ ^[]; }
	*queryKeys{ ^[]; }
	*declarationKeys{ ^this.attributeKeys ++ this.parameterKeys; }


	declaration{
		^declaration.copy;
	}

	//can only set parameter values.
	set{arg key, value;
		var val;
		//try to get parameter
		declaration.put(key, value);
		//TODO: set parameter value.
		parameters[key].valueAction_(value);
	}

	//can only get parameter values.
	get{arg key;
		^VTMDeclaration.newFrom(declaration).putAll(parameters.declaration);
	}

	makeView{arg parent, bounds, definition, settings;
		var viewClass = this.class.viewClassSymbol.asClass;
		//override class if defined in settings.
		if(settings.notNil, {
			if(settings.includesKey(\viewClass), {
				viewClass = settings[\viewClass];
			});
		});
		^viewClass.new(parent, bounds, definition, settings, this);
	}
}
