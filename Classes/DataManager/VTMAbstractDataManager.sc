//Manages a model
VTMAbstractDataManager {
	var model;
	var definition;
	var buildFunction;
	var items;
	var oscInterface;
	var attributes;

	*dataClass{
		^this.subclassResponsibility(thisMethod);
	}

	*new{arg model, definition, attributes, buildFunction;
		^super.new.initAbstractDataManager(attributes, model, definition, buildFunction);
	}

	initAbstractDataManager{arg attributes_, model_, definition_, buildFunction_;
		attributes = attributes_;
		model = model_;
		definition = definition_ ? IdentityDictionary.new;
		items = IdentityDictionary.newFrom( attributes );
		buildFunction = buildFunction_;
		oscInterface = VTMOSCInterface.new(this);
	}

	at{arg key;
		attributes.at(key);
	}

	names{
		^attributes.keys;
	}

	attributes{
		^attributes;
	}

	attributes_{arg attr;
		//TODO: reinit here
		attributes = attr;
	}

	add{arg key, data;
		attributes.put(key, data);
	}

	remove{arg key, data;

	}

	prepare{arg cond;}
	run{arg cond;}
	free{arg cond;}

	enableOSC{}
	disableOSC{}

	includes{arg key;
		^items.includesKey(key);
	}

	isEmpty{
		^items.isEmpty;
	}

	do{arg action;
		items.do{arg item;
			action.value(item);
		}
	}

	keysValuesDo{arg action;
		items.keysValuesDo({arg item;
			item.value(item);
		});
	}

	fullPath{
		this.subclassResponsibility(thisMethod);
	}

	*makeOSCAPI{arg model;
		this.subclassResponsibility(thisMethod);
	}

}