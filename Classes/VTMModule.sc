VTMModule : VTMContext {

	*new{arg host;
		^super.new(host).initModule;
	}

	initModule{
	}

	parameters{
		^namespace.children; // will always return VTMParameter
	}

	host{
		^namespace.parent; // Will always return VTMModuleOwner
	}
}