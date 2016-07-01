VTMContextProxy : VTMContext {
	var remoteAddr;
	var remotePath;

	*new{arg name, parent, description, definition;
		^super.new(name, parent, description, definition).initContextProxy;
	}

	initContextProxy{

	}

	sendMsg{arg msg ...args;
		remoteAddr.sendMsg(remotePath, msg, *args);
	}
}

/*'/aaa/modules/toglyd', 'db', -10, 'pan', -1.0;*/
