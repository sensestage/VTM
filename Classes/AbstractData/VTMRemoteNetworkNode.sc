VTMRemoteNetworkNode : VTMAbstractNetworkNode {
	*managerClass{ ^VTMNetworkNodeManager; }

	*new{arg name, declaration, manager;
		^super.new(name, declaration, manager).initRemoveNetworkNode;
	}

	initRemoveNetworkNode{}

	*commandNames{
		^super.commandNames ++ [\start, \stop];
	}

	*queryNames{
		^super.queryNames ++ [\applications];
	}
}
