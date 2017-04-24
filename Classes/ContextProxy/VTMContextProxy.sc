VTMContextProxy {
	var implementation;

	*new{arg name, definition, attributes, manager;
		^super.new().initContextProxy(name, definition, attributes, manager);
	}

	initContextProxy{arg name, definition, attributes, manager;
		//determine which implementation to use
	}

	sendMsg{arg ...msg;
		implementation.sendMsg(*msg);
	}
}
