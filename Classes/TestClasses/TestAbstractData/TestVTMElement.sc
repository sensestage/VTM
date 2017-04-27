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
			var testClass = this.class.findTestClass(class);
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
			//test init manager
			{
				var testManager = testClass.makeRandomManagerObject;
				var testAttr = testClass.makeRandomAttributes;
				var testName = testClass.makeRandomSymbol;

				obj = class.new( testName, testAttr, testManager );
				[
					[\name, testName],
					[\attributes, testAttr],
					[\manager, testManager]
				].do({arg items, i;
					var method, variable;
					#method, variable = items;
					this.assertEquals(
						obj.perform(method), variable,
						"[%] - init '%' correctly".format(class, method)
					);
				});
			}.value;

		});
	}
}