VTMModule : VTMContext {

	*new{arg host;
		^super.new(host).initModule;
	}

	initModule{
	}

	parameters{
		^namespace.children;
	}

	host{
		^namespace.parent;
	}
}