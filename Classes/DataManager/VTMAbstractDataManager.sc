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

	initAbstractDataManager{arg context_, attributes_;
		context = context_;
		attributes_.do({arg item;
			this.class.dataClass.newFromAttributes(item);
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
