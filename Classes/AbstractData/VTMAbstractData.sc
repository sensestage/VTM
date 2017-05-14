VTMAbstractData{
	var <name;
	var <manager;
	var declaration;
	var attributes;

	classvar viewClassSymbol = \VTMAbstractDataView;

	*managerClass{
		^this.subclassResponsibility(thisMethod);
	}

	*new{arg name, declaration, manager;
		^super.new.initAbstractData(name, declaration, manager);
	}

	*newFromDeclaration{arg declaration, manager;
		var dec = declaration.deepCopy;
		^this.new(dec.removeAt(\name), dec, manager);
	}

	initAbstractData{arg name_, declaration_, manager_;
		name = name_;
		manager = manager_;

		declaration = VTMDeclaration.newFrom(declaration_);
		this.prInitAttributes;
	}

	prInitAttributes{
		attributes = VTMAttributes(this, declaration[\attributes]);
	}

	free{
		this.releaseDependants;
		declaration = nil;
		manager = nil;
	}

	*declarationKeys{
		var result;
		result = this.attributeDefinitions.collect({arg it; it[\name]});
		^result;
	}

	*attributeDefinitions{ ^[
		(name: \name, type: \string)
	]; }

	description{
		var result = VTMOrderedIdentityDictionary[
			\attributes -> this.class.attributeDefinitions,
		];
		^result;
	}

	declaration{
		^declaration.copy;
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
