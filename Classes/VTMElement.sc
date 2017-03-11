VTMElement : VTMAbstractData {
	var name;
	var commands;
	var oscInterface;

	*new{arg attributes, manager, name;
		if(name.isNil, {
			Error("% must have name".format(this.class.name)).throw;
		});
		^super.new(attributes, manager).initAbstractElement(name);
	}

	initAbstractElement{arg name_;
		name = name_.asSymbol;
	}

	free{
		oscInterface.free;
		commands.free;
		super.free;
	}

}