VTMRemoteNetworkNode : VTMElement {
	*managerClass{ ^VTMNetworkNodeManager; }

	*new{arg name, declaration, manager;
		^super.new(name, declaration, manager).initRemoveNetworkNode;
	}

	initRemoveNetworkNode{}

}
