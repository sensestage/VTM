VTMCollectionValue : VTMValue {
	var <items;
	var <itemDescription;
	var <maxLength;
	var <minLength;

	*new{arg name, description;
		^super.new(name, description).initCollectionParameter;
	}

	initCollectionParameter{
		if(description.notEmpty, {
			if(description.includesKey(\itemDescription), {
				itemDescription = description[\itemDescription];
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
