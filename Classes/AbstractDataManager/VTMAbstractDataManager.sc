VTMAbstractDataManager {
	var name;
	var context;
	var items;
	var oscInterface;

	*dataClass{
		^this.subclassResponsibility(thisMethod);
	}

	*new{arg context, attributes;
		^super.new.initAbstractDataManager(context, attributes);
	}

	//context is an instance of kind VTMContext.
	//attributes is an array of Dictionaries with item attributes.
	initAbstractDataManager{arg context_, attributes_;
		context = context_;
		// if(attributes_.notNil, {
		// 	attributes_.do({arg item;
		// 		var newItem;
		// 		newItem = this.class.dataClass.newFromAttributes(item);
		// 		this.addItem(newItem);
		// 	});
		// });
	}

	addItem{arg newItem;
		if(newItem.isKindOf(this.class.dataClass), {//check arg type
			items = items.add(newItem);
		});
	}

	removeItem{arg itemName;
		var indexToRemove;
		indexToRemove = items.detectIndex({arg item; item.name == itemName; });
		if(indexToRemove.notNil, {
			var removedItem;
			removedItem = items.removeAt(indexToRemove);
			removedItem.free;
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
