TestVTMContext : VTMUnitTest { 
	test_missingNameError{
		var context;
		//Should fail if not named
		try{
			context = VTMContext();
			this.failed(thisMethod,
				"Context did not throw error correctly when name not defined."
			);
		} {|err|
			if(err.what == "Context must have name", {
				this.passed(thisMethod,
					"Context threw error correctly when name not defined."
				);
			}, {
				this.failed(thisMethod,
					"Context threw wrong error when name not defined: \n\t%".format(
						err.errorString
					)
				);
			})
		};
	}

	test_DefaultConstruction{
		var context, testName;
		//construct without definition and declaration
		testName = this.class.makeRandomString.asSymbol;
		context = VTMContext(testName);
		this.assertEquals(
			context.name, testName,
			"Context initialized name correctly"
		);

		//should init envir as Environment with \self as it itself
		this.assert(
			context.envir.class == Environment and: {
				context.envir == Environment[\self -> context]
			},
			"Context initialized envir as Environment with self reference in self key"
		);

		this.assert(context.parent.isNil,
			"Context parent is nil"
		);

		this.assert(context.children.isNil,
			"Context children is nil"
		);

		//context path is nil
		this.assertEquals(context.path, nil,
			"Context init path to nil"
		);

		//context fullPath is just name prefixed with forward slash
		this.assertEquals(context.fullPath, "/%".format(testName).asSymbol,
			"Context init fullPath to /<name>"
		);

		this.assertEquals(context.state, \initialized,
			"Context set state to initialized when constructed"
		);

		this.assertEquals(context.addr, NetAddr.localAddr,
			"Context initialized addr to local address"
		);

		//Constructor extracts definition from declaration if defined
	}

	test_NewAndInitWithDeclaration{
		var context, testName;
		var declaration;
		var definition;
		//Construct with definition and declaration
		testName = this.class.makeRandomString.asSymbol;
		definition = Environment[];
		declaration = (
			path: "/%".format(this.class.makeRandomString).asSymbol,

		);
		context = VTMContext(testName);
	}

	test_ForceLeadingSlashInPath{
		var context, testPath, testName;
		var definition, declaration;
		testName = this.class.makeRandomString.asSymbol;
		testPath = 'pathWithout/leadingSlash';
		declaration = (
			path: testPath
		);
		context = VTMContext(testName, declaration: declaration);
		//should add missing leading slash
		this.assertEquals(
			context.path,
			"/%".format(testPath).asSymbol,
			"Context forcibly added missing leading slash to path"
		);
		//should also work with full path

		this.assertEquals(
			context.fullPath,
			"/%/%".format(testPath, testName).asSymbol,
			"Context forcibly added missing leading slash to fullPath"
		);
	}

	test_DerivePathFromParentContext{}

	test_DefinitionInitAndPrepareRunFreeAndStateChange{
		var context;
		var definition, declaration, name;
		var wasPrepared = false;
	   	var wasRun = false;
		var wasFreed = false;
		var prepareArgs;
		var runArgs;
		var freeArgs;
		var prepareCallbackArgs;
		var runCallbackArgs;
		var freeCallbackArgs;

		name = this.class.makeRandomString;
		definition = Environment.make{
			~prepare = {arg ...args;
				wasPrepared = true;
				prepareArgs = args;
			};
			~run = {arg ...args;
				wasRun = true;
				runArgs = args;
			};
			~free = {arg ...args;
				wasFreed = true;
				freeArgs = args;
			};
		};

		context = VTMContext(name, definition);

		this.assertEquals(
			context.envir,
			definition.put(\self, context),
			"Context envir is equal to definition argument plus self ref."
		);
		this.assert(
			context.envir !== definition,
			"Context definition argument is not identical to context envir"
		);

		//Do the prepare
		context.prepare(onPrepared: {arg ...args;
			prepareCallbackArgs = args;
		});
		this.assert(wasPrepared,
			"Context prepare def function was run"
		);
		this.assert(
			context.state == \prepared,
			"Context changed state to prepared"
		);
		this.assert(prepareArgs.notNil and: {
			prepareArgs[0] === context
		} and: {
			prepareArgs[1].isKindOf(Condition)
		},
		"Context passed correct argument to definition prepare function"
		);
		this.assertEquals(
			prepareCallbackArgs, [context],
			"Context passed correct arguments to prepare callback"
		);

		//Do the run
		context.run(onRunning: {arg ...args;
			runCallbackArgs = args;
		});
		this.assert(wasRun,
			"Context run def function was run"
		);
		this.assert(
			context.state == \running,
			"Context changed state to running"
		);
		this.assert(runArgs.notNil and: {
			runArgs[0] === context
		} and: {
			runArgs[1].isKindOf(Condition)
		},
		"Context passed correct argument to definition run function"
		);
		this.assertEquals(
			runCallbackArgs, [context],
			"Context passed correct arguments to run callback"
		);

		//Do the free
		context.free(onFreed: {arg ...args;
			freeCallbackArgs = args;
		});
		this.assert(wasFreed,
			"Context free def function was run"
		);
		this.assert(
			context.state == \freed,
			"Context changed state to freed"
		);
		this.assert(freeArgs.notNil and: {
			freeArgs[0] === context
		} and: {
			freeArgs[1].isKindOf(Condition)
		},
		"Context passed correct argument to definition free function"
		);
		this.assertEquals(
			freeCallbackArgs, [context],
			"Context passed correct arguments to free callback"
		);
	}

	test_initParameters{
		var context, name = this.class.makeRandomString;
		var parameterDeclarations;
		var definition, declaration;
		var numParameters = rrand(3,8);
		var parameterValues = Array.newClear(numParameters);
		var testParameterValues = parameterValues.deepCopy;
		parameterDeclarations = numParameters.collect({arg i;
			TestVTMParameter.makeRandomDeclaration(
				[\integer, \decimal, \string, \boolean].choose
			).put(\action, {|p|
				parameterValues[i] = p.value;
			});
		});
		definition	= Environment.make{
			~parameters = parameterDeclarations;
		};
		declaration = (
			path: "/%".format(this.class.makeRandomString).asSymbol
		);
		context = VTMContext(name, definition, declaration);
		context.prepare;
		this.assertEquals(
			context.parameters,
			parameterDeclarations.collect({arg it; it[\name]}),
			"Context initialized parameter names in the right order"
		);

		//check that the param path was built with the context path
		parameterDeclarations.do({arg item;
			var pathShouldBe;
			item[\name].postln;
			pathShouldBe = "%/%".format(context.fullPath, item[\name]).asSymbol;
			this.assertEquals(
				pathShouldBe, 
				context.getParameter(item[\name]).fullPath,
				"Context set Parameter path relative to its own path."
			);
		});
		
		//should get Parameter values through object API§

		//should set Parameter values through object API

		//should free all parameters upon context free.
		context.free;
	}

	test_OSCCommunication{
		var context, name = this.class.makeRandomString;
		var parameterDeclarations;
		var definition, declaration;
		var numParameters = 1;//rrand(3,8);
		var subContexts;
		var parameterValues = Array.newClear(numParameters);
		var testParameterValues = parameterValues.deepCopy;
		parameterDeclarations = numParameters.collect({arg i;
			TestVTMParameter.makeRandomDeclaration(
				[\integer, \decimal, \string, \boolean].choose
			).put(\action, {|p|
				parameterValues[i] = p.value;
			});
		});
		definition	= Environment.make{
			~parameters = parameterDeclarations;
		};
		declaration = (
			path: "/%".format(this.class.makeRandomString).asSymbol
		);
		context = VTMContext(name, definition, declaration);
		context.prepare;

		subContexts = 4.collect({arg i;
			VTMContext(this.class.makeRandomString.asSymbol, parent: context);
		});
		
		//startingOSC
		context.enableOSC;
		//should activate OSC
		this.assert(context.oscEnabled,
			"Context OSC communication activated."
		);

		//should initialize OSC commands
		//e.g. :children :declaration :parameters :parameterOrder :parameterValues
		//:state :reset

		{//test the OSC API getters
			[
				'children?', 'parameters?'//, 'state?'
			].do({arg cmdKey;
				var tempResponder, response, cond;
				var responded = false;
				var respPath = "%:%_testreply".format(context.fullPath, cmdKey).asSymbol;
				cond = Condition.new;
				tempResponder = OSCFunc({arg msg, time, addr, port;
					//if(msg.size > 2, {
					//	response = msg[1..].flat;
					//}, {
					//});
					response = msg[1..].flat;
					responded = true;
					cond.unhang;
				}, respPath);
				context.addr.sendMsg(
					"%:%".format(context.fullPath, cmdKey).asSymbol,
					NetAddr.localAddr.hostname,
					NetAddr.localAddr.port,
					respPath
				);
				cond.hang(0.2);
				this.assert(responded, 
					"Context OSC API command '%' responded".format(cmdKey)
				);
				this.assertEquals(
					response,
					context.perform(cmdKey.asString.drop(-1).asSymbol),
					"Context getter OSC responders responded with correct value for '%'.".format(cmdKey)
				);
				tempResponder.free;
			});
		}.value;

//		{//test the OSC API attributes responder
//			var cmdKey = 'attributes?';
//			var tempResponder, response, cond;
//			var responded = false;
//			var respPath = "%:%_testreply".format(context.fullPath, cmdKey).asSymbol;
//			cond = Condition.new;
//			tempResponder = OSCFunc({arg msg, time, addr, port;
//				response = msg[1].asString.parseYAML;
//				response = response.changeScalarValuesToDataTypes;
//				response = response.asIdentityDictionaryWithSymbolKeys;
//				responded = true;
//				cond.unhang;
//			}, respPath);
//			context.addr.sendMsg(
//				"%:%".format(context.fullPath, cmdKey).asSymbol,
//				NetAddr.localAddr.hostname,
//				NetAddr.localAddr.port,
//				respPath
//			);
//			cond.hang(0.2);
//			this.assert(responded, 
//				"Context OSC API command '%' responded".format(cmdKey)
//			);
//			this.assertEquals(
//				response,
//				context.perform(cmdKey.asString.drop(-1).asSymbol),
//				"Context getter OSC responders responded with correct value for '%'.".format(cmdKey)
//			);
//
//			tempResponder.free;
//		}.value;
		//test OSC responders for parameters

		//stoppingOSC

		//restarting OSC

		//free context frees OSC responders, (is tested in VTMUnitTest:tearDown)
		context.free;
	}

	test_addingChildContexts{
		var rootData;
		rootData = IdentityDictionary[\name -> this.class.makeRandomString];

		rootData.put(\obj, VTMContext(rootData[\name]));

		//should return children as nil
		this.assert(
			rootData[\obj].children.isNil,
			"Context return children from context without children as nil"
		);
		this.assert(
			rootData[\obj].isLeaf,
			"Context is considered a leaf when childless"
		);

		this.assert(
			rootData[\obj].root === rootData[\obj],
			"Context root points to itself when being root"
		);

		rootData.put(\children, {
			var childName = this.class.makeRandomString; 
			IdentityDictionary[
				\name -> childName,
				\obj -> VTMContext(childName, parent: rootData[\obj]);
			];
		} ! rrand(3,7));

		rootData[\children].do({arg childData;
			childData.put(\children, {
				var grandChildName = this.class.makeRandomString;
				IdentityDictionary[
					\name -> grandChildName,
					\obj -> VTMContext(grandChildName, parent: childData[\obj])
				]
			} ! rrand(3, 7));
		});

		this.assert(
			rootData[\obj].isLeaf.not,
			"Context added children to root object"
		);
		//the root obj should be root and not leafs
		this.assert(
			rootData[\obj].isRoot and: {rootData[\obj].isLeaf.not},
			"Context returned isRoot when being constructed to be so"
		);

		//all children should be both not roots and not leafs
		this.assert(
			rootData[\children].every({arg childData;
				var child = childData[\obj];
				child.isRoot.not and: { child.isLeaf.not};
			}),
			"Context returned all children as non roots and non leafs"
		);

		//all grand children should be non root and leafs
		this.assert(
			rootData[\children].every({arg childData;
				childData[\children].every({arg grandChildData;
					var grandChild = grandChildData[\obj];
					grandChild.isRoot.not and: { grandChild.isLeaf};
				});
			})
		);

		//the children name and paths should match with the test data
		this.assert(
			rootData[\children].every({arg childData;
				var child = childData[\obj];
				"/%/%".format(
					rootData[\name],
				   	childData[\name]
				).asSymbol == child.fullPath and: {
					"/%".format(rootData[\name]).asSymbol == child.path
				};
			}),
			"Context children paths and fullPaths were correctly returned"
		);

		//the grand children paths and fullPaths should match with the test data
		this.assert(
			rootData[\children].every({arg childData;
				childData[\children].every({arg grandChildData;
					var grandChild = grandChildData[\obj];
					"/%/%/%".format(
						rootData[\name],
						childData[\name],
						grandChildData[\name]
					).asSymbol == grandChild.fullPath and: {
						"/%/%".format(
							rootData[\name],
							childData[\name]
						).asSymbol == grandChild.path
					};
				});
			}),
			"Context grand children paths and fullPaths were correctly returned"
		);

		//all children and grand children point to the root as root
		this.assert(
			rootData[\children].every({arg childData;
				(childData[\obj].root === rootData[\obj]) and: {
					childData[\children].every({arg grandChildData;
						grandChildData[\obj].root === rootData[\obj];
					});
				};
			}),
			"Context children and grand children all point to correct root context"
		);
	}

//	test_EnvirExecute{
//		var wasRun = false, itself, theArgs;
//		var testArgs = [11,22,\hello];
//		var testDesc = IdentityDictionary[\testObj -> 33];
//		var testDef = IdentityDictionary[\bongo -> 8383, \brexit -> {|context ...args|"So you wanna leave?".postln; wasRun = true; theArgs = args; itself = context;}];
//		var context = VTMContext.new('myRoot', testDef, testDesc);
//
//		context.execute(\brexit, *testArgs);
//		this.assert(
//			wasRun, "Context did run function"
//		);
//
//		this.assert(
//			itself === context, "Context passed itself to the envir function"
//		);
//
//		this.assertEquals(
//			theArgs, testArgs, "Context passed correct arguments"
//		);
//
//	}
//
//	test_nodeManagement{
//		var root = VTMContext.new('myRoot');
//		var app = VTMContext.new('myApp', parent: root);
//		var module, moduleObj;
//
//		this.assert(
//			root.children.includes(app),
//			"Context added app to its children"
//		);
//
//		//should notify the parent upon free
//		app.free;
//		this.assert(
//			root.children.includes(app).not,
//			"Context removed app from its children"
//		);
//
//		//If the root context is freed it should remove all node context, and this should
//		//propagate down the context tree
//
//		//Make three level context tree (chain)
//		app = VTMContext.new('myOtherApp', parent: root);
//		module = VTMContext.new('myModule', parent: root);
//
//		//Should free all node contexts, i.e. 'myModule' and 'myOtherApp'
//		root.free;
//
//		this.assert(
//			root.children.includes(app).not,
//			"Context removed first level node"
//		);
//
//		this.assert(
//			app.children.includes(module).not,
//			"Context removed second level node"
//		);
//	}
//
//	test_MultiLevelChildManagament{
//		var root;
//		var children;
//
//		//using empty Event as bogus obj
//		root = VTMContext.new('myRoot');
//
//		//Make a three level context tree
//		3.do({arg i;
//			var iNode = VTMContext.new("node_%".format(i).asSymbol, parent: root);
//			children = children.add(iNode);
//			3.do({arg j;
//				var jNode = VTMContext.new("node_%_%".format(i, j).asSymbol, parent: iNode);
//				children = children.add(jNode);
//				4.do({arg k;
//					var kNode = VTMContext.new("node_%_%_%".format(i, j, k).asSymbol, parent: jNode);
//					children = children.add(kNode);
//				});
//			});
//		});
//
//		//All nodes must have same root
//		this.assert(
//			children.collect({arg item;
//				item.root === root;
//			}).every({arg it; it}),
//			"Context children all have the same root"
//		);
//
//		//freeing root also frees child nodes
//		root.free;
//		this.assert(
//			children.collect({arg item;
//				//The nodes should no longer have child nodes nor parent node
//				item.children.isEmpty and: {item.parent.isNil}
//			}).every({arg it;it}),
//			"Context children was freed when context root was freed"
//		);
//	}
}
