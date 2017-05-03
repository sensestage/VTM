VTMComposableContext : VTMContext {
	var <children;

	*new{arg name, attributes, manager, definition;
		^super.new(name, attributes, manager, definition).initComposableContext;
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

	*attributeKeys{
		^super.attributeKeys;
	}

	*commandNames{
		^super.commandNames ++ [\takeOwnership, \releaseOwnership];
	}

	*queryNames{
		^super.queryNames ++ [\children, \parent, \owner, \exclusivelyOwned];
	}
}
