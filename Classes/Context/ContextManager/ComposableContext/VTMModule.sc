VTMModule : VTMComposableContext {
	var <declaration;
	var <definition;
	var <parameterOrder;
	var <defName;
	var parameters;
	var submodules;

	*new{arg name, parent, declaration, defintion;
		^super.new(name, parent, declaration, defintion).initModule;
	}

	initModule{
		envir = definition.deepCopy;
		defName = declaration[\def] ? \none;
		this.makeParameters;

		//it is only the "real" implementation classes that will know when it
		//has been properly initialized
		this.prChangeState(\initialized);
	}

	prepare{
		envir.use{
			~prepareModule.value(this);
			this.prChangeState(\prepared);
		};
	}

	run{
		envir.use{
			~runModule.value(this);
			this.prChangeState(\running);
		};
	}

	free{
		envir.use{
			~freeModule.value(this);
		};
		// "FREE: %".format("MODULE").postln;
		super.free;//superclass changes the state
	}


	//Making parameters depends on if the module's definition specifies
	//a ~buildParameters function or a ~parameterdeclarations array.
	//If both are defined they will be combined but the restults of
	//the ~buildParameters function will override what is declared in
	//~parameterdeclarations.
	makeParameters{
		var parametersToBuild = IdentityDictionary.new;
		var buildOrder = [];

		if(definition.includesKey(\parameterdeclarations), {
			definition[\parameterdeclarations].pairsDo({arg paramName, paramDesc;
				// "Adding param: % to build queue: \n\t%".format(
				// paramName, paramDesc).postln;

				//Avoid adding identically named parameters, warn if happens
				if(parametersToBuild.includesKey(paramName), {
					"Multiple parameters '%' defined for module: '%'".format(paramName, this.name).warn;
					}, {
						buildOrder = buildOrder.add(paramName);
				});
				parametersToBuild.put(paramName, paramDesc.as(IdentityDictionary));
			});
		});

		if(definition.includesKey(\buildParameters), {
			var paramDescs = definition[\buildParameters].value(definition, this);
			paramDescs.pairsDo({arg paramName, paramDesc;
				parametersToBuild.put(paramName, paramDesc.as(IdentityDictionary));

				//if the parameter is overridden it still keeps its
				//position in the build order
				if(buildOrder.includes(paramName).not, {
					buildOrder = buildOrder.add(paramName);
				});
			});
		});

		buildOrder.do{arg item;
			this.addParameter(item, parametersToBuild[item]);
		};
		parameterOrder = buildOrder;
	}

	prInvalidateChildren{
		parameters = Thunk({
			children.select({arg item; item.isKindOf(VTMParameter)});
		});
		submodules = Thunk({
			children.select({arg item; item.isKindOf(VTMModule)});
		});
	}

	addParameter{arg parameterName, parameterdeclaration;
		var newParameter;
		newParameter = VTMParameter.makeFromdeclaration(
			parameterName, parameterdeclaration
		);
		if(newParameter.notNil, {
			this.addChild(newParameter);
			this.prInvalidateChildren;
		});
	}

	addSubmodule{arg newSubmodule;
		if(newSubmodule.isKindOf(VTMModule), {
			"ADDING SUBMODULE".postln;
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

	removeParameter{arg parameterName;
		var removedParameter;
		removedParameter = this.removeChild(parameterName);
		if(removedParameter.notNil, {
			removedParameter.free;
			this.prInvalidateChildren;
		});
	}

	orderedParameters{
		^parameterOrder.collect(this.parameters[_]);
	}

	leadingSeparator{
		var result;
		if(parent.isKindOf(VTMModule), {
			result = $.;
			}, {
				result = $/;
		});
		^result;
	}

	parameters{	^parameters.value; }
	submodules{	^submodules.value; }
	isSubmodule{ ^this.host.isKindOf(VTMModule); }
	host { ^parent; }

}
