VTMSelectionParameter : VTMValueParameter {
	var options;

	type{ ^\selection; }

	prDefaultValueForType{ ^[]; }

	isValidType{arg val;
		^val.isKindOf(Array);
	}

	*new{arg name, declaration;
		^super.new(name, declaration).initSelectionParameter;
	}

	initSelectionParameter{
		if(declaration.notNil, {
			if(declaration.includesKey(\options), {
				this.options = declaration[\options];
				if(declaration.includesKey(\defaultValue), {
					this.defaultValue = declaration[\defaultValue];
				});
				if(declaration.includesKey(\value), {
					this.value = declaration[\value];
				});
			});
		});
		if(options.isNil, {
			this.options = [];
		});
	}

	options_{arg val;
		if(val.isArray, {
			options = val;
			if(value.every( {arg item; options.includes(item)} ).not, {
				this.value_([]);
			});
			if(defaultValue.every( {arg item; options.includes(item)} ).not, {
				this.defaultValue_([]);
			});
		}, {
			if(val.isNil, {
				options = [];
				this.value_([]);
				this.defaultValue_([]);
			}, {
				"SelectionParameter:options_ '%' - options must be an array: '%[%]'".format(
					this.fullPath, val, val.class
				).warn;
			});
		});
	}

	options{
		^options.copy;
	}

	value_{arg val;
		if(val.isArray, {
			if(options.notNil and: {options.isEmpty.not}, {
				//check if any items in the selection are not found in the options.
				if(val.every({arg item; options.includes(item)}), {
					super.value_(val, omitTypecheck: true);
				}, {
					"SelectionParameter:value_ '%' - some of the items in the selections was not in options. Ignoring value: '%[%]'".format(
						this.fullPath, val, val.class
					).warn;
				});
			}, {
				super.value_([], omitTypecheck: true);
			});
		}, {
			if(val.isNil, {
				super.value_([], omitTypecheck: true);
			}, {
				"SelectionParameter:value_ '%' - value must be an array of selected option items or nil: '%[%]'".format(
					this.fullPath, val, val.class
				).warn;
			});
		});
	}

	value{
		^value.copy;
	}

	addToSelection{arg val;
		if(options.includes(val), {
			this.value_(this.value.add(val));
		}, {
			"SelectionParameter:addToSelection '%' - val not found in option items: '%[%]'".format(
				this.fullPath, val, val.class
			).warn;
		});
	}

	removeFromSelection{arg val;
		if(value.includes(val), {
			var newVal = this.value;
			newVal.remove(val);
			this.value_(newVal);
		}, {
			"SelectionParameter:removeFromSelection '%' - val not found in selection items: '%[%]'".format(
				this.fullPath, val, val.class
			).warn;
		});
	}

	clear{
		this.value_(nil);
	}

	attributes{
		var result;
		result = super.attributes.putAll(IdentityDictionary[
			\options -> this.options
		]);
		^result;
	}

	*attributeKeys{
		^(super.attributeKeys ++ [\options]);
	}

}