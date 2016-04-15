VTMSceneOwner : VTMNodeContext {
	var <sceneFactory;

	*isCorrectChildContextType{arg child;
		^child.isKindOf(VTMScene);
	}

	*new{arg node;
		^super.new('scene', node).initSceneOwner;
	}

	initSceneOwner{
		sceneFactory = VTMSceneFactory.new(this);
	}

	scenes{
		^children;
	}

	addScene{arg newScene;
	}

	loadSceneCue{arg cue;
		var newScene;
		try{
			newScene = sceneFactory.build(cue);
		} {|err|
			"Scene cue build error".warn;
			err.postln;
		};
		this.addScene(newScene);
	}

}