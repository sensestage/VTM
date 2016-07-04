VTMContextProxyImplementation {
	var context;
	var description;
	var definition;

	*new{arg context, description, definition;
		^super.new.initContextProxyImplementation(context, description, definition);
	}

	initContextProxyImplementation{arg context_, description_, definition_;
		context = context_;
		description = description_;
		definition = definition_;
	}

	sendMsg{arg subpath ...msg;
		this.subclassResponsibility(thisMethod);
	}
}
