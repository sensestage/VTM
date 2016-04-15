VTMContext : VTMContextRoot {
	var parentContextType;

	*isCorrectParentContextType{arg parent;
		^this.subclassResponsibility(thisMethod);
	}

	*new{arg name, parent;
		^super.new(name).initContext(parent);
	}

	initContext{arg parent_;
		parent = parent_;
		parent.addDependant(this);
		this.addDependant(parent);
	}

	free{
		// parent.removeDependant(this);
		// this.removeDependant(parent);
		// "FREE: %".format("CONTEXT").postln;
		super.free;
	}

	///Method for finding other context elements
	find{arg path;

	}

	update{arg theChanged, whatChanged, theChanger, more;
		// "Context updated: %".format([theChanged, whatChanged, theChanger, more]).postln;
		//is the parent context is freed we need to free this context too
		if(theChanged === this.parent, {
			switch(whatChanged,
				\freed, {
					this.free;
				}
			);
		});
		super.update(theChanged, whatChanged, theChanger, more);
	}
}