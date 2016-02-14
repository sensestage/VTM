VTMModuleHost : VTMNodeContext {
	*new{arg node;
		^super.new(node).initModuleHost;
	}

	initModuleHost {
	}

	modules{
		^namespaceElement.children;
	}
}
