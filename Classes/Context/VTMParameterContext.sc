VTMParameterContext : VTMComposableContext {
	var parameter;

	*new{arg name, parent, declaration, definition;
		^super.new(name, parent, declaration, definition).initRemoteSceneProxy;
	}

	initRemoteSceneProxy{

	}
}
