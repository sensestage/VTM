//a Singleton class that communicates with the network and manages Applications
VTMLocalNetworkNode : VTMAbstractDataManager {
	classvar <singleton;

	*dataClass{ ^VTMApplication; }
	name{ ^\local; }

	*initClass{
		Class.initClassTree(VTMAbstractData);
		singleton = super.new.initLocalNetworkNode;
	}

	*new{
		^singleton;
	}

	initLocalNetworkNode{}

	leadingSeparator { ^$/; }

}

