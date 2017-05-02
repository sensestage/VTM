VTMRemoteNetworkNode : VTMAbstractNetworkNode {
	*managerClass{ ^VTMNetworkNodeManager; }

	*new{arg name, attributes, manager;
		^super.new(name, attributes, manager).initRemoveNetworkNode;
	}

	initRemoveNetworkNode{}

	*attributeKeys{
		^super.attributeKeys ++ [\applications];
	}

	*commandNames{
		^super.commandNames ++ [\start, \stop];
	}
}
