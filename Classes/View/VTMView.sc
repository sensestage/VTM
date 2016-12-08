VTMView : View {
	var <declaration;
	var <definition;
	var >font;

	font{ ^font ? Font("Menlo", 12); }


	*new{arg parent, bounds, definition, declaration;
		"Making VTM view with parent: %".format(parent).postln;
		^super.new(parent, bounds).initView(definition, declaration);
	}

	initView{arg definition_, declaration_;
		declaration = declaration_;
		definition = definition_;
	}

}
