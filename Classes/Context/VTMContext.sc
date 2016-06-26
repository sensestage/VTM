VTMContext {
	var <name;
	var <parent;
	var description;
	var <definition; //this is not really safe so getter will probably be removed
	var <children;
	var <path, fullPathThunk; //an OSC valid path.
	var <envir;
	var <oscInterface;

	*new{arg name, parent, description, defintion;
		^super.new.initContext(name, parent, description, defintion);
	}

	initContext{arg name_, parent_, description_, definition_;
		if(name_.isNil, {
			Error("Context must have name").throw;
		}, {
			name = name_;
		});
		if(description_.isNil, {
			description = IdentityDictionary.new;
		}, {
			description = IdentityDictionary.newFrom(description_);
		});
		if(definition_.isNil, {
			definition = IdentityDictionary.new;
		}, {
			definition = IdentityDictionary.newFrom(definition_);
		});

		parent = parent_;
		if(parent.notNil, {
			//Have parent add this to its children
			parent.addChild(this);
		});

		children = IdentityDictionary.new;
		envir = Environment.newFrom(definition);
		envir.put(\self, this);
		envir.put(\runtimeDescription, this.description );// a runtime description that can be changed

		fullPathThunk = Thunk.new({
			"/%".format(name).asSymbol;
		});
	}

	free{
		children.keysValuesDo({arg key, child;
			child.free(key);
		});
		this.changed(\freed);
		this.release;
	}

	addChild{arg context;
		children.put(context.name, context);
		context.addDependant(this);
	}

	removeChild{arg key;
		var removedChild;
		removedChild = children.removeAt(key);
		"[%] Removing child '%'".format(this.name, key).postln;
		removedChild.removeDependant(this);
		^removedChild;
	}

	//If path is not defined the name is returned with a leading slash
	fullPath{
		^fullPathThunk.value;
	}

	path_{arg str;
		var newPath = str.copy.asString;
		//add leading slash if not defined
		if(newPath.first != $/, {
			newPath = newPath.addFirst("/");
			"Added leading slash for parameter '%'".format(name).warn;
		});
		path = newPath.asSymbol;
		fullPathThunk = Thunk.new({
			"%/%".format(path, name).asSymbol;
		});
	}

	leadingSeparator{ ^$/;	}

	parent_{arg newParentContext;
		if(parent.notNil, {
			parent.moveContext(this, newParentContext);
		}, {
			parent = newParentContext;
			parent.addChild(this);
		});
	}

	moveChild{arg child, newParentContext;
		var removedChild;
		removedChild = this.removeChild(child);
		if(removedChild.notNil, {
			newParentContext.addChild(removedChild);
		}, {
			"Context: Did not find child: %".format(child).warn;
		});
	}

	isRoot{
		^parent.isNil;
	}

	isLeaf{
		^children.isEmpty;
	}

	root{
		var result;
		//search for context root
		result = this;
		while({result.isRoot.not}, {
			result = result.parent;
		});
		^result;
	}

	//immutable description. Should only be change with 'changeDescription'
	description{
		^description.deepCopy;
	}

	//If not passed in as argument get the description from current envir.
	//If passed as argument use only the described keys to change the current description
	changeDescription{arg newDesc;

	}

	//Save current description to file
	writeDescription{}
	//Read description from file
	readDescription{}

	//forwarding to runtime environment with this module as first arg.
	//Module definition functions always has the module as its first arg.
	//Returns the result
	execute{arg selector ...args;
		^envir[selector].value(this, *args);
	}

	update{arg theChanged, whatChanged, theChanger ...args;
		// "[%] Update: %".format(this.name, [theChanged, whatChanged, theChanger, args]).postln;
		if(theChanged.isKindOf(VTMContext), {
			if(this.children.includes(theChanged), {
				switch(whatChanged,
					\freed, {
						this.removeChild(theChanged.name);
					}
				);
			});
		});
	}

}