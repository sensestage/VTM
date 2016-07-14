//Abstract base for class for parameter classes.
//This is never instanciated in its own type, but
//should return an object with type of one of its
//subclasses

VTMParameter {
	var <name;
	var <path, fullPathThunk; //an OSC valid path.
	var <declaration;
	var action, hiddenAction;
	var <enabled = true;
	var <mappings;
	var <responders;
	var <oscInterface;

	var <isSubParameter = false;

	*typeToClass{arg val;
		^"VTM%Parameter".format(val.asString.capitalize).asSymbol.asClass;
	}

	*classToType{arg val;
		^val.name.asString.findRegexp("^VTM(.+)Parameter$")[1][1].toLower;
	}

	type{//only the non-abstract classes will implement this methods
		this.subclassResponsibility(thisMethod);
	}

	//factory type constructor
	//In declaration dict 'name' and 'type' is mandatory.
	*makeFromdeclaration{arg declaration;
		//if 'type' and 'name' is defined in declaration
		if(declaration.includesKey(\name), {
			if(declaration.includesKey(\type), {
				var paramClass = declaration.removeAt(\type);
				var paramName = declaration.removeAt(\name);
				^VTMParameter.typeToClass(paramClass).new(paramName, declaration);
			}, {
				Error("VTMParameter declaration needs type").throw;
			})
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
		declaration = declaration_.deepCopy;
		// declaration = IdentityDictionary.newFrom(declaration_);
		fullPathThunk = Thunk.new({
			if(isSubParameter, {
				".%".format(name).asSymbol;
			}, {
				"/%".format(name).asSymbol;
			});
		});
		if(declaration.notNil, {
			if(declaration.includesKey(\isSubParameter), {
				isSubParameter = declaration[\isSubParameter];
			});
			if(declaration.includesKey(\path), {
				this.path = declaration[\path];
			});
			if(declaration.includesKey(\action), {
				"Setting action from declaration: %".format(declaration[\action]).postln;
				this.action_(declaration[\action]);
			});
			if(declaration.includesKey(\enabled), {
				//Is enabled by default so only disabled if defined
				if(declaration[\enabled].not, {
					this.disable;
				})
			})
		});
	}

	doAction{
		action.value(this);
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

		if(responders.notNil, {
			responders.do(_.free);
		});
		responders = nil;

		this.changed(\freed);
	}

	attributes{
		var aFunction;
		aFunction = this.action;
		if(aFunction.notNil and: {aFunction.isKindOf(Function)} and: {aFunction.isClosed}, {
			//Only return closed functions as attributes
			aFunction = aFunction.asCompileString;
		}, {
			aFunction = nil;
		});

		^IdentityDictionary[
			\name -> this.name,
			\path -> this.path,
			\action -> aFunction,
			\enabled -> this.enabled
		];
	}

	*attributeKeys{
		^[\name, \path, \action, \enabled];
	}

	makeView{arg parent, bounds, declaration, definition;
		^VTMParameterView.makeFromdeclaration(parent, bounds, this, declaration, definition);
	}
}
