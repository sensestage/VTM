VTMParameterContext : VTMComposableContext {
	var parameter;
	
	*new{arg name, parent, declaration, defintion;
		^super.new(name, parent, declaration, defintion).initRemoteSceneProxy;
	}

	initRemoteSceneProxy{

	}
}
