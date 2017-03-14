VTMComposableContext : VTMContext {
	var <children;
	var subcontexts, nonSubcontexts;

	*new{arg name, attributes, manager, definition;
		^super.new(name, attributes, manager, definition).initComposableContext;
	}

	initComposableContext{
		"VTMComposableContext initialized".postln;
	}

	isSubcontext{ ^this.parent.isKindOf(this.class); }

	leadingSeparator{
		if(this.isSubcontext,
			{
                    ^'/';
			}, {
                    ^'.'
			}
		);
	}
}
