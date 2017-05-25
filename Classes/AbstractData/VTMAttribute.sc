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

	value_{arg val;
		valueObj.value_(val);
	}

	valueAction_{arg val;
		valueObj.valueAction_(val);
	}
}
