VTMParameterProxy : VTMContextProxy {

	*new{arg name, definition, attributes, parent;
		^super.new(name, definition, attributes, parent).initParameterProxy;
	}

	initParameterProxy{

	}
}
