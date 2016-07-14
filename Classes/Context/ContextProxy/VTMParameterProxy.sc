VTMParameterProxy : VTMContextProxy {

	*new{arg name, parent, declaration, defintion;
		^super.new(name, parent, declaration, defintion).initParameterProxy;
	}

	initParameterProxy{

	}
}
