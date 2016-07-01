VTMContextProxyImplementation {
	var context;

	*new{arg context;
		^super.new.initContextProxyImplementation(context);
	}

	initContextProxyImplementation{arg context_;
		context = context_;
	}

}
