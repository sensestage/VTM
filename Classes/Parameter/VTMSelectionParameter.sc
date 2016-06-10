VTMSelectionParameter : VTMValueParameter {
	var options;

	prDefaultValueForType{ ^[]; }

	isValidType{arg val;
		^val.isKindOf(Array);
	}

	*new{arg name, description;
		^super.new(name, description).initSelectionParameter;
	}

	initSelectionParameter{
		if(description.notNil, {
			if(description.includesKey(\options), {
				this.options = description[\options];
				if(description.includesKey(\defaultValue), {
					this.defaultValue = description[\defaultValue];
				});
				if(description.includesKey(\value), {
					this.value = description[\value];
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
}