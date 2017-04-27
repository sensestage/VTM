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
}