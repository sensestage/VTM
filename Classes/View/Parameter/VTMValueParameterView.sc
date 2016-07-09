VTMValueParameterView : VTMParameterView {

	*new{arg parent, bounds, parameter, description, definition;
		^super.new(parent, bounds, parameter, description, definition).initValueParameterView;
	}

	initValueParameterView{
	}

}
