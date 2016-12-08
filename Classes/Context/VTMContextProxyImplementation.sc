VTMContextProxyImplementation {
	var context;
	var declaration;
	var definition;

	*new{arg context, definition, declaration;
		^super.new.initContextProxyImplementation(context, definition, declaration);
	}

	initContextProxyImplementation{arg context_, definition_, declaration_;
		context = context_;
		declaration = declaration_;
		definition = definition_;
	}

	sendMsg{arg subpath ...msg;
		this.subclassResponsibility(thisMethod);
	}
}
