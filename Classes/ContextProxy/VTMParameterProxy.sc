VTMParameterProxy : VTMContextProxy {

	*new{arg name, definition, declaration, parent;
		^super.new(name, definition, declaration, parent).initParameterProxy;
	}

	initParameterProxy{

	}
}
