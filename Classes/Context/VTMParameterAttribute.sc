VTMParameterAttribute : VTMContext {

	*new{arg name, parent, declaration, definition;
		^super.new(name, parent, declaration, definition).initParameterAttribute;
	}

	initParameterAttribute{

	}
}
