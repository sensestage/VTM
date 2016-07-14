VTMContext {
	var <name;
	var <parent;
	var declaration;
	var <definition; //this is not really safe so getter will probably be removed
	var <children;
	var path ; //an OSC valid path.
	var fullPathThunk;
	var <envir;
	var <addr; //the address for this object instance.
	var <oscInterface;

	*new{arg name, parent, declaration, definition;
		^super.new.initContext(name, parent, declaration, definition);
	}

	initContext{arg name_, parent_, declaration_, definition_;
		if(name_.isNil, {
			Error("Context must have name").throw;
		}, {
			name = name_;
		});
		if(declaration_.isNil, {
			declaration = IdentityDictionary.new;
		}, {
			declaration = IdentityDictionary.newFrom(declaration_);
			if(declaration.includesKey(\addr), {
				addr = declaration[\addr];
			});
		});

		if(definition_.isNil, {
			definition = IdentityDictionary.new;
		}, {
			definition = IdentityDictionary.newFrom(definition_);
		});
		if(addr.isNil, {
			addr = NetAddr.localAddr;
		}, {
			// a different address is used. Check if UPD port needs to be opened
			if(thisProcess.openPorts.includes(addr.port), {
				"VTMContext - UDP port number already opened for address: %".format(addr).postln;
			}, {
				//try to open this port
				if(thisProcess.openUDPPort(addr.port), {
					"VTMContext - Opened UDP port number for address: %".format(addr).postln;
				}, {
					Error("VTMContext failed to open UDP port number for address: %".format(addr)).throw;
				});
			});
		});

		parent = parent_;
		children = IdentityDictionary.new;
		envir = Environment.newFrom(definition.deepCopy);
		envir.put(\self, this);
		envir.put(\runtimedeclaration, this.declaration );// a declaration that can be changed in runtime.

		fullPathThunk = Thunk.new({
			"/%".format(name).asSymbol;
		});

		if(parent.notNil, {
			//Make parent add this to its children.
			parent.addChild(this);
		});
	}

	free{
		children.keysValuesDo({arg key, child;
			child.free(key);
		});
		this.changed(\freed);
		this.release; //Release this as dependant from other objects.
	}

	addChild{arg context;
		children.put(context.name, context);
		context.addDependant(this);
		this.changed(\addedChild, context.name);
	}

	removeChild{arg key;
		var removedChild;
		removedChild = children.removeAt(key);
		"[%] Removing child '%'".format(this.name, key).postln;
		removedChild.removeDependant(this);
		this.changed(\removedChild, key);
		^removedChild;
	}

	//If path is not defined the name is returned with a leading slash
	fullPath{
		^fullPathThunk.value;
	}

	//Can not set path, it is always determined by its place in the namespace
	//Return only the path, not the name. Use fullPath if that is needed.
	path{arg str;
		//Search parents until root node is found and construct path from that
	}

	//Some objects need to define special separators, e.g. subparameters, submodules etc.
	leadingSeparator{ ^$/;	}

	//Move this context to another parent context
	move{arg newParentContext;
		if(parent.notNil, {
			parent.moveContext(this, newParentContext);
		}, {
			parent = newParentContext;
			parent.addChild(this);
		});
	}

	//Move one of children to another parent context
	moveChild{arg child, newParentContext;
		var removedChild;
		removedChild = this.removeChild(child);
		if(removedChild.notNil, {
			newParentContext.addChild(removedChild);
		}, {
			"Context: Did not find child: %".format(child).warn;
		});
	}

	//Determine if this is a root context, i.e. having no parent.
	isRoot{
		^parent.isNil;
	}

	//Determine is this a lead context, i.e. having no children.
	isLeaf{
		^children.isEmpty;
	}

	//Find the root for this context.
	root{
		var result;
		//search for context root
		result = this;
		while({result.isRoot.not}, {
			result = result.parent;
		});
		^result;
	}

	//Search namespace for context path.
	//Search path can be relative or absolute.
	find{arg searchPath;

	}

	//Get the whole child context tree.
	childTree{
		^children.collect(_.childTree);
	}

	//immutable declaration. Should only be changed with 'changeDeclaration'
	declaration{
		^declaration.deepCopy;
	}

	//If not passed in as argument get the declaration from current envir.
	//If passed as argument use only the described keys to change the current declaration
	changeDeclaration{arg newDesc;

	}

	//Save current declaration to file
	writeDeclaration{

	}

	//Read declaration from file
	readDeclaration{

	}

	//Call functions in the runtime environment with this module as first arg.
	//Module definition functions always has the module as its first arg.
	//The method returns the result from the called function.
	execute{arg selector ...args;
		^envir[selector].value(this, *args);
	}

	makeView{arg declaration;
		var viewClass = this.class.viewClass;
		//override class if defined in declaration.
		if(declaration.notNil, {
			if(declaration.includesKey(\viewClass), {
				viewClass = declaration[\viewClass];
				});
			});
		^viewClass.new(this, declaration);
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
