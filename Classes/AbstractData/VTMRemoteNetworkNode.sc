VTMRemoteNetworkNode : VTMAbstractNetworkNode {
	*managerClass{ ^VTMNetworkNodeManager; }

	*new{arg name, attributes, manager;
		^super.new(name, attributes, manager).initRemoveNetworkNode;
	}

	initRemoveNetworkNode{}

}
