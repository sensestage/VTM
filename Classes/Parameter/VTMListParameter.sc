VTMListParameter : VTMCollectionParameter {
	var <itemType; //Which parameter type to contain in this class
	var orderThunk;
	var itemAtThunk, <prItemDict;

	isValidType{arg val;
		^(val.isArray and: val.isString.not);
	}

	prDefaultValueForType{
		^Dictionary.new;//not sure about the default value here
	}

	*new{arg name, declaration;
		^super.new(name, declaration).initListParameter;
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
		itemDeclarations = [ () ];

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
			var itemClass, itemDeclarations, attributeKeys;
			var baseItemDesc;
			items = Dictionary.new;
			itemClass = VTMParameter.typeToClass(itemType);

			//all sub parameters have this base item declaration
			baseItemDesc = (
				isSubParameter: true
			);

			//Expand all the items in the item declaration, e.g. arrayed keys etc.
			//All item declarations should now be expanded into separate Associations
			itemDeclarations = this.class.prExpanditemDeclarations(itemDeclarations);
			attributeKeys = itemClass.attributeKeys.asSet.sect(declaration.keys);
			itemDeclarations = itemDeclarations.collect({arg itemAssoc, index;
				var itemName, itemDesc, newItemDesc;
				itemName = itemAssoc.key;
				itemDesc = itemAssoc.value;
				newItemDesc = itemDesc.deepCopy;

				//add the values from the outer declaration that applies to all items of this type.
				//Getting only the keys that pertain to the itemClass, and which are defined in the
				//declaration.
				itemClass.attributeKeys.asSet.sect(declaration.keys).do({arg attrKey;
					newItemDesc.put(attrKey, declaration[attrKey]);
				});

				//add the base item desc, overriding some of the outer declaration values
				newItemDesc.putAll(baseItemDesc.deepCopy);
				newItemDesc.put(\name, itemName);
				newItemDesc.put(\path, this.fullPath);//using the owner parameter fullPath
				newItemDesc.put(\type, declaration[\itemType]);

				//override with the values in the itemDeclarations
				newItemDesc.putAll(itemDesc);

				Association.new(itemName, newItemDesc);
			});

			//Build the item parameter objects
			items = itemDeclarations.collect({arg itemDesc;
				VTMParameter.makeFromDeclaration(itemDesc.value);
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

	*prExpanditemDeclarations{arg desc;
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

	value{
		var result = Dictionary.new;
		items.collect({arg item;
			result.put(item.name, item.value);
		});
		^result;
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
		^prItemDict.at(itemName)
	}
}
