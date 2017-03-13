VTMMapping : VTMElement {
	*new{arg name, attributes, manager;
		^super.new(name, attributes, manager).initMapping;
	}

	initMapping{}

	when{arg what, action;
		//e.g. when parameter \freq > 900, action.value
	}

}
