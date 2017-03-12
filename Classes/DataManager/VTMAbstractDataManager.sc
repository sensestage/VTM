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
		oscInterface = VTMOSCInterface.new(this);
	}

	free{
		items.do(_.free);
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
		);
	}

	leadingSeparator{ ^':'; }
}
