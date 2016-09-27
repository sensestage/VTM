//children may be Parameter and Module
VTMModule : VTMComposableContext {

	var submodules;

	*new{arg name, parent, declaration, definition;
		^super.new(name, parent, declaration, definition).initModule;
	}

	initModule{
		// this.makeParameters;

		//it is only the "real" implementation classes that will know when it
		//has been properly initialized
		this.prChangeState(\initialized);
	}

	//Making parameters depends on if the module's definition specifies
	//a ~buildParameters function or a ~parameterDeclarations array.
	//If both are defined they will be combined but the restults of
	//the ~buildParameters function will override what is declared in
	//~parameterDeclarations.
	makeParameters{
		var parametersToBuild = IdentityDictionary.new;
		var buildOrder = [];

		if(definition.includesKey(\parameterDeclarations), {
			definition[\parameterDeclarations].pairsDo({arg paramName, paramDesc;
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

	addParameter{arg parameterName, parameterDeclaration;
		var newParameter;
		newParameter = VTMParameter.makeFromDeclaration(
			parameterName, parameterDeclaration
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

	submodules{	^subcontexts.value; }
	isSubmodule{ ^this.isSubcontext; }
	isParameter{ ^this.isSubcontext.not; }
	host { ^parent; }

}
