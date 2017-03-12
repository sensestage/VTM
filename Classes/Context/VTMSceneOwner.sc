VTMSceneOwner : VTMComposableContextManager {
	var <sceneFactory;
	*dataClass{ ^VTMScene; }
	name{ ^\scenes; }

	initSceneOwner{
		sceneFactory = VTMSceneFactory.new(this);
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
