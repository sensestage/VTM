VTMAbstractDataManager {
	var name;
	var context;
	var items;
	var oscInterface;
	var declaration;

	*dataClass{
		^this.subclassResponsibility(thisMethod);
	}

	*new{arg context, declaration;
		^super.new.initAbstractDataManager(context, declaration);
	}

	//context is an instance of kind VTMContext or a symbol.
	//declaration is an array of Dictionaries with item declaration.
	initAbstractDataManager{arg context_, declaration_;
		context = context_;
		declaration = declaration_;
		items = VTMNamedList.new;
		if(declaration_.notNil, {
			declaration_.do({arg item;
				var newItem;
				newItem = this.class.dataClass.newFromDeclaration(item);
				this.addItem(newItem);
			});
		});
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

	at{arg key;
		^items.at(key);
	}

	isEmpty{ ^items.isEmpty; }

	free{
		this.disableOSC;
		items.do(_.free);
		context = nil;
	}

	names{
		^items.collect(_.name);
	}

	name{ this.subclassResponsibility(thisMethod); }

	declaration{arg recursive;
		var result;
		if(recursive, {
			items.do({arg item;
				result = result.addAll([item.name, item.declaration]);
			});
		}, {
			items.do({arg item;
				result = result.addAll([item.name]);
			});
		});
		^result;
	}

	path{
		if(context.notNil, {
			if(context.isKindOf(VTMContext), {
				^context.fullPath;
			}, {
				"/%".format(context).asSymbol;
			});
		});
		^'/';
	}

	fullPath{
		^(this.path ++ this.leadingSeparator ++	this.name).asSymbol;
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

	*declarationKeys{
		^[];
	}

	*commandNames{
		^[];
	}

	*queryNames{
		^[];
	}

}
