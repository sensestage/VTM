VTMAbstractData{
	var <name;
	var <manager;
	var attributes;
	var oscInterface;
	var path;

	classvar viewClassSymbol = \VTMAbstractDataView;

	*managerClass{
		^this.subclassResponsibility(thisMethod);
	}

	*new{arg name, attributes, manager;
		^super.new.initAbstractData(name, attributes, manager);
	}

	*newFromDeclaration{arg attributes, manager;
		var dec = attributes.deepCopy;
		^this.new(dec.removeAt(\name), dec, manager);
	}

	initAbstractData{arg name_, attributes_, manager_;
		name = name_;
		manager = manager_;

		attributes = VTMAttributeManager.newFrom(attributes_);
	}

	free{
		this.disableOSC;
		this.releaseDependants;
		attributes = nil;
		manager = nil;
	}

	*attributesKeys{
		^this.attributeDescriptions.keys;
	}

	*attributeDescriptions{
		^VTMOrderedIdentityDictionary[
			\name -> (type: \string, optional: true),
			\path -> (type: \string, optional: true)
	   	]; 
	}

	attributes{
		^attributes.as(VTMOrderedIdentityDictionary);
	}

	description{
		var result = VTMOrderedIdentityDictionary[
			\attributes -> this.class.attributeDescriptions,
		];
		^result;
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
