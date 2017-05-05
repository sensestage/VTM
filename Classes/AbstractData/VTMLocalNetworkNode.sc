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

	sendMsg{arg hostname, port, path ...data;
		//sending eeeeverything as typed YAML for now.
		NetAddr(hostname, port).sendMsg(path, VTMJSON.stringify(data.unbubble));
	}
}

