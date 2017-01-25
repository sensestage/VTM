VTMNumberParameterView : VTMValueParameterView {
	var <spec;

	*new{arg parent, bounds, parameter, definition, attributes;
		^super.new(parent, bounds, parameter, definition, attributes).initNumberParameterView;
	}

	initNumberParameterView{
		var min, max;
		if(attributes.notNil, {
			if(attributes.includesKey(\minVal), { min = attributes[\minVal]; });
			if(attributes.includesKey(\maxVal), { max = attributes[\maxVal]; });
		});
		if(min.isNil, { min = parameter.minVal; });
		if(max.isNil, { max = parameter.maxVal; });

		spec = ControlSpec(min, max, step: parameter.stepsize, default: parameter.defaultValue);
	}
}
