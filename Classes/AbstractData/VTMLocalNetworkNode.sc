VTMLocalNetworkNode : VTMAbstractNetworkNode {
	*new{arg name, attributes, manager;
		^super.new(name, attributes, manager).initLocalNetworkNode;
	}

	initLocalNetworkNode{}

}

