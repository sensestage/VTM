//A function that can be attached to an environment, e.g. in a context.
VTMCommand : VTMElement {
	var >envir;
	var <>action;

	*new{arg name, attributes, manager;
		^super.new(name, attributes, manager).initCommand;
	}

	initCommand{}

	value{arg ...args;
		if(envir.notNil, {
			envir.use{
				action.value(*args);
			};
		});
	}

	free{
		envir = nil;
		action = nil;
	}
}
