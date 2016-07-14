VTMParameterAttribute : VTMContext {

	*new{arg name, parent, declaration, defintion;
		^super.new(name, parent, declaration, defintion).initParameterAttribute;
	}

	initParameterAttribute{

	}
}
