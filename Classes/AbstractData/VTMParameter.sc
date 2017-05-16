VTMParameter : VTMValueElement {
	*managerClass{ ^VTMParameterManager; }

	*new{arg name, declaration, manager;
		^super.new(name, declaration, manager).initParameter;
	}

	initParameter{
	}

	action_{arg val;
		valueObj.action_(val);
	}

	value_{arg val;
		valueObj.value_(val);
	}

	valueAction_{arg val;
		valueObj.valueAction_(val);
	}
}
