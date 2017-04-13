VTMAbstractDataManager {
	var name;
	var context;
	var items;
	var oscInterface;

	*dataClass{
		^this.subclassResponsibility(thisMethod);
	}

	*new{arg context, attributes;
		if(context.isKindOf(VTMContext), {
			^super.new.initAbstractDataManager(context, attributes);
		}, {
			Error("Context argument must be kind of VTMContext").throw;
		});
	}

	//context is an instance of kind VTMContext.
	//attributes is an array of Dictionaries with item attributes.
	initAbstractDataManager{arg context_, attributes_;
		context = context_;
		attributes_.do({arg item;
			var newItem;
			newItem = this.class.dataClass.newFromAttributes(item);
			this.addItem(newItem);
		});
	}

	addItem{arg newItem;
		if(newItem.isKindOf(this.class.dataClass), {//check arg type
			items = items.add(newItem);
		});
	}

	free{
		this.disableOSC;
		items.do(_.free);
		context = nil;
	}

	name{ this.subclassResponsibility(thisMethod); }

	attributes{
		var result;
		items.collect({arg item; 
			item.attributes;
		});
		^result;
	}

	path{
		^"%%%".format(
			context.path,
			this.leadingSeparator,
			this.name
		).asSymbol;
	}

	leadingSeparator{ ^':'; }

	enableOSC{
		//make OSC interface if not already created
		if(oscInterface.isNil, {
			oscInterface = VTMOSCInterface.new(this);
		});
		oscInterface.enable;
	}

	disableOSC{
		oscInterface.free;
		oscInterface = nil;
	}

	oscEnabled{
		^if(oscInterface.notNil, {
			oscInterface.enabled;
		}, {
			^nil;
		});
	}

	*makeOSCAPI{arg obj;
		^IdentityDictionary.new;
	}
}
