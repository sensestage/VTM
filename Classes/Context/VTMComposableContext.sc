VTMComposableContext : VTMContext {
	var <children;
	var subcontexts, nonSubcontexts;

	*new{arg name, definition, attributes, manager;
		^super.new(name, definition, attributes, manager).initComposableContext;
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
