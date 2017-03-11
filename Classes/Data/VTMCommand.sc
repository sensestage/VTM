//A function that can be attached to an environment, e.g. in a context.
VTMElementCommand : VTMElement {
	var >envir;
	var <>action;

	*new{arg func;
		^super.new.initCommand(func);
	}

	initCommand{arg func_;
		action = func_;
	}

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