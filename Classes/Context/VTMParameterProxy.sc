VTMParameterProxy : VTMContext {

	*new{arg name, parent, description, defintion;
		^super.new(name, parent, description, defintion).initParameterProxy;
	}

	initParameterProxy{

	}
}