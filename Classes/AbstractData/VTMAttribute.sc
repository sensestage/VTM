VTMAttribute : VTMValueElement {
	*managerClass{ ^VTMAttributeManager; }

	*new{arg name, declaration, manager;
		^super.new(name, declaration, manager).initAttribute;
	}

	initAttribute{
		//add the action
		if(declaration.includesKey(\action), {
			this.action_(declaration[\action]);
		});
	}

	action_{arg func;
		valueObj.action_({
			func.value(this, context);
		});
	}

	value_{arg val;
		valueObj.value_(val);
	}

	valueAction_{arg val;
		valueObj.valueAction_(val);
	}
}
