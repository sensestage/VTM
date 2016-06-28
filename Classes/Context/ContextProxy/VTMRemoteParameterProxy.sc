VTMRemoteParameterProxy : VTMParameterProxy {

	*new{arg name, parent, description, defintion;
		^super.new(name, parent, description, defintion).initRemoteParameterProxy;
	}

	initRemoteParameterProxy{

	}
}