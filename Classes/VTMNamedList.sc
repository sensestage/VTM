//A list where enumeration are numbers from 1 .. N.
//Optionally items can be named with symbols.
//This is not help "store-now-name-later"
//Also stores time of creating items, and time when they are changed.
VTMNamedList {
	var <items;

	*new{arg items, slotNames;
		^super.new.init(items, slotNames);
	}

	init{arg items_, slotNames_;
		if(items_.notNil or: { items.notEmpty }, {
			this.items_(items_, slotNames_);
		}, {
			Error("%:% - Must define 'items' as a kind of SequenceableCollection with at least one element".format(
				this.class.name, thisMethod.name
			)).throw;
		});
	}

	items_{arg val, slotNames;
		var temp;
		temp = val.collect({arg item;
			(data: item, created: Date.getDate)
		});
		items = List.newFrom(temp);
		if(slotNames.notNil, {
			if(slotNames.every({arg it; it.isKindOf(Association)}), {
				slotNames.do({arg mapping;
					var itemToMap = items[mapping.key - 1];
					itemToMap.put(\name, mapping.value);
				});
			}, {
				"ListEnumerator: slot names needs to be a collection of Associations: %".format(slotNames).warn;
			});
		});
	}

	names{
		var result;
		result = items.collect({arg item, i;
			if(item.includesKey(\name), {
				item[\name];
			}, {
				i + 1;
			});
		});
		^result;
	}

	at{arg val;
		var result;
		if(val.isInteger, {
			^items[val - 1][\data];
		}, {
			if(val.isKindOf(Symbol), {
				result = items.detect({arg item;
					if(item.includesKey(\name), {
						item[\name] == val;
					}, {
						false;
					});
				})[\data];
			})
		});
		^result;
	}

	associations{
		var result;
		result = items.collect({arg item, i;
			var itemName;
			itemName = item[\name] ? (i + 1);
			Association.new(itemName, item[\data]);
		});
		^result;
	}

	setItemName{arg number, name;
		var itemToChange = items[number];
		if(itemToChange.notNil, {
			itemToChange.put(\name, name);
		});
	}

	getItems{
		^items.collect({arg it; it[\data]});
	}

	getItemName{arg number;
		var result;
		result = items[number];
		if(result.notNil, {
			result = result[\name];
		}, {
			result = nil;
		});
		^result;
	}

	getItemTimeCreated{arg name;
		var result = items[\name];
		if(result.notNil, {
			^result[\created];
		}, { ^nil });
	}

	getItemTimeLastChanged{
		var result = items[\name];
		if(result.notNil, {
			^result[\changed];
		}, { ^nil });
	}

	changeItem{arg name, data;
		var itemToChange = items[name];
		if(itemToChange.notNil, {
			itemToChange.put(\data, data);
			itemToChange.put(\changed, Date.getDate);
		});
	}

	addItem{arg data, name, slot;//name can also be an integer slot
		var newItem = (data: data, created: Date.getDate);
		if(name.notNil, {
			newItem.put(\name, name);
		});
		if(slot.isNil, {
			//add to tail by default
			items.add(newItem);
		}, {
			items.insert(slot - 1, newItem);
		});
	}

	removeItem{arg name;
		var indexToRemove, removedItem;
		if(items.isEmpty, {^nil});
		if(name.isInteger, {
			indexToRemove = name - 1;
		}, {
			if(name.isKindOf(Symbol), {
				indexToRemove = items.detectIndex({arg it;
					if(it.includesKey(\name), {
						it[\name] == name;
					}, {
						false;
					});
				});
			});
		});
		if(indexToRemove.notNil, {
			removedItem = items.removeAt(indexToRemove);
		});
		^removedItem;
	}

	moveItem{arg name, slot;
		var itemToMove;
		itemToMove = this.removeItem(name);
		if(itemToMove.notNil, {
			items.insert(slot - 1, itemToMove);
		});
	}
}
