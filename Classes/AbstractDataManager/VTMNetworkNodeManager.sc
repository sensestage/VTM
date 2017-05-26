//a singleton class
VTMNetworkNodeManager : VTMAbstractDataManager {

	*dataClass{ ^VTMRemoteNetworkNode; }

	name{ ^\networkNodes; }

	*sendToAll{arg ...args;
	}

	addItemsFromItemDeclarations{arg itemDeclarations;
		"Registering new network nodes: %".format(itemDeclarations).postln;
		super.addItemsFromItemDeclarations(itemDeclarations);
	}
}
