VTMComposableContext : VTMContext {
	var subcontexts, nonSubcontexts;

	*new{arg name, parent, declaration, definition;
		^super.new(name, parent, declaration, definition).initComposableContext;
	}

	initComposableContext{
		"VTMComposableContext initialized".postln;
	}

	prepare{
		envir.use{
			~prepare.value(this);
			this.prChangeState(\prepared);
		};
	}

	run{
		envir.use{
			~run.value(this);
			this.prChangeState(\running);
		};
	}

	free{
		envir.use{
			~free.value(this);
		};
		super.free;//superclass changes the state
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
