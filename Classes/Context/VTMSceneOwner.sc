VTMSceneOwner : VTMComposableContextManager {
	var <sceneFactory;

	*new{arg definition, attributes, network;
		^super.new('scenes', definition, attributes, network).initSceneOwner;
	}

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
