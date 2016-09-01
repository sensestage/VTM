VTMParameterOSCInterface {
	var parameter;
	var responders;

	*new{arg parameter_;
		^super.new.initParameterOSCInterface(parameter_);
	}

	initParameterOSCInterface{arg parameter_;
		parameter = parameter_;
		responders = IdentityDictionary.new;
	}

	enable{
		responders.do(_.enable);
	}

	disable{
		responders.do(_.enable);
	}

	free{
		responders.do(_.free);
	}
}
