VTMSceneFactory{
	var <node;

	*new{arg node;
		^super.new.init(node);
	}

	init{arg node_;
		node = node_;
	}

	build{arg cue;
		^cue;//returnig for now
	}
}