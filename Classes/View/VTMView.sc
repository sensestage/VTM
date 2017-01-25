VTMView : View {
	var <attributes;
	var <definition;
	var >font;

	font{ ^font ? Font("Menlo", 12); }


	*new{arg parent, bounds, definition, attributes;
		"Making VTM view with parent: %".format(parent).postln;
		^super.new(parent, bounds).initView(definition, attributes);
	}

	initView{arg definition_, attributes_;
		attributes = attributes_;
		definition = definition_;
	}

}
