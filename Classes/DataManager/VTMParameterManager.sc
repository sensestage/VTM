VTMParameterManager : VTMAbstractDataManager{

	reset{arg doAction = true;
		items.do(_.reset(doAction));
	}

	free{
		items.do(_.free);
		super.free;
	}

	ramp{arg ...keyValTimes;
		items.do({arg it;
			it.ramp(*keyValTimes);
		});
	}
}