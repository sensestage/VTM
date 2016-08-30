VTMComposableContext : VTMContext {
	var subcontexts, nonSubcontexts;

	*new{arg name, parent, declaration, definition;
		^super.new(name, parent, declaration, definition).initComposableContext;
	}

	initComposableContext{
		"VTMComposableContext initialized".postln;
	}

	subcontexts{	^subcontexts.value; }
	nonSubcontexts{	^nonSubcontexts.value; }
	isSubcontext{ ^this.parent.isKindOf(this.class); }

	leadingSeparator{
		if(this.isSubcontext,
			{
				^this.class.subcontextSeparator;
			}, {
				^this.class.contextLevelSeparator;
			}
		);
	}

	prInvalidateChildren{
		subcontexts = Thunk({
			children.select({arg item; item.isSubcontext; });
		});
		nonSubcontexts = Thunk({
			children.reject({arg item; item.isSubcontext; });
		});
	}
}
