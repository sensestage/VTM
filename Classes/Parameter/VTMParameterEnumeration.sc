VTMParameterOptions {
	var <items;
	var <selection;
	var <restrictToEnum;
	var <autoAddToItems;

	*new{arg items, restrictToEnum, autoAddToItems;
		^super.new.init(items, restrictToEnum, autoAddToItems);
	}

	init{arg items_, restrictToEnum_, autoAddToItems_;
		if(items_.notNil or: { items.notEmpty }, {
			items = items_;
		}, {
			Error("%:% - Must define 'items' as array with at least one element".format(
				this.class.name, thisMethod.name
			)).throw;
		});
		restrictToEnum = restrictToEnum_ ? true;
		selection = items.first;
		autoAddToItems = autoAddToItems_ ? false;
	}

	isValidOption{arg val; 
		var result;
		result = items.includes(val) and: restrictToEnum;
		^result;
	}

	selection_{arg val;
		if(autoAddToItems, {
			this.items_(items.add(val));
		});
		selection = val;
	}

	items_{arg val;
		items = val;
		this.changed(\items);
	}
}
