VTMContextParameterManager {
	var context;
	var <parameters;
	var <order;

	*new{arg context;
		^super.new.init(context);
	}

	init{arg context_;
		context = context_;
		parameters = IdentityDictionary.new;
		order = [];
	}

	loadParameterAttributes{arg parameterAttributes;
		if(parameterAttributes.notNil, {
			parameterAttributes.do({arg paramAttributes;
				var newParam;
				paramAttributes.put(\path, context.fullPath);
				newParam = VTMParameter.makeFromAttributes(paramAttributes);
				newParam.envir = context.envir;
				if(newParam.notNil, {
					this.addParameter(newParam);
				}, {
					"Failed to build parameter attributes: '%' for context: '%'".format(
						paramAttributes, context.path;
					).warn;
				});
			});
		});
	}

	//TODO: What if param has no path here?
	addParameter{arg param, index;
		if(parameters.includesKey(param.name).not, {
			parameters.put(param.name, param);
			if(index.isNil, {
				order = order.add(param.name);
			}, {
				order.insert(index, param.name);
			});
			context.changed( \addedParameter, param.name );
		}, {
			"Parameter '%' already exists for context '%'".format(param.name, context.path).warn;
		});
	}

	removeParameter{arg paramName;
		var paramToRemove;
		paramToRemove = parameters.removeAt(paramName);
		if(paramToRemove.notNil, {
			parameters.put( paramToRemove.name, paramToRemove );
			order = order.remove(paramToRemove.name);
			context.changed( \removedParameter, paramToRemove.name );
		}, {
			"Couldn't find parameter: '%' in context '%'".format(paramName).warn;
		});

	}

	free{
		parameters.do(_.free);
		order = nil;
		parameters = nil;
		context = nil;
	}

	enableOSC{
		parameters.do({arg item;
			item.enableOSC;
		});
	}

	disableOSC{
		parameters.do({arg item;
			item.disableOSC;
		});
	}
}
