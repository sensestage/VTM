VTMListParameter : VTMValueParameter {
	var <itemType; //Which parameter type to contain in this class
	var <itemDescriptions; //The consolidated item descriptionn will be stored here.
	var <items;
	var orderThunk;
	var itemAtThunk, <prItemDict;

	isValidType{arg val;
		^(val.isArray and: val.isString.not);
	}

	prDefaultValueForType{
		^[];//not sure about the default value here
	}

	*new{arg name, description;
		^super.new(name, description).initListParameter;
	}

	initListParameter{
		if(description.notNil, {
			if(description.includesKey(\itemType), {
				itemType = description[\itemType];
			}, {
				Error("ListParameters needs itemType in description. [%]".format(this.fullPath)).throw;
			});
			if(description.includesKey(\itemDescriptions), {
				itemDescriptions = description[\itemDescriptions];
			}, {
				Error("ListParameters needs itemDescriptions in description. [%]".format(this.fullPath)).throw;
			});
		}, {
			Error("ListParameters need description with mandatory attributes: itemType, itemEnum. [%]".format(this.fullPath)).throw;
			^nil;
		});

		//build the internal parameters
		this.prBuildItemParameters;
		orderThunk = Thunk{
			items.collect(_.name);
		};
		//build item dictionary for lookup
		prItemDict = Dictionary.new;
		items.do({arg item;
			prItemDict.put(item.name, item);
		});
	}

	prBuildItemParameters{
		//Check if the items are already built.
		//This forces one to always make a new list parameter if one is
		//already made.
		if(items.isNil, {
			var itemClass, itemDescriptions, attributeKeys;
			var baseItemDesc;
			items = Dictionary.new;
			itemClass = VTMParameter.typeToClass(description[\itemType]);
			itemDescriptions = description[\itemDescriptions];

			//all sub parameters have this base item description
			baseItemDesc = (
				isSubParameter: true
			);

			//Expand all the items in the item description, e.g. arrayed keys etc.
			//All item descriptions should now be expanded into separate Associations
			itemDescriptions = this.class.prExpanditemDescriptions(description[\itemDescriptions].deepCopy);
			attributeKeys = itemClass.attributeKeys.asSet.sect(description.keys);
			itemDescriptions = itemDescriptions.collect({arg itemAssoc, index;
				var itemName, itemDesc, newItemDesc;
				itemName = itemAssoc.key;
				itemDesc = itemAssoc.value;
				newItemDesc = itemDesc.deepCopy;

				//add the values from the outer description that applies to all items of this type.
				//Getting only the keys that pertain to the itemClass, and which are defined in the
				//description.
				itemClass.attributeKeys.asSet.sect(description.keys).do({arg attrKey;
					newItemDesc.put(attrKey, description[attrKey]);
				});

				//add the base item desc, overriding some of the outer description values
				newItemDesc.putAll(baseItemDesc.deepCopy);
				newItemDesc.put(\name, itemName);
				newItemDesc.put(\path, this.fullPath);//using the owner parameter fullPath
				newItemDesc.put(\type, description[\itemType]);

				//override with the values in the itemDescriptions
				newItemDesc.putAll(itemDesc);

				Association.new(itemName, newItemDesc);
			});
			items = itemDescriptions.collect({arg itemDesc;
				VTMParameter.makeFromDescription(itemDesc.value);
			});

		}, {
			Error(
				"ListParameter items already built, please free current and build a new parameter. [%]".format(
					this.fullPath
				)
			).throw;
			^nil;
		});
	}

	*prExpanditemDescriptions{arg desc;
		var result;
		desc.do({arg item, i;
			if(item.isKindOf(Association), {
				// "Parsing association: %".format(item).postln;
				if(item.key.isArray and: {item.isString.not}, {
					// "Expanding association key: %".format(item).postln;
					item.key.do({arg jtem, j;
						var jDesc = ();
						item.value.keysValuesDo({arg ke, va;
							if(va.isArray and: {va.isString.not}, {
								//expand item description value to arrayed key by wrapped indexing
								jDesc.put(
									ke,
									va.wrapAt(j)
								);

							}, {
								jDesc.put(ke, va);
							});
						});
						result = result.add(
							Association.new(
								jtem, jDesc
							);
						);
					});

				}, {
					result = result.add( item );
				});
			}, {
				if(item.isArray and: item.isString.not, {
					result = result.addAll(item);
				}, {
					result = result.add(item);
				});
			});
		});
		//Make all items into Associations with name pointing to a Dictionary
		result = result.collect({arg item;
			var res = item;
			if(item.isKindOf(Association).not, {
				res = Association.new(item, ());
			});
			res;
		});
		^result;
	}

	value{
		var result = Dictionary.new;
		items.collect({arg item;
			result.put(item.name, item.value);
		});
		^result;
	}

	value_{arg val, omitTypecheck = false;

	}

	defaultValue {
		var result = Dictionary.new;
		items.collect({arg item;
			result.put(item.name, item.value);
		});
		^result;
	}

	itemOrder{
		^orderThunk.value;
	}

	at{arg itemName;
		"Checingkngkng: %".format(prItemDict).postln;

		^prItemDict.at(itemName)
	}
}