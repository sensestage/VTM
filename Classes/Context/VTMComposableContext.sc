VTMComposableContext : VTMContext {
	var subcontexts, nonSubcontexts;

	*new{arg name, parent, definition, declaration;
		^super.new(name, parent, definition, declaration).initComposableContext;
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

	*loadPrototypesForDefinition{arg definition;
		var result, envirs;
		definition[\prototypes].collect({arg item;
			envirs = envirs.add(item);
		});
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
