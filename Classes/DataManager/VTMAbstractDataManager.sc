//Manages a model
VTMAbstractDataManager {
	var model;
	var definition;
	var attributes;
	var buildFunction;
	var items;
	var oscInterface;

	*dataClass{
		^this.subclassResponsibility(thisMethod);
	}

	*new{arg model, definition, attributes, buildFunction;
		^super.new.initAbstractDataManager(model, definition, attributes, buildFunction);
	}

	initAbstractDataManager{arg model_, definition_, attributes_, buildFunction_;
		model = model_;
		definition = definition_ ? IdentityDictionary.new;
		attributes = attributes_ ? IdentityDictionary.new;
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

}