VTMSceneOwner : VTMNodeContext {

	*new{arg node;
		^super.new(node).initSceneOwner;
	}

	initSceneOwner{
	}

	scenes{
		^namespace.children;
	}
}