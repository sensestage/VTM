VTMComposableContext : VTMContext {
	var <children;

	*new{arg name, declaration, manager, definition;
		^super.new(name, declaration, manager, definition).initComposableContext;
	}

	initComposableContext{
		//TODO: init children here
	}

	free{
		children.do(_.free);
		super.free;
	}

	isSubcontext{
		if(manager.notNil, {
			^this.manager.isKindOf(this.class);
		});
		^false;
	}

	leadingSeparator{
		if(this.isSubcontext,
			{
				^'.';
			}, {
				^'/'
			}
		);
	}

	*declarationKeys{
		^super.declarationKeys;
	}

	*commandNames{
		^super.commandNames ++ [\takeOwnership, \releaseOwnership];
	}

	*queryNames{
		^super.queryNames ++ [\children, \parent, \owner, \exclusivelyOwned];
	}
}
