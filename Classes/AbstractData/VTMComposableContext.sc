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

	*attributeKeys{
		^super.attributeKeys;
	}

	*commandNames{
		^super.commandNames ++ [\takeOwnership];
	}

	*queryNames{
		^super.queryNames ++ [\children, \parent, \owner, \exclusivelyOwned];
	}
}
