VTMNamespaceElement {
	classvar <namespace; //a global cache?
	var <parent;
	var <children;
	var <key;
	var <obj;
	var <searchTokens; // temp getter

	*new{arg parent, key, obj;
		^super.new.init(parent, key, obj);
	}

	*initClass{
		namespace = VTMNamespace;
	}

	init{arg parent_, key_, obj_;
		parent = parent_;
		key = key_;
		obj = obj_;
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