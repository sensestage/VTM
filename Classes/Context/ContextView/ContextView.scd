VTMContextView : View {
	var context;

	*new{arg parent, bounds, context, description;
		^super.new(parent, bounds).initContextView(context, description);
	}
	initContextView{arg context_, description_;
		context = context_;
		description = description_;
	}
}
