VTMNamespaceElement {
	classvar <globalCache;
	var <parent;
	var <children;
	var <key;
	var <>obj;

	*new{arg parent, key;
		^super.new.init(parent, key);
	}

	init{arg parent_, key_;
		parent = parent_;
		key = key_;
		children = ();
	}

	absolutePath{
		^'an absolute path for this namespace element';
	}

	addChild{arg child;
		children.put(child, "myChild" ++ Date.localtime);
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
		//destructure query into array of tokens
	}
}