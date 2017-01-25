VTMContextProxy : VTMContext {
	var implementation;

	*new{arg name, definition, attributes, parent;
		^super.new(name, definition, attributes, parent).initContextProxy;
	}

	initContextProxy{
		var implClass;
		//determine which implementation to use
		implClass = VTMRemoteContextProxyImplementation;
		//make implementation of correct type //FIXME: only remote proxy implmentation for now.
		implementation = VTMRemoteContextProxyImplementation.new(this, definition, attributes);
	}

	sendMsg{arg ...msg;
		implementation.sendMsg(*msg);
	}
}
