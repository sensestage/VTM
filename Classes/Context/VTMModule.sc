VTMModule : VTMContext {

	*new{arg host;
		^super.new(host).initModule;
	}

	initModule{
	}

	parameters{
		^namespaceElement.children; // will always return VTMParameter
	}

	host{
		^namespaceElement.parent; // Will always return VTMModuleOwner
	}
}
