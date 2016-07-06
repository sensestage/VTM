VTMView : View {
	var <description;
	var <definition;

	*new{arg parent, bounds, description, definition;
		^super.new(parent, bounds).initVTMView(description, definition);
	}

	initVTMView{arg description_, definition_;
		description = description_;
		definition = definition_;
	}

}
