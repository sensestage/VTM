VTMNamespace {
	var <parent;
	var <children;
	var <key;

	*new{arg parent, key;
		^super.new.init(parent, key);
	}

	init{arg parent_, key_;
		parent = parent_;
		key = key_;
		children = ();
	}

	addChild{arg child;
		children.put(child, "myChild" ++ Date.localtime);
	}

	removeChild{arg childKey;
		children.removeAt(childKey);
	}
}