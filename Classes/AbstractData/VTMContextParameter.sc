VTMContextParameter : VTMElement {
	var <valueObj;

	*managerClass{ ^VTMContextParameterManager; }

	*new{arg name, attributes, manager;
		^super.new(name, attributes, manager).initContextParameter;
	}

	initContextParameter{
		if(this.type.notNil, {
			try{
				valueObj = VTMValue.perform(this.type, attributes);
			} {
				Error("[%] - Unknown parameter type: '%'".format(this.fullPath, this.type)).throw;
			}
		});
	}


	action_{arg func;
		valueObj.action = func;
	}

	*attributeKeys{
		^super.attributeKeys ++ VTMValue.attributeKeys;
	}

	attributeKeys{
		^super.attributeKeys ++ valueObj.class.attributeKeys;
	}

	attributes{arg includeDefaultValues = true;
		^super.attributes.deepCopy.putAll(valueObj.attributes(includeDefaultValues));
	}

	type{ ^this.get(\type); }
	valueAction_{arg val; valueObj.valueAction_(val); }

	//Atribute setters and getters
	value{ ^valueObj.value;	}
	value_{arg val; valueObj.value_(val); }

	enabled{ ^valueObj.enabled;	}
	enabled_{arg val; valueObj.enabled_(val); }

	filterRepetitions{ ^valueObj.filterRepetitions;	}
	filterRepetitions_{arg val; valueObj.filterRepetitions_(val); }

	defaultValue{ ^valueObj.defaultValue;	}
	defaultValue_{arg val; valueObj.defaultValue_(val); }

	enum{ ^valueObj.enum;	}
	enum_{arg val; valueObj.enum_(val); }

	restrictValueToEnum{ ^valueObj.restrictValueToEnum;	}
	restrictValueToEnum_{arg val; valueObj.restrictValueToEnum_(val); }

}
