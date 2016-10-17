VTMView : View {
	var <declaration;
	var <definition;
	var >font;

	font{ ^font ? Font("Menlo", 12); }


	*new{arg parent, bounds, declaration, definition;
		"Making VTM view with parent: %".format(parent).postln;
		^super.new(parent, bounds).initView(declaration, definition);
	}

	initView{arg declaration_, definition_;
		declaration = declaration_;
		definition = definition_;
	}

}
