VTMContextProxy {
	var implementation;

	*new{arg name, definition, declaration, manager;
		^super.new().initContextProxy(name, definition, declaration, manager);
	}

	initContextProxy{arg name, definition, declaration, manager;
		//determine which implementation to use
	}

	sendMsg{arg ...msg;
		implementation.sendMsg(*msg);
	}
}
