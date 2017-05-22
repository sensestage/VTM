VTMAttribute : VTMValueElement {
	*managerClass{ ^VTMAttributeManager; }

	*new{arg name, declaration, manager;
		^super.new(name, declaration, manager).initAttribute;
	}

	initAttribute{
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
