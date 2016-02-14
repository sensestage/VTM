VTMContext {
	var <namespace;
	var <parent;

	*new{arg parent;
		^super.new.initContext(parent);
	}

	initContext{arg parent_;
		parent = parent_;
		namespace = VTMNamespaceElement.new(parent_.namespace).obj_(parent);
	}

	add{arg context;
		namespace.addChild(context.namespace);
	}

	remove{arg contextKey;
		namespace.removeChild(contextKey);
	}
}