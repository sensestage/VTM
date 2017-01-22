//base class for parameter classes.
//Objects of this type has no arguments/values but works
//as a 'command' to perform the defined action.

VTMParameter {
	var <name;
	var <path, fullPathThunk; //an OSC valid path.
	var <declaration;
	var action, hiddenAction;
	var <enabled = true;
	var <mappings;
	var <oscInterface;
	var <>willStore = true;
	var <>onlyReturn = false;
	var <isSubParameter = false;
	var >envir;
	var attributeGetterFunctionsThunk;
	var attributeSetterFunctionsThunk;

	*typeToClass{arg val;
		^"VTM%Parameter".format(val.asString.capitalize).asSymbol.asClass;
	}

	*classToType{arg val;
		^val.name.asString.findRegexp("^VTM(.+)Parameter$")[1][1].toLower;
	}

	*type{
		this.subclassResponsibility(thisMethod);
	}

	type{
		^this.class.type;
	}

	//factory type constructor
	//In declaration dict 'name' and 'type' is mandatory.
	*makeFromDeclaration{arg declaration;
		var decl = declaration.deepCopy;
		//if 'type' and 'name' is defined in declaration
		if(decl.includesKey(\name), {
			if(decl.includesKey(\type), {
				var paramClass = decl.removeAt(\type);
				var paramName = decl.removeAt(\name);
				^VTMParameter.typeToClass(paramClass).new(paramName, decl);
			}, {
				Error("VTMParameter declaration needs type").throw;
			});
		}, {
			Error("VTMParameter declaration needs name").throw;
		});
	}

	//This constructor is not used directly, only for testing purposes
	*new{arg name, declaration;
		if(name.notNil, {
			^super.new.initParameter(name, declaration);
		}, {
			Error("VTMParameter needs name").throw;
		});
	}

	initParameter{arg name_, declaration_;
		var tempName = name_.copy.asString;
		if(tempName.first == $/, {
			tempName = tempName[1..];
			"Parameter : removed leading slash from name: %".format(tempName).warn;
		});
		name = tempName.asSymbol;
		if(declaration_.notNil, {
			declaration = declaration_.deepCopy;
		}, {
			declaration = IdentityDictionary.new;
		});
		// declaration = IdentityDictionary.newFrom(declaration_);
		fullPathThunk = Thunk.new({
			if(isSubParameter, {
				".%".format(name).asSymbol;
			}, {
				"/%".format(name).asSymbol;
			});
		});
		if(declaration.notEmpty, {
			if(declaration.includesKey(\isSubParameter), {
				isSubParameter = declaration[\isSubParameter];
			});
			if(declaration.includesKey(\path), {
				this.path = declaration[\path];
			});
			if(declaration.includesKey(\action), {
				// "Setting action from declaration: %".format(declaration[\action]).postln;
				this.action_(declaration[\action]);
			});
			if(declaration.includesKey(\enabled), {
				//Is enabled by default so only disabled if defined
				if(declaration[\enabled].not, {
					this.disable;
				})
			});
			if(declaration.includesKey(\willStore), {
				willStore = declaration[\willStore];
			});
			if(declaration.includesKey(\onlyReturn), {
				onlyReturn = declaration[\onlyReturn];
			});
		});

		//lazy attributesGetters and setters
		attributeGetterFunctionsThunk = Thunk({
			this.class.makeAttributeGetterFunctions(this);
		});
		attributeSetterFunctionsThunk = Thunk({
			this.class.makeAttributeSetterFunctions(this);
		});
	}

	doAction{
		if(envir.notNil, {
			envir.use{action.value(this)};
		}, {
			action.value(this);
		});
	}

	//If path is not defined the name is returned with a leading slash
	fullPath{
		^fullPathThunk.value;
	}

	path_{arg str;
		var newPath = str.copy.asString;
		//add leading slash if not defined
		if(newPath.first != $/, {
			newPath = newPath.addFirst("/");
			"Added leading slash for parameter '%'".format(name).warn;
		});
		path = newPath.asSymbol;
		fullPathThunk = Thunk.new({
			if(isSubParameter, {
				"%.%".format(path, name).asSymbol;
			}, {
				"%/%".format(path, name).asSymbol;
			});
		});
	}

	action_{arg func;
		//add to hidden action if disabled
		if(enabled, {
			action = func;
		}, {
			hiddenAction = func;
		});
	}

	action{
		if(enabled, {
			^action;
		}, {
			^hiddenAction;
		});
	}

	//Enabled by default.
	//Will enable action to be run
	enable{arg doActionWhenEnabled = false;
		if(hiddenAction.notNil, {
			action = hiddenAction;
		});
		hiddenAction = nil;
		enabled = true;
		if(doActionWhenEnabled, {
			this.doAction;
		});
	}

	//Will disable action from being run
	disable{
		//only happens when action is defined
		if(action.notNil, {
			hiddenAction = action;
			action = nil;
		});
		enabled = false;
	}

	free{
		action = nil;
		hiddenAction = nil;
		if(mappings.notNil, {
			mappings.do(_.free);
		});
		mappings = nil;

		if(oscInterface.notNil, {
			oscInterface.free;
		});
		oscInterface = nil;

		this.changed(\freed);
	}

	attributes{
		var result;
		result = IdentityDictionary.new;
		this.class.attributeKeys.do({arg attrKey;
			var val;
			val = this.attributeGetterFunctions[attrKey].value;
			//outside the class, i.e. "in real life", there is no such thing as a Symbol,
			//that data type is only relevant in the context of sclang code.
			//Thus we turn all values into strings when thing come in and out of a Parameter.
			//When sending attributes over OSC to other nodes/application the problem with distinguishing
			//between symbols and strings becomes apparent. That is the reason for doing this.
			//Attributes is always pertaining to the "outside world" of the sclang code domain.
			if(val.isKindOf(Symbol), {
				val = val.asString;
			});
			result.put(
				attrKey,
				val
			);
		});
		^result;
	}

	attributeGetterFunctions{
		^attributeGetterFunctionsThunk.value;
	}

	attributeSetterFunctions{
		^attributeSetterFunctionsThunk.value;
	}

	*attributeKeys{
		^[\name, \path, \action, \enabled, \type];
	}

	*makeAttributeGetterFunctions{arg param;
		var result;
		result = IdentityDictionary[
			\name -> {param.name;},
			\path -> {param.path;},
			\action -> {
				var aFunction;
				aFunction = param.action;
				if(aFunction.notNil and: {aFunction.isKindOf(Function)} and: {aFunction.isClosed}, {
					//Only return closed functions as attributes
					aFunction = aFunction.asCompileString;
				}, {
					aFunction = nil;
				});
				aFunction;
			},
			\enabled -> {param.enabled;},
			\type -> {param.type;}
		];
		^result;
	}

	*makeAttributeSetterFunctions{arg param;
		var result;
		result = IdentityDictionary.new;
		^result;
	}

	makeView{arg parent, bounds, definition, declaration;
		^VTMParameterView.makeFromDeclaration(parent, bounds, definition, declaration, this);
	}

	*makeOSCAPI{arg param;
		var result = IdentityDictionary.new;

		//make query getters for attributes
		param.attributeGetterFunctions.keysValuesDo({arg key, getFunc;
			result.put(
				"%?".format(key).asSymbol,
				getFunc
			);
		});
		//make setters for attributes
		result.putAll(param.attributeSetterFunctions);
		^result;
	}

	enableOSC{
		if(oscInterface.isNil, {
			oscInterface = VTMParameterOSCInterface(this);
		});
		oscInterface.enable;
	}

	disableOSC{
		if(oscInterface.notNil, {
			oscInterface.free;
			oscInterface = nil;
		});
	}
}
