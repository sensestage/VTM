VTMAttributeManager : VTMElementComponent {
	var <parameters;
	var <order;
	var presets;

	*dataClass{ ^VTMParameter; }
	name{ ^\parameters; }

	*new{arg context, declaration;
		^super.new(context, declaration).initParameterManager;
	}

	initParameterManager{
		//presets = VTMPresetManager(declaration[\presets]);
	}
	
	loadParameterDeclaration{arg parameterDeclaration;
		if(parameterDeclaration.notNil, {
			parameterDeclaration.do({arg paramDeclaration;
				var newParam;
				paramDeclaration.put(\path, context.fullPath);
				newParam = VTMParameter.makeFromDeclaration(paramDeclaration);
				newParam.envir = context.envir;
				if(newParam.notNil, {
					this.addParameter(newParam);
				}, {
					"Failed to build parameter declaration: '%' for context: '%'".format(
						paramDeclaration, context.path;
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

	//The presets getter only returns the names of the presets.
	//Usage:
	// myContext.presets;// -> returns preset names
	// myContext.set(myContext.getPreset(\myPresetName));
	presets{
		var result = [];
		if(context.envir.includesKey(\presets), {
			result = result.addAll(context.envir[\presets].collect({arg assoc; assoc.key;}));
		});
		if(presets.notNil, {
			result = result.addAll(presets.names);
		});
		^result;
		// ^result;
	}

	getPreset{arg presetName;
		^presets.at(presetName);
	}

	addPreset{arg data, presetName, slot;
		//if this is the first preset to be added we have to create
		//a presets first
		"Adding preset slot %[%]:\n\t%".format(presetName, slot, data).postln;
		if(presets.isNil, {
			presets = VTMNamedList.new;
		});
		presets.addItem(data, presetName, slot);
		this.changed(\presetAdded, presetName);
	}

	removePreset{arg presetName;
		var removedPreset;
		if(presets.notNil, {
			removedPreset = presets.removeItem(presetName);
			if(removedPreset.notNil, {
				"Context: % - removed preset '%'".format(this.fullPath, presetName).postln;
			});
		}, {
			"Context: % - no presets to remove".format(this.fullPath).warn
		});
	}

	presetDeclaration{
		^presets.asKeyValuePairs;
	}

	names{
		^this.parameters.collect(_.name);
	}
	//END preset methods
}
