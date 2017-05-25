// declaration must pass in mktlName and mktlDescription

VTMMKtlDevice : VTMHardwareDevice {

	*new{arg name, declaration, manager, definition;
		^super.new(name, declaration, manager, definition).initMKtlDevice;
	}

	initMKtlDevice{
		envir.use({
			var attributes,returns, virtualMKtl;
			virtualMKtl = MKtl( ~self.get( \mktlName ), ~self.get( \mktlDescription ), tryOpenDevice: false );

			returns = virtualMKtl.inputElements.keys.asArray.sort.collect{ |it|
				var el = virtualMKtl.inputElements.at( it );
				it -> (
					type: \float,  // mktl always converts to 0 - 1 range as floats
					minValue: 0.0, // or el.deviceSpec.minval
					maxValue: 1.0, // or el.deviceSpec.maxval
					defaultValue: el.defaultValue,
					value: el.value
				)
			};

			attributes = virtualMKtl.outputElements.keys.asArray.sort.collect{ |it|
				var el = virtualMKtl.inputElements.at( it );
				it -> (
					type: \float,  // mktl always converts to 0 - 1 range as floats
					minValue: 0.0, // or el.deviceSpec.minval
					maxValue: 1.0, // or el.deviceSpec.maxval
					defaultValue: el.defaultValue,
				);
			};

			~returns.postcs;
			returns.postcs;

			~attributes.postcs;
			attributes.postcs;

			returns.do{ |it|
				it.postln;
				~returns.put( it.key, it.value );
			}.inEnvir;
			~returns.postcs;
			attributes.do{ |it|
				it.postln;
				~attributes.put( it.key, it.value );
				// ~attributes.add( it );
			}.inEnvir;
			~attributes.postcs;

			~commands.put( \makeGui, ( action: { ~mktl.gui } ) );

			virtualMKtl.free;
		});
		"VTMMKtlDevice initialized".postln;
	}

	prepare {
		envir.use({
			// open desired device
			~mktl = MKtl( ~self.get( \mktlName ), ~self.get( \mktlDescription ) );
			// set actions for each control: here an interesting question: do we need to set actions for controls not currently used?
			~mktl.inputElements.do{ |it|
				~self.return( it.name ).value_( it.value );
				it.action = { |el|
					~self.return( it.name ).value_( el.value ); // inEnvir?
				};
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

}
