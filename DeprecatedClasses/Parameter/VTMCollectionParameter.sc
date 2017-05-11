VTMCollectionParameter : VTMValueParameter {
	var <items;
	var <itemAttributes;
	var <maxLength;
	var <minLength;

	*new{arg name, attributes;
		^super.new(name, attributes).initCollectionParameter;
	}

	initCollectionParameter{
		if(attributes.notEmpty, {
			if(attributes.includesKey(\itemAttributes), {
				itemAttributes = attributes[\itemAttributes];
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
