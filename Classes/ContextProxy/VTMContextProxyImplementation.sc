VTMContextProxyImplementation {
	var context;
	var attributes;
	var definition;

	*new{arg context, definition, attributes;
		^super.new.initContextProxyImplementation(context, definition, attributes);
	}

	initContextProxyImplementation{arg context_, definition_, attributes_;
		context = context_;
		attributes = attributes_;
		definition = definition_;
	}

	sendMsg{arg subpath ...msg;
		this.subclassResponsibility(thisMethod);
	}
}
