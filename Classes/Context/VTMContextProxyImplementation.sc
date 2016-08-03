VTMContextProxyImplementation {
	var context;
	var declaration;
	var definition;

	*new{arg context, declaration, definition;
		^super.new.initContextProxyImplementation(context, declaration, definition);
	}

	initContextProxyImplementation{arg context_, declaration_, definition_;
		context = context_;
		declaration = declaration_;
		definition = definition_;
	}

	sendMsg{arg subpath ...msg;
		this.subclassResponsibility(thisMethod);
	}
}
