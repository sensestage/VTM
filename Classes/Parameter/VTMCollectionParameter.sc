VTMCollectionParameter : VTMValueParameter {
	var <items;
	var <itemDeclarations;
	var <maxLength;
	var <minLength;

	*new{arg name, declaration;
		^super.new(name, declaration).initCollectionParameter;
	}

	initCollectionParameter{
		if(declaration.notEmpty, {
			if(declaration.includesKey(\itemDeclarations), {
				itemDeclarations = declaration[\itemDeclarations];
			});
		});
	}

	addItem{arg val;
		items.add(val);
	}

	removeItem{arg val;
		items.remove(val);
	}

	minLength_{arg val;
		minLength = val;
	}

	maxLength_{arg val;
		maxLength = val;
	}
}
