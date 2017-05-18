VTMElement : VTMAbstractData {
	var <attributes;
	var <commands;
	var <queries;

	*new{arg name, declaration, manager;
		^super.new(name, declaration, manager).initElement;
	}

	initElement{
		this.prInitAttributes;
		this.prInitQueries;
		this.prInitCommands;
	}

	prInitAttributes{
		var attrDeclaration = VTMOrderedIdentityDictionary.new;
		this.class.attributeDescriptions.keysValuesDo({arg attrKey, attrDesc;
			attrDeclaration.put(attrKey, attrDesc.deepCopy);
			if(declaration.includesKey(attrKey), {
				attrDeclaration.at(attrKey).put(\value, declaration[attrKey]);
			});
		});
		attributes = VTMAttributeManager(this, attrDeclaration);
	}

	prInitQueries{
		queries = VTMQueryManager(this, declaration[\queries]);
	}

	prInitCommands{
		commands = VTMCommandManager(this, declaration[\commands]);
	}

	components{
		^[attributes, queries, commands];
	}

	free{
		this.components.do(_.free);
		super.free;
	}

	*attributeDescriptions{  ^VTMOrderedIdentityDictionary[]; }
	*commandDescriptions{ ^VTMOrderedIdentityDictionary[]; }
	*queryDescriptions{ ^VTMOrderedIdentityDictionary[]; }

	description{
		var result = super.description;
		result.putAll(VTMOrderedIdentityDictionary[
			\attributes -> this.class.attributeDescriptions,
			\commands -> this.class.commandDescriptions,
			\queries -> this.class.queryDescriptions
		]);
		^result;
	}

	//set attribute values.
	set{arg key, value;
		var attr = attributes[key];
		if(attr.notNil, {
			attr.valueAction_(value);
		});
	}

	//get attribute(init or run-time) or parameter(init-time) values.
	get{arg key;
		var attr = attributes[key];
		if(attr.notNil, {
			^attr.value;
		});
		//if no attribute found try getting a parameter
		^super.get(key);
	}

	//do command with possible value args. Only run-time.
	do{arg key ...args;
		commands[key].do(*args);
	}

	//get query results. Only run-time
	query{arg key;
		^queries[key].value;
	}

}
