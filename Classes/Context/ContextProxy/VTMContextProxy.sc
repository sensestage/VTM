VTMContextProxy : VTMContext {
	var targetAddr;
	var targetPath;
	var implementation;

	*new{arg name, parent, description, definition;
		^super.new(name, parent, description, definition).initContextProxy;
	}

	initContextProxy{
		var implClass;
		//deterimine which implementation to use
		implClass = VTMRemoteContextProxyImplementation;
		//make implementation of correct type
		implementation = implClass.new(this);
	}

	sendMsg{arg msg ...args;
		targetAddr.sendMsg(targetPath, msg, *args);
	}
}

/*'/aaa/modules/toglyd', 'db', -10, 'pan', -1.0;*/
