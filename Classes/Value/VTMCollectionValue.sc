VTMCollectionValue : VTMValue {
	var <items;
	var <itemDescription;
	var <maxLength;
	var <minLength;

	*new{arg name, properties;
		^super.new(name, properties).initCollectionValue;
	}

	initCollectionValue{
		if(properties.notEmpty, {
			if(properties.includesKey(\itemDescription), {
				itemDescription = properties[\itemDescription];
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
