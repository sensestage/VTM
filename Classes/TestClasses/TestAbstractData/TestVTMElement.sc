TestVTMElement : TestVTMAbstractData {
	*classesForTesting{
		^[
			VTMMapping,
			VTMDefinitionLibrary,
			VTMCommand,
			VTMContextParameter,
			VTMRemoteNetworkNode,
			VTMModule,
			VTMApplication,
			VTMHardwareDevice,
			VTMScore,
			VTMScene
		];
	}

	test_initElement{
		var obj;
		//should error if created without name
		this.class.classesForTesting.do({arg class;
			try {
				obj = class.new(name: nil, attributes: nil, manager: nil);
				this.failed(thisMethod,
					"[%] - Should have thrown error when created without 'name'".format(class)
				);
				obj.free;
			} {|err|
				//TODO: add error type check here when VTMError classes are implemented
				this.passed(thisMethod,
					"[%] - Threw error when created without 'name'".format(class)
				);
			};
		});
	}

	test_DerivedPath{
		var obj, testAttributes, managerObj;
		this.class.classesForTesting.do({arg class;
			var testClass = VTMUnitTest.findTestClass(class);
			var testName = VTMUnitTest.makeRandomSymbol;
			var managerClass = class.managerClass;
			var testPath;

			managerObj = managerClass.new;

			testAttributes = nil;
			obj = class.new(
				testName,
				testAttributes,
				managerObj
			);

			//should be the manager fullPath
			this.assert(obj.path.notNil,
				"[%] - path initially not nil".format(class)
			);
			this.assert(obj.hasDerivedPath,
				"[%] - has derived path".format(class)
			);
			this.assertEquals(
				obj.path, managerObj.fullPath,
				"[%] - path is the same as manager full path".format(class)
			);

			//Should not be able to change the path when managed element object.
			testPath = "/%".format(this.class.makeRandomString);
			obj.path = testPath;
			this.assertEquals(
				obj.path, managerObj.fullPath,
				"[%] - path is unchanged in managed element object".format(class)
			);
			this.assert(obj.hasDerivedPath,
				"[%] - still has derived path".format(class)
			);
			this.assertEquals(
				obj.fullPath,
				"%%%".format(
					managerObj.path ++ managerObj.leadingSeparator ++ managerObj.name ++
					obj.leadingSeparator ++ obj.name
				).asSymbol,
				"[%] - fullPath is correct".format(class);
			);

			obj.free;
			managerObj.free;

		});

	}

	test_ManualPath{
		var obj, testAttributes;
		this.class.classesForTesting.do({arg class;
			var testClass = VTMUnitTest.findTestClass(class);
			var testName = VTMUnitTest.makeRandomSymbol;
			var testPath = "/%".format(this.class.makeRandomString);

			obj = class.new(
				testName,
				testAttributes
			);
			//should be nil
			this.assert(obj.path.isNil,
				"[%] - path init to nil".format(class)
			);
			this.assert(obj.hasDerivedPath.not,
				"[%] - has not derived path".format(class)
			);

			obj.path = testPath;
			this.assertEquals(
				obj.path, testPath.asSymbol,
				"[%] - set path and returned it as symbol".format(class)
			);
			this.assert(obj.hasDerivedPath.not,
				"[%] - still has non-derived path".format(class)
			);
			this.assertEquals(
				obj.fullPath, "%%%".format(testPath, obj.leadingSeparator, obj.name).asSymbol,
				"[%] - fullPath is correct".format(class);
			);

			//change path with nonleading slash path arg
			//using 'g' as non-leading separator char here
			testPath = "g%".format(this.class.makeRandomString);
			obj.path = testPath;

			//should force leading separator
			this.assertEquals(
				obj.path, "/%".format(testPath).asSymbol,
				"[%] - Changed and forced leading slash to set path".format(class)
			);
			obj.free;
		});

	}

	test_AttributeOSC{

		//changing path manually should update the OSC interface paths.
	}

	test_CommandOSC{}

	test_QueryOSC{}
}