VTMAbstractData{
	var <name;
	var attributes;
	var <manager;
	var attributeGetterFunctionsThunk;
	var attributeSetterFunctionsThunk;
	classvar viewClassSymbol = \VTMAbstractDataView;

	*managerClass{
		^this.subclassResponsibility(thisMethod);
	}

	*new{arg name, attributes, manager;
		^super.new.initAbstractData(name, attributes, manager);
	}

	initAbstractData{arg name_, attributes_, manager_;
		name = name_;
		manager = manager_;
		attributes = VTMAttributes.newFrom(attributes_);
	}

	free{
		this.releaseDependants;
		attributes = nil;
		manager = nil;
	}

	components{
		^nil;
	}

	*attributeKeys{
		^[];
	}

	attributeKeys{
		^this.class.attributeKeys;
	}

	attributes{
		^attributes.copy;
	}

	set{arg attributeKey, value;
		attributes.put(attributeKey, value);
		this.changed(\attribute, attributeKey);
	}

	get{arg attributeKey;
		^attributes.at(attributeKey);
	}

	attributeGetterFunctions{
		^attributeGetterFunctionsThunk.value;
	}

	attributeSetterFunctions{
		^attributeSetterFunctionsThunk.value;
	}

	*makeAttributeGetterFunctions{arg data;
		this.subclassResponsibility(thisMethod);
	}

	*makeAttributeSetterFunctions{arg param;
		this.subclassResponsibility(thisMethod);
	}

	makeView{arg parent, bounds, definition, attributes;
		var viewClass = this.class.viewClassSymbol.asClass;
		//override class if defined in attributes.
		if(attributes.notNil, {
			if(attributes.includesKey(\viewClass), {
				viewClass = attributes[\viewClass];
			});
		});
		^viewClass.new(parent, bounds, definition, attributes, this);
	}
}
