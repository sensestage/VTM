TestVTMContext : UnitTest {
	setUp{}

	tearDown{}

	test_Construction{
		var testDesc = IdentityDictionary[\testObj -> 33];
		var testDef = IdentityDictionary[\bongo -> 8383, \brexit -> {"So you wanna leave?".postln;}];
		var context = VTMContext.new('myRoot', nil, testDesc, testDef);

		this.assert(
			context === context.root,
			"Context root is itself"
		);

		this.assertEquals(
			context.children, IdentityDictionary.new,
			"Context initialized to empty IdentityDictionary"
		);

		this.assert(
			context.description == testDesc and: {context.description !== testDesc},
			"Context set description to equal, but not identical description."
		);

		this.assert(
			context.definition == testDef and: {context.definition !== testDef},
			"Context set definition to equal, but not identical definition."
		);
	}

	test_EnvirExecute{
		var wasRun = false, itself, theArgs;
		var testArgs = [11,22,\hello];
		var testDesc = IdentityDictionary[\testObj -> 33];
		var testDef = IdentityDictionary[\bongo -> 8383, \brexit -> {|context ...args|"So you wanna leave?".postln; wasRun = true; theArgs = args; itself = context;}];
		var context = VTMContext.new('myRoot', nil, testDesc, testDef);

		context.execute(\brexit, *testArgs);
		this.assert(
			wasRun, "Context did run function"
		);

		this.assert(
			itself === context, "Context passed itself to the envir function"
		);

		this.assertEquals(
			theArgs, testArgs, "Context passed correct arguments"
		);

	}

	test_nodeManagement{
		var root = VTMContext.new('myRoot');
		var app = VTMContext.new('myApp', root);
		var module, moduleObj;

		this.assert(
			root.children.includes(app),
			"Context added app to its children"
		);

		//should notify the parent upon free
		app.free;
		this.assert(
			root.children.includes(app).not,
			"Context removed app from its children"
		);

		//If the root context is freed it should remove all node context, and this should
		//propagate down the context tree

		//Make three level context tree (chain)
		app = VTMContext.new('myOtherApp', root);
		module = VTMContext.new('myModule', root);

		//Should free all node contexts, i.e. 'myModule' and 'myOtherApp'
		root.free;

		this.assert(
			root.children.includes(app).not,
			"Context removed first level node"
		);

		this.assert(
			app.children.includes(module).not,
			"Context removed second level node"
		);
	}

	test_MultiLevelChildManagament{
		var root;
		var children;

		//using empty Event as bogus obj
		root = VTMContext.new('myRoot');

		//Make a three level context tree
		3.do({arg i;
			var iNode = VTMContext.new("node_%".format(i).asSymbol, root);
			children = children.add(iNode);
			3.do({arg j;
				var jNode = VTMContext.new("node_%_%".format(i, j).asSymbol, iNode);
				children = children.add(jNode);
				4.do({arg k;
					var kNode = VTMContext.new("node_%_%_%".format(i, j, k).asSymbol, jNode);
					children = children.add(kNode);
				});
			});
		});

		//All nodes must have same root
		this.assert(
			children.collect({arg item;
				item.root === root;
			}).every({arg it; it}),
			"Context children all have the same root"
		);

		//freeing root also frees child nodes
		root.free;
		this.assert(
			children.collect({arg item;
				//The nodes should no longer have child nodes nor parent node
				item.children.isEmpty and: {item.parent.isNil}
			}).every({arg it;it}),
			"Context children was freed when context root was freed"
		);
	}
}
