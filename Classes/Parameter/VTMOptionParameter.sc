// VTMOptionParameter : VTMValueParameter {
// 	var options;
// 	var <sequenceMode = \clip; //modes: 'clip', 'wrap'
//
// 	type{ ^\option; }
//
// 	isValidType{arg val; ^true }
//
// 	prDefaultValueForType{ ^nil; }
//
// 	*new{arg name, declaration;
// 		^super.new(name, declaration).initOptionParameter;
// 	}
//
// 	initOptionParameter{
// 		if(declaration.notNil, {
// 			if(declaration.includesKey(\options), {
// 				this.options_(declaration[\options]);
// 				//if the declaration has defined value or defaultValue this is the time to set those
// 				if(declaration.includesKey(\defaultValue), {
// 					this.defaultValue_(declaration[\defaultValue]);
// 				});
// 				if(declaration.includesKey(\value), {
// 					this.value_(declaration[\value]);
// 				});
// 			});
// 			if(declaration.includesKey(\sequenceMode), {
// 				sequenceMode = declaration[\sequenceMode];
// 			});
// 		});
// 	}
//
// 	//options can be any kind of data type that responds to ==
// 	options_{arg val;
// 		var newOptions;
// 		if(val.isArray, {
// 			//Remove duplicate options
// 			newOptions = [];
// 			val.do({arg item;
// 				if( newOptions.includes(item).not, {
// 					newOptions = newOptions.add(item);
// 					}, {
// 						"OptionParameter:value_ '%' - removing duplicate option: '%[%]'".format(
// 							this.fullPath, val, val.class
// 						).warn;
// 				});
// 			});
// 			options = newOptions;
// 			if(options.includes(this.defaultValue).not, {
// 				this.defaultValue_(options.first);
// 			});
// 			if(options.includes(this.value).not, {
// 				this.value_(this.defaultValue);
// 			});
//
// 			}, {
// 				if(val.isNil, {
// 					options = val;
// 					this.defaultValue_(nil);
// 					this.value_(nil);
// 					}, {
// 						"OptionParameter:options_ '%' - options value must be an array: '%[%]'".format(
// 							this.fullPath, val, val.class
// 						).warn;
// 				});
// 		});
// 	}
//
// 	defaultValue_{arg val;
// 		if(val.isNil, {
// 			if(options.isNil, {
// 				defaultValue = nil;
// 				}, {
// 					defaultValue = this.options.first;
// 			});
// 			}, {
// 				if(options.notNil, {
// 					if(options.includes(val), {
// 						defaultValue = val;
// 					});
// 					}, {
// 						defaultValue = nil;
// 				});
// 		});
// 	}
//
// 	value_{arg val;
// 		if(options.notNil, {
// 			if(options.includes(val), {
// 				super.value_(val);
// 			});
// 			}, {
// 				if(val.isNil, {
// 					super.value_(nil);
// 				});
// 		});
// 	}
//
// 	options{
// 		^options.copy;
// 	}
//
// 	sequenceMode_{arg mode;
// 		if([\clip, \wrap].includes(mode), {
// 			sequenceMode = mode;
// 			}, {
// 				"OptionParameter:options_ '%' - unknown sequence mode: '%[%]'".format(
// 					this.fullPath, mode, mode.class
// 				).warn;
// 		});
// 	}
//
// 	nextOption{
// 		var indexOfCurrent = options.detectIndex({arg item; item == this.value;});
// 		switch(sequenceMode,
// 			\clip, {
// 				if(indexOfCurrent != (options.size - 1), {
// 					this.value_( options[indexOfCurrent + 1] );
// 				});
// 			},
// 			\wrap, {
// 				this.value_( options[(indexOfCurrent + 1) % options.size] );
// 			}
// 		);
// 		^this.value;
// 	}
//
// 	previousOption{
// 		var indexOfCurrent = options.detectIndex({arg item; item == this.value;});
// 		switch(sequenceMode,
// 			\clip, {
// 				if(indexOfCurrent > 0, {
// 					this.value_( options[indexOfCurrent - 1] );
// 				});
// 			},
// 			\wrap, {
// 				this.value_( options[(indexOfCurrent - 1) % options.size] );
// 			}
// 		);
// 		^this.value;
// 	}
//
// 	attributes{
// 		var result;
// 		result = super.attributes.putAll(IdentityDictionary[
// 			\options -> this.options,
// 			\sequenceMode -> this.sequenceMode
// 		]);
// 		^result;
// 	}
//
// 	*attributeKeys{
// 		^(super.attributeKeys ++ [\options, \sequenceMode]);
// 	}
//
// }