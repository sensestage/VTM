//children may be Module
VTMModule : VTMComposableContext {

	var submodules;

	*new{arg name, parent, declaration, definition;
		var actualDefinition, actualDeclaration;
		//Check if either definition or declaration are Symobol, in which case
		//they will be looked up in the global vtm library
		if(definition.isKindOf(Symbol), {
			actualDefinition = VTMLibrary.at(\definitions, definition);
		}, {
			actualDefinition = definition;
		});

		actualDefinition = this.makeDefinitionEnvironment(actualDefinition);

		if(declaration.isKindOf(Symbol), {
			actualDeclaration = VTMLibrary.at(\declarations, declaration);
		}, {
			actualDeclaration = declaration;
		});
		^super.new(name, parent, actualDeclaration, actualDefinition).initModule;
	}

	*makeDefinitionEnvironment{arg definition;
		//----Temp hack for audio modules hackaton:
		var result, prototypes;
		if(definition.includesKey(\prototypes), {
			prototypes = definition[\prototypes];
			if(prototypes.first == 'AudioSource', {
				var audioModule, audioSource;
				audioModule = Environment.new;
				audioModule.use{
					~prepare = {arg module, cond;
						// "PREPARE AUDIO MODULE".postln;
						~server = Server.default;
					};
				};

				audioSource = Environment.new(proto: audioModule);
				audioSource.use{
					~prepare = {arg module, cond;
						// "PREPARE AUDIO SOURCE: server: %".format(~server).postln;
						~output = NodeProxy.audio(~server, 2);
						~play = {
							var extraArgs = IdentityDictionary.new;
							// "PLAYING with source: %".format(~source).postln;
							// ~output.source = ~source;

							if(module.envir.includesKey(\initSynthArgs), {
								module.envir[\initSynthArgs].do({arg item;
									var param = module.parameters[item];
									extraArgs.put(param.name, param.value);
								});
							});
							extraArgs = extraArgs.asKeyValuePairs;
							"Extra ARGS: %".format(extraArgs).postln;
							~output.put(0, ~source, extraArgs: extraArgs);
							~output.play;
						};
						~stop = {
							// "STOPPING".postln;
							~output.stop
						};
					};
					~free = {
						~output.stop;
						~output.clear;
					};
				};
				result = Environment.new(proto: audioSource);
				result.putAll(definition);
			});
		}, {
			result = definition;
		});

		///-----End temp hackaton hack


		// var result, tempDef;
		// if(definition.isKindOf(Symbol), {
		// 	tempDef = VTMLibrary.at(\definitions, definition);
		// 	}, {
		// 		tempDef = definition;
		// });
		// //Find and load the prototypes for this definition
		// if(definition.includesKey(\prototypes), {
		// 	var protoLoaderFunc;
		// 	protoLoaderFunc = {arg protoDefName;
		// 		var res;
		// 		res = VTMLibrary.at(\definitions, protoDefName);
		// 		if(res.includesKey(\prototypes), {
		// 			protoLoaderFunc.value(res);
		// 		});
		// 		res;
		// 	};
		// 	if(definition[\prototypes].isArray, {
		// 		//Load all prototypes and its prototypes
		// 		definition[\prototypes].do({arg item;
		// 			result = protoLoaderFunc.value(item);
		// 		});
		// 		}, {
		// 			"Prototypes must be array of Symbols".warn;
		// 	})
		// });
		^result;
	}

	*loadPrototypesForDefinition{arg definition;
		var result, envirs;
		definition[\prototypes].collect({arg item;
			envirs = envirs.add(item);
		});
	}

	initModule{

		//it is only the "real" implementation classes that will know when it
		//has been properly initialized
		this.prChangeState(\initialized);
	}

	//Making parameters depends on if the module's definition specifies
	//a ~buildParameters function or a ~parameterDeclarations array.
	//If both are defined they will be combined but the restults of
	//the ~buildParameters function will override what is declared in
	//~parameterDeclarations.
	// makeParameters{
	// 	var parametersToBuild = IdentityDictionary.new;
	// 	var buildOrder = [];
	//
	// 	if(definition.includesKey(\parameters), {
	// 		definition[\parameters].do({arg paramDeclaration;
	// 			var paramName, paramDesc;
	// 			paramName = paramDeclaration[\name];
	// 			// "Adding param: % to build queue: \n\t%".format(
	// 			// paramName, paramDesc).postln;
	//
	// 			//Avoid adding identically named parameters, warn if happens
	// 			if(parametersToBuild.includesKey(paramName), {
	// 				"Multiple parameters '%' defined for module: '%'".format(paramName, this.name).warn;
	// 				}, {
	// 					buildOrder = buildOrder.add(paramName);
	// 			});
	// 			parametersToBuild.put(paramName, paramDeclaration.as(IdentityDictionary));
	// 		});
	// 	});
	//
	// 	if(definition.includesKey(\buildParameters), {
	// 		var paramDescs = definition[\buildParameters].value(definition, this);
	// 		paramDescs.pairsDo({arg paramName, paramDesc;
	// 			parametersToBuild.put(paramName, paramDesc.as(IdentityDictionary));
	//
	// 			//if the parameter is overridden it still keeps its
	// 			//position in the build order
	// 			if(buildOrder.includes(paramName).not, {
	// 				buildOrder = buildOrder.add(paramName);
	// 			});
	// 		});
	// 	});
	//
	// 	buildOrder.do{arg item;
	// 		this.addParameter(item, parametersToBuild[item]);
	// 	};
	// }

	// addParameter{arg parameterName, parameterDeclaration;
	// 	var newParameter;
	//
	// 	newParameter = VTMParameter.makeFromDeclaration(
	// 		parameterDeclaration.put(\name, parameterName)
	// 	);
	// 	if(newParameter.notNil, {
	// 		this.addChild(newParameter);
	// 		this.prInvalidateChildren;
	// 		}, {
	// 			"Failed to add parameter: \n\t%\n\t%".format(parameterName, parameterDeclaration).postln;
	// 	});
	// }

	play{
		this.execute(\play);
	} //temp for module definition hackaton

	stop{
		this.execute(\stop);
	} //temp for module definition hackaton


	addSubmodule{arg newSubmodule;
		if(newSubmodule.isKindOf(VTMModule), {
			// "ADDING SUBMODULE".postln;
			this.addChild(newSubmodule);
			this.prInvalidateChildren;
		});
		//TODO: Submodular dependancies
	}

	removeSubmodule{arg submodName;
		var removedSubmodule;
		removedSubmodule = this.removeChild(submodName);
		if(removedSubmodule.notNil, {
			removedSubmodule.free;
		});
	}

	submodules{	^subcontexts.value; }
	isSubmodule{ ^this.isSubcontext; }
	host { ^parent; }

}
