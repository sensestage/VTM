VTMScalarParameterView : VTMValueParameterView {
	var <spec;

	*new{arg parent, bounds, parameter, description, definition;
		^super.new(parent, bounds, parameter, description, definition).initScalarParameterView;
	}

	initScalarParameterView{
		var min, max;
		if(description.notNil, {
			if(description.includesKey(\minVal), { min = description[\minVal]; });
			if(description.includesKey(\maxVal), { max = description[\maxVal]; });
		});
		if(min.isNil, { min = parameter.minVal; });
		if(max.isNil, { max = parameter.maxVal; });

		spec = ControlSpec(min, max, step: parameter.stepsize, default: parameter.defaultValue);
	}
}
