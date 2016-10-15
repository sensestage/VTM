VTMSceneOwner : VTMNetworkedContext {
	var <sceneFactory;

	*new{arg network, declaration, definition;
		^super.new('scenes', network, declaration, definition).initSceneOwner;
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
