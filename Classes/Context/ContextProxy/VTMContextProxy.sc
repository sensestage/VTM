VTMContextProxy : VTMContext {
	var implementation;

	*new{arg name, parent, declaration, definition;
		^super.new(name, parent, declaration, definition).initContextProxy;
	}

	initContextProxy{
		var implClass;
		//determine which implementation to use
		implClass = VTMRemoteContextProxyImplementation;
		//make implementation of correct type //FIXME: only remote proxy implmentation for now.
		implementation = VTMRemoteContextProxyImplementation.new(this, declaration, definition);
	}

	sendMsg{arg ...msg;
		implementation.sendMsg(*msg);
	}
}
