/*
A ListValue will have items with arbitrary types.
*/
VTMListValue : VTMCollectionValue {
	var <itemType; //Which parameter type to contain in this class
	var orderThunk;
	var itemAtThunk, <prItemDict;

	isValidType{arg val;
		^(val.isArray and: val.isString.not);
	}

	*type{ ^\list; }

	*prDefaultValueForType{
		^[];
	}

	*new{arg declaration;
		^super.new(declaration).initListParameter;
	}

	initListParameter{
		if(declaration.notEmpty, {
			if(declaration.includesKey(\itemType), {
				itemType = declaration[\itemType];
			});
		});
		//Using decimal as default item type so
		//that list parameter can be made using empty declaration.
		itemType = itemType ? \decimal;
		itemDeclaration = [ () ];

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
		//This forces you to always make a new list parameter if one is
		//already made.
		if(items.isNil, {
			var itemClass, itemDeclaration, declarationKeys;
			var baseItemDesc;
			items = Dictionary.new;
			itemClass = VTMParameter.typeToClass(itemType);

			//all sub parameters have this base item declaration
			baseItemDesc = (
				isSubParameter: true
			);

			//Expand all the items in the item declaration, e.g. arrayed keys etc.
			//All item declaration should now be expanded into separate Associations
			itemDeclaration = this.class.prExpanditemDeclaration(itemDeclaration);
			declarationKeys = itemClass.declarationKeys.asSet.sect(declaration.keys);
			itemDeclaration = itemDeclaration.collect({arg itemAssoc, index;
				var itemName, itemDesc, newItemDesc;
				itemName = itemAssoc.key;
				itemDesc = itemAssoc.value;
				newItemDesc = itemDesc.deepCopy;

				//add the values from the outer declaration that applies to all items of this type.
				//Getting only the keys that pertain to the itemClass, and which are defined in the
				//declaration.
				itemClass.declarationKeys.asSet.sect(declaration.keys).do({arg attrKey;
					newItemDesc.put(attrKey, declaration[attrKey]);
				});

				//add the base item desc, overriding some of the outer declaration values
				newItemDesc.putAll(baseItemDesc.deepCopy);

				//override with the values in the itemDeclaration
				newItemDesc.putAll(itemDesc);

				Association.new(itemName, newItemDesc);
			});

			//Build the item parameter objects
			items = itemDeclaration.collect({arg itemDesc;
				VTMValue.makeFromType(declaration[\itemType], itemDesc);
			});

		}, {
			Error("ListParameter items already built, please free current and build a new parameter.").throw;
		});
	}

	*prExpanditemDeclaration{arg desc;
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
								//expand item declaration value to arrayed key by wrapped indexing
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

	itemOrder{
		^orderThunk.value;
	}

	at{arg itemName;
		^prItemDict.at(itemName)
	}
}
