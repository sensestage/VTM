VTMValueElement : VTMElement {
	var valueObj;

	*new{arg name, attributes, manager;
		^super.new(name, attributes, manager).initValueElement;
	}

	initValueElement{
		if(attributes.includesKey(\type), {
			try{
				var type, attr;
				attr = attributes.deepCopy;
				type = attr.at(\type);
				valueObj = VTMValue.makeFromType(type, attr);
			} {
				Error("[%] - Unknown parameter type: '%'".format(this.fullPath, this.type)).throw;
			}
		},{
			Error("[%] - Value type for context parameter not defined: '%'".format(this.fullPath)).throw;
		});

	}

}