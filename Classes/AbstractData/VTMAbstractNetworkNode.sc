VTMAbstractNetworkNode : VTMElement {
	classvar <applications;

	*managerClass{ ^VTMNetworkNodeManager; }

	*new{arg name, declaration, manager;
		^super.new(name, declaration, manager).initAbstractNetworkNode;
	}

	initAbstractNetworkNode{}

	*registerApplication{

	}

	*unregisterApplication{

	}

	*checkApplications{
	}
}
