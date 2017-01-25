VTMHardwareSetup : VTMNetworkedContext {

	//a hardware setups parent will be an Application
	*new{arg definition, attributes, network;
		^super.new('devices', definition, attributes, network).initHardwareSetup;
	}

	initHardwareSetup{
		"init hardware setup".postln;
		if(attributes.notNil, {
			"init hardware setup attributes: %".format(attributes).postln;
			if(attributes.includesKey(\devices), {
				"init hardware setup devices".postln;
				attributes[\devices].do({arg deviceAttributes;
					"\tiinit hardware device: %".format(deviceAttributes).postln;
					VTMHardwareDevice.buildFromAttributes(deviceAttributes, this);
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
