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
		attributes = VTMAttributeManager.newFrom(declaration);
		attributes.put(\name, this.name);
	}

	free{
		this.disableOSC;
		this.releaseDependants;
		declaration = nil;
		manager = nil;
	}

	*declarationKeys{
		var result;
		result = this.attributeDescriptions.collect({arg it; it[\name]});
		^result;
	}

	*attributeDescriptions{
		^[
			(name: \name, type: \string),
			(name: \path, type: \string),
	   	]; 
	}

	description{
		var result = VTMOrderedIdentityDictionary[
			\attributes -> this.class.attributeDescriptions,
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
			^attributes.at(\path);
		}, {
			^manager.fullPath;
		});
	}

	hasDerivedPath{
		^manager.notNil;
	}

	get{arg key;
		^attributes.at(key);
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
