VTMContextOSCInterface {
	var context;
	var responders;

	*new{arg context;
		^super.new.initContextOSCInterface(context);
	}
	initContextOSCInterface{arg context_;
		context = context_;
		responders = IdentityDictionary.new;
		
		//determine interface messages from context

		//build responders

		//start responders
	}
	start{}
	free{}
	stop{}
}
