VTMContextProxy : VTMContext {
	var implementation;

	*new{arg name, definition, declaration, parent;
		^super.new(name, definition, declaration, parent).initContextProxy;
	}

	initContextProxy{
		var implClass;
		//determine which implementation to use
		implClass = VTMRemoteContextProxyImplementation;
		//make implementation of correct type //FIXME: only remote proxy implmentation for now.
		implementation = VTMRemoteContextProxyImplementation.new(this, definition, declaration);
	}

	sendMsg{arg ...msg;
		implementation.sendMsg(*msg);
	}
}
