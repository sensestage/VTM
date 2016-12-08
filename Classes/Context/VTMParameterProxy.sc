VTMParameterProxy : VTMContextProxy {

	*new{arg name, parent, definition, declaration;
		^super.new(name, parent, definition, declaration).initParameterProxy;
	}

	initParameterProxy{

	}
}
