VTMElement : VTMAbstractData {
	var commands;
	var oscInterface;

	*new{arg name, attributes, manager;
		if(name.isNil, {
			Error("% must have name".format(this.class.name)).throw;
		});
		^super.new(name, attributes, manager).initElement;
	}

	initElement{
	}

	free{
		oscInterface.free;
		commands.free;
		super.free;
	}

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
}
