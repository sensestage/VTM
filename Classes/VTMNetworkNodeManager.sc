VTMNetworkNodeManager : VTMAbstractDataManager {
	
	*dataClass{ ^VTMRemoteNetworkNode; }

	name{ ^\networkNodes; }

	*sendToAll{arg ...args;
	}
}
