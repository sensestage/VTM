VTMSceneOwner : VTMContextManager {
	var <sceneFactory;

	*new{arg network, description, defintion;
		^super.new('scenes', network, description, defintion).initSceneOwner;
	}

	initSceneOwner{
		sceneFactory = VTMSceneFactory.new(this);
	}

	scenes{
		^children;
	}

	network{
		^this.parent;
	}

	application{
		^this.network.application;
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
