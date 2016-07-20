VTMContextManager : VTMContext {
	var <definitionPaths;
	var <declarationPaths;

	*new{arg name, parent, declaration, definition;
		^super.new(name, parent, declaration, definition).initContextManager;
	}

	initContextManager {
		definitionPaths = [];
		declarationPaths = [];

		if(declaration.notNil, {
			if(declaration.includesKey(\definitionPaths), {
				var paths = declaration[\definitionPaths].asArray;
				paths.do({arg item; this.addDefinitionPath(item); });
			});

			if(declaration.includesKey(\declarationPaths), {
				var paths = declaration[\declarationPaths].asArray;
				paths.do({arg item; this.addDeclarationPath(item); });
			});
		});
	}

	addDeclarationPath{arg path;
		declarationPaths = declarationPaths.add(path);
		this.changed(\declarationPaths);
	}

	addDefinitionPath{arg path;
		definitionPaths = definitionPaths.add(path);
		this.changed(\definitionPaths);
	}
}
