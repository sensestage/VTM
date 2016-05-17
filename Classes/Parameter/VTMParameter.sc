//Abstract base for class for parameter classes.
//This is never instanciated in its own type, but
//should return an object with type of one of its
//subclasses

VTMParameter {
	var <name;
	var path, pathThunk; //an OSC valid path.
	var <description;
	var <action, hiddenAction;
	var <enabled = true;
	var <responders;

	//factory type constructor
	//'name' is mandatory. TODO: this may not be the best place to have this check
	*makeFromDescription{arg name, description;
		//Determine class from type in description
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
		description = description_;
		pathThunk = Thunk.new({
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
	path{
		^pathThunk.value;
	}

	path_{arg str;
		var newPath = str.copy.asString;
		//add leading slash if not defined
		if(newPath.first != $/, {
			newPath = newPath.addFirst("/");
			"Added leading slash for parameter '%'".format(name).warn;
		});
		newPath = newPath.asSymbol;
		pathThunk = Thunk.new({
			"%/%".format(newPath, name).asSymbol;
		});
	}

	action_{arg func;
		//add to hidden action if disabled
		"Setting action 'enabled; is: %, func: %".format(enabled, func).postln;
		if(enabled, {
			action = func;
		}, {
			hiddenAction = func;
		});
	}

	//Enabled by default.
	//Will enable action to be run
	enable{
		if(hiddenAction.notNil, {
			action = hiddenAction;
		});
		hiddenAction = nil;
		enabled = true;
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
}


/*
Types to implement:
- Value
  - Boolean
  - Generic
  - Scalar
    - Integer
    - Decimal
  - Option
  - Symbol
  - String
  - Timecode
  - Dictionary
    - JSON
- Message
- Return
- Array (encapsulates internal types)
  - IntegerArray
  - DecimalArray
  - GenericArray
  - StringArray
  - SymbolArray
  - etc.


*/