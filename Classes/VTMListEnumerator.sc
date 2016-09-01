//A Dictionary that enumerates a items with ascending integers, but
//	enumeration can be changed to whatever we like.
VTMListEnumerator {
	var items; 
	var enumeration;

	*new{arg items;
		^super.new.init(items);
	}

	init{arg items_;
		if(items_.notNil or: { items.notEmpty }, {
			items = List.newFrom(items_);
		}, {
			Error("%:% - Must define 'items' as a kind of SequenceableCollection with at least one element".format(
				this.class.name, thisMethod.name
			)).throw;
		});
	}

	items{
		^items;
	}

	moveItem{arg what, to;}

	removeItem{arg what;}

	copyItem{arg what, newName;
	}

	renameItem{}
}
