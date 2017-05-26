// declaration must pass in mktlName and mktlDescription

VTMMKtlDevice : VTMHardwareDevice {

	*new{arg name, declaration, manager, definition;
		^super.new(name, declaration, manager, definition).initMKtlDevice;
	}

	initMKtlDevice{
		this.envir.use({
			var attributes,returns, virtualMKtl;
			virtualMKtl = MKtl(
			   	~self.get( \mktlName ),
			   	~self.get( \mktlDescription ).asString,
				multiIndex: ~self.get(\mktlIndex),
			   	tryOpenDevice: false );

			returns = virtualMKtl.inputElements.keys.asArray.sort.collect{ |it|
				var el = virtualMKtl.inputElements.at( it );
				it -> (
					type: this.class.checkValueType( el.deviceSpec ),  // mktl always converts to 0 - 1 range as floats // FIXME: it could be an integer
					minValue: el.deviceSpec.unmap( el.deviceSpec.minval ),
					maxValue: el.deviceSpec.unmap( el.deviceSpec.maxval ), // some specs may not be 0 to 1
					defaultValue: el.defaultValue,
					value: el.value
				)
			};

			attributes = virtualMKtl.outputElements.keys.asArray.sort.collect{ |it|
				var el = virtualMKtl.outputElements.at( it );
				it -> (
					type: this.class.checkValueType( el.deviceSpec ),  // mktl always converts to 0 - 1 range as floats // FIXME: it could be an integer
					minValue: el.deviceSpec.unmap( el.deviceSpec.minval ),
					maxValue: el.deviceSpec.unmap( el.deviceSpec.maxval ), // some specs may not be 0 to 1
					defaultValue: el.defaultValue,
					value: el.value
				);
			};

			~self.prAddComponentsToEnvir( (
				returns: returns,
				attributes: attributes,
				commands: [ \makeGui -> ( action: { ~mktl.gui } ) ] 
			) );

			virtualMKtl.free;
		});
		"VTMMKtlDevice initialized".postln;
	}

	prepare {
		envir.use({
			// open desired device
			~mktl = MKtl(
				~self.get( \mktlName ),
			   	~self.get( \mktlDescription ).asString,
				multiIndex: ~self.get(\mktlIndex)
		   	);
			// set actions for each control: here an interesting question: do we need to set actions for controls not currently used?
			~mktl.inputElements.do{ |it|
				~self.return( it.name, it.value );
				it.action = { |el|
					~self.return( it.name, el.value ); // inEnvir?
				}.inEnvir;
			};
			// create an output action
			~setMKtlCtl = { |name, val|
				~mktl.outputElements.at( name ).value_( val );
			};
		});
		super.prepare;
	}

	free{
		envir.use({
			~mktl.free;
		});
		super.free;
	}


	*parameterDescriptions{
		^super.parameterDescriptions.putAll(VTMOrderedIdentityDictionary[
			\mktlName -> (type: \string, optional: false),
			//TODO: Change the type to symbol when it is implemented
			\mktlDescription -> (type: \string, optional: false, strictlyTyped: true),
			\mktlIndex -> (type: \integer, optional: true, strictlyTyped: true)
	   	]);
	}

	*checkValueType{ |spec|
		var midvalue = ( spec.minval + spec.maxval )/2;
		var testval = spec.unmap( midvalue );
		if ( testval.isKindOf(Integer) ){
			^\integer;
		};
		^\float;
	}

}
