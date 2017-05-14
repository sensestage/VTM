VTMValueElement : VTMAbstractData {
	var valueObj;

	*new{arg name, declaration, manager;
		^super.new(name, declaration, manager).initValueElement;
	}

	initValueElement{
		if(declaration.includesKey(\type), {
			try{
				var type, decl;
				decl = declaration.deepCopy;
				type = decl.at(\type);
				//no ValueElement can define action externally.
				decl.removeAt(\action);
				valueObj = VTMValue.makeFromType(type, decl);
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

	value{
		^valueObj.value;
	}
}
