VTMParameterContext : VTMComposableContext {
	var parameter;
	
	*new{arg name, parent, description, defintion;
		^super.new(name, parent, description, defintion).initRemoteSceneProxy;
	}

	initRemoteSceneProxy{

	}
}
