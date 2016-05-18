//Abstract base for class for parameter classes.
//This is never instanciated in its own type, but
//should return an object with type of one of its
//subclasses

VTMParameter {
	var <name;
	var <path, fullPathThunk; //an OSC valid path.
	var <description;
	var action, hiddenAction;
	var <enabled = true;
	var <mappings;
	var <responders;
	var <oscInterface;

	//factory type constructor
	//'name' is mandatory. TODO: this may not be the best place to have this check
	*makeFromDescription{arg name, description;
		//if type is defined in description
		//  then determine class from type in description
		//  else if defaultValue is defined
		//    then infer class from defaultValue type (only works for ValueParameter classes
		//if class found
		//  then
		//  Run constructor for found class
		//else
		//  Throw error
		//  return nil
	}

	//This constructor is not used directly, only for testing purposes
	*new{arg name, description;
		if(name.notNil, {
			^super.new.initParameter(name, description);
		}, {
			Error("VTMParameter needs name").throw;
		});
	}

	initParameter{arg name_, description_;
		var tempName = name_.copy.asString;
		if(tempName.first == $/, {
			tempName = tempName[1..];
			"Parameter : removed leading slash from name: %".format(tempName).warn;
		});
		name = tempName.asSymbol;
		description = IdentityDictionary.newFrom(description_);
		fullPathThunk = Thunk.new({
			"/%".format(name).asSymbol;
		});
		if(description.notNil, {
			if(description.includesKey(\path), {
				this.path = description[\path];
			});
			if(description.includesKey(\action), {
				"Setting action from description: %".format(description[\action]).postln;
				this.action_(description[\action]);
			});
			if(description.includesKey(\enabled), {
				//Is enabled by default so only disabled if defined
				if(description[\enabled].not, {
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
			"%/%".format(path, name).asSymbol;
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
		if(aFunction.isClosed, {
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
}