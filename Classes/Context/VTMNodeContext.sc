//a context that is running on a node
VTMNodeContext : VTMContext {
	*new{arg node;
		^super.new(node).initNodeContext;
	}

	initNodeContext{
	}

	node{
		^namespace.parent;
	}
}