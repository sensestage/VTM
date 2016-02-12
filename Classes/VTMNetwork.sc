VTMNetwork : VTMNodeProxyContext {

	*new{arg node;
		^super.new(node).initNetwork;
	}

	initNetwork{

	}

	nodes{
		^namespace.children; //Returns objects of type VTMNodeProxy
	}

	parent{
		^this; //the network is the global context, so for now we will only return itself.
	}

}