VTMNumberParameterView : VTMValueParameterView {
	var <spec;

	*new{arg parent, bounds, parameter, definition, declaration;
		^super.new(parent, bounds, parameter, definition, declaration).initNumberParameterView;
	}

	initNumberParameterView{
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
