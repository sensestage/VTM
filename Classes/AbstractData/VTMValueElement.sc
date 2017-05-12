VTMValueElement : VTMElement {
	var valueObj;

	*new{arg name, declaration, manager;
		^super.new(name, declaration, manager).initValueElement;
	}

	initValueElement{
		if(declaration.includesKey(\type), {
			try{
				var type, attr;
				attr = declaration.deepCopy;
				type = attr.at(\type);
				valueObj = VTMValue.makeFromType(type, attr);
			} {
				Error("[%] - Unknown parameter type: '%'".format(this.fullPath, this.type)).throw;
			}
		},{
			Error("[%] - Value type for value element not defined.".format(this.fullPath)).throw;
		});

	}

	*declarationKeys{
		^super.declarationKeys ++ [\type];
	}

	action_{arg func;
		valueObj.action = func;
	}
}