VTMAbstractData{
	var <name;
	var declaration;
	var <manager;
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
	}

	free{
		this.releaseDependants;
		declaration = nil;
		manager = nil;
	}

	components{
		^nil;
	}

	*declarationKeys{
		^[];
	}

	declarationKeys{
		^this.class.declarationKeys;
	}

	declaration{
		^declaration.copy;
	}

	//can only set parameter values.
	set{arg key, value;
		var val;
		//try to get parameter
		declaration.put(key, value);
		//TODO: set parameter value.
		//parameters[key].valueAction_(value);
	}

	//can only get parameter values.
	get{arg key;
		^declaration.at(key);
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
