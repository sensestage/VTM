VTMView : View {
	var <declaration;
	var <definition;

	*new{arg parent, bounds, declaration, definition;
		^super.new(parent, bounds).initVTMView(declaration, definition);
	}

	initVTMView{arg declaration_, definition_;
		declaration = declaration_;
		definition = definition_;
	}

}
