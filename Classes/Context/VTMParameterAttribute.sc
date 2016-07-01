VTMParameterAttribute : VTMContext {

	*new{arg name, parent, description, defintion;
		^super.new(name, parent, description, defintion).initParameterAttribute;
	}

	initParameterAttribute{

	}
}
