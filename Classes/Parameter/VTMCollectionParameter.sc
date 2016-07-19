VTMCollectionParameter : VTMValueParameter {
	var <items;
	var <maxLength;
	var <minLength;

	*new{arg name, declaration;
		^super.new(name, declaration).initCollectionParameter;
	}

	initCollectionParameter{
		items = List.new;
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
