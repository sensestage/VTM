VTMHardwareSetup : VTMNetworkedContext {

	//a hardware setups parent will be an Application
	*new{arg network, definition, declaration;
		^super.new('devices', network, definition, declaration).initHardwareSetup;
	}

	initHardwareSetup{
		"init hardware setup".postln;
		if(declaration.notNil, {
			"init hardware setup declaration: %".format(declaration).postln;
			if(declaration.includesKey(\devices), {
				"init hardware setup devices".postln;
				declaration[\devices].do({arg deviceDeclaration;
					"\tiinit hardware device: %".format(deviceDeclaration).postln;
					VTMHardwareDevice.buildFromDeclaration(deviceDeclaration, this);
				})
			});
		});
	}

	addDevice{arg newHardwareDevice;
		this.addChild(newHardwareDevice);
	}
	removeDevice{arg deviceName;
		this.removeChild(deviceName);
	}

	devices{ ^children; }
}
