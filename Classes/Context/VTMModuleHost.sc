VTMModuleHost : VTMNodeContext {
	*new{arg node;
		^super.new(node).initModuleHost;
	}

	initModuleHost {
	}

	modules{
		^namespace.children;
	}
}