VTMScalarParameterView : VTMValueParameterView {
	var <spec;

	*new{arg parent, bounds, parameter, declaration, definition;
		^super.new(parent, bounds, parameter, declaration, definition).initScalarParameterView;
	}

	initScalarParameterView{
		var min, max;
		if(declaration.notNil, {
			if(declaration.includesKey(\minVal), { min = declaration[\minVal]; });
			if(declaration.includesKey(\maxVal), { max = declaration[\maxVal]; });
		});
		if(min.isNil, { min = parameter.minVal; });
		if(max.isNil, { max = parameter.maxVal; });

		spec = ControlSpec(min, max, step: parameter.stepsize, default: parameter.defaultValue);
	}
}
