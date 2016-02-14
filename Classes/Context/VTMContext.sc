VTMContext {
	var namespaceElement;
	var <parent;

	*new{arg parent;
		^super.new.initContext(parent);
	}

	initContext{arg parent_;
		parent = parent_;
		namespaceElement = VTMNamespaceElement.new(parent_.prNamespaceElement).obj_(parent);
	}

	add{arg context;
		namespaceElement.addChild(context.namespaceElement);
	}

	remove{arg contextKey;
		namespaceElement.removeChild(contextKey);
	}

	//this is a form of 'friend' method for other context objects
	prNamespaceElement{
		^namespaceElement;
	}
}