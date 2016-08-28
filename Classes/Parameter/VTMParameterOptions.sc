//VTMParameterOptions {
//	//A Dictionary that enumrates options with ascending integers, but
//	//	enumeration can be changed to whatever we like.
//	var options; 
//	var <selection;
//	var <autoAddToItems;
//
//	*new{arg options, restrictToEnum, autoAddToItems;
//		^super.new.init(options, restrictToEnum, autoAddToItems);
//	}
//
//	init{arg options_, restrictToEnum_, autoAddToItems_;
//		if(options_.notNil or: { options.notEmpty }, {
//			options = options_;
//		}, {
//			Error("%:% - Must define 'options' as array with at least one element".format(
//				this.class.name, thisMethod.name
//			)).throw;
//		});
//		restrictToEnum = restrictToEnum_ ? true;
//		selection = options.first;
//		autoAddToItems = autoAddToItems_ ? false;
//	}
//
//	isValidOption{arg val; 
//		var result;
//		result = options.includes(val) and: restrictToEnum;
//		^result;
//	}
//
//	selection_{arg val;
//		if(autoAddToItems, {
//			this.options_(options.add(val));
//		});
//		selection = val;
//	}
//
//	//Options value is an array of values.
//	//The items can also be Associations, in which case the
//	//association key will be the enumeration for the value instead
//	//of an integer.
//	options_{arg val;
//		options = val;
//		this.changed(\options);
//	}
//
//	enum{
//	}
//
//	enum_{arg val;
//	}
//}
