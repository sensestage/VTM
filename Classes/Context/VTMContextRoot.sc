/*
A context is a manager for its children elements.
It has type check abilities, every context type can only have a defined type of children.
It notifies dependants when child elements are removed and/or added.
*/
VTMContextRoot {
	var <name;
	var children;
	var <parent; //will always return nil for context root
	var <state = \none;
	var path;

	*isCorrectChildContextType{arg child;
		^this.subclassResponsibility(thisMethod);
	}

	*new{arg name;
		^super.new.initContextRoot(name);
	}

	initContextRoot{arg name_;
		name = name_;
		children = IdentityDictionary.new;
	}

	free{
		// "FREE: %".format("CONTEXT ROOT").postln;
		this.prChangeState(\freed);
		this.dependants.do({arg dep;
			dep.removeDependant(this);
			this.removeDependant(dep);
		});
	}

	addChild{arg childContext;
		if(this.class.isCorrectChildContextType(childContext), {
			// if(true/*Will add type check here*/, {
			children.put(childContext.name, childContext);
			childContext.addDependant(this);
			this.addDependant(childContext);
		}, {
			Error("Tried to add child context of wrong type").throw;
		});
	}

	//Return the removed object if child found. Returns nil if not found.
	removeChild{arg contextKey;
		var removedChild = children.removeAt(contextKey);
		removedChild.removeDependant(this);
		this.removeDependant(removedChild);
		//free the child if not already freed
		if(removedChild.state != \freed, {
			removedChild.free;
		});
		^removedChild;
	}

	children{
		^children.values.asArray;
	}

	childrenNames{
		^children.keys.asArray;
	}

	isLeafContext{
		^children.every({arg item; item.isKindOf(VTMContext).not});
	}

	prChangeState{arg newState;
		if(newState != state, {
			state = newState;
			this.changed(newState);
		});
	}

	leadingSeparator{
		^$/;
	}

	path{
		if(path.isNil, {
			var result;
			var nextObj = this;
			while({nextObj.parent.notNil and: { nextObj.parent.class != VTMContextRoot} }, {
				result = result.add(nextObj.name);
				result = result.add(nextObj.leadingSeparator);
				nextObj = nextObj.parent;
			});
			path = result.reverse.join;
		});
		^path;
	}

	update{arg theChanged, whatChanged, theChanger, more;
		// "ContextRoot updated: %".format([theChanged, whatChanged, theChanger, more]).postln;
		if(theChanged.isKindOf(VTMContextRoot), {
			switch(whatChanged,
				\freed, {
					if(children.includesKey(theChanged.name), {
						// "A CHILD WAS FREED: %".format(theChanged.name).postln;
						this.removeChild(theChanged.name);
					});
				},
				\initialized, {
					// "A CHILD IS BORN: %".format(theChanged.name).postln;
					if(children.includesKey(theChanged.name), {
						// "\tAND NEED TO BE REPLACED: %".format(theChanged.name).postln;
						this.removeChild(theChanged.name);
					});

					this.addChild(theChanged);

				}
			);
		});
	}
}