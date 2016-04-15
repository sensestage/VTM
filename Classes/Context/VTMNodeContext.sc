//a context that is running on a node
VTMNodeContext : VTMContext {

	*isCorrectParentContextType{arg parent;
		^parent.isKindOf(VTMNode);
	}

	*new{arg name, node;
		^super.new(name, node).initNodeContext;
	}

	initNodeContext{
	}

	node{ ^parent; }
	network { ^this.node.network; }
}
