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

	start{
		responders.do(_.enable);
	}

	free{
		responders.do(_.clear);
		responders.do(_.free);
	}

	addResponder{arg msgPath, resp;
		responders = responders.put(msgPath, resp);
	}

	removeResponder{arg msgPath;
		responders.removeAt(msgPath);
	}

	stop{
		responders.do(_.disable);
	}
}
