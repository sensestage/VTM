VTMRemoteSceneProxy : VTMContextProxy {

	*new{arg name, parent, description, defintion;
		^super.new(name, parent, description, defintion).initRemoteSceneProxy;
	}

	initRemoteSceneProxy{

	}
}
