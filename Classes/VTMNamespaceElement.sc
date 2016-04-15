VTMNamespaceElement {
	var <parent;
	var <children;
	var <key;
	var <obj;
	var <searchTokens; // temp getter

	*new{arg parent, key, obj;
		^super.new.init(parent, key, obj);
	}

	*namespace{
		^VTMNamespace.global;
	}

	init{arg parent_, key_, obj_;
		parent = parent_;
		key = key_;
		obj = obj_;
		children = IdentityDictionary.new;
		//add itself to its parents children

		parent !? { parent.addChild(this) };
	}

	absolutePath{
		^'an absolute path for this namespace element';
	}

	addChild{arg child;
		children.put(child.key, child);
	}

	removeChild{arg childKey;
		children.removeAt(childKey);
	}

	send{arg addr;
		var element;
		element = this.find(addr);
		if(element.isNil, {

		}, {

		});
	}

	find{
		// if path is relative
		//   then search child elements
		// else if path is absolute
		//   then ask parent

		//try to find match in global namespace cached paths

		//if found return the instance
		//else do a linked search

		//destructure query into array of tokens
	}
}