VTMAbstractData{
	var <name;
	var <manager;
	var declaration;
	var attributes;
	var oscInterface;
	var path;

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
		attributes = VTMAttributeManager(this, declaration[\attributes]);
	}

	free{
		this.disableOSC;
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
