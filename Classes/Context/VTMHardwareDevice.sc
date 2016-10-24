//children may be Parameter and HardwareDevice
VTMHardwareDevice : VTMComposableContext {

	// *new{arg name, parent, declaration, definition;
	// 	^super.new(name, parent, declaration, definition).initHardwareDevice;
	// }

	//Non DRY hack for Martin/Sofia jam - duplication of VTMModule constructor
	*new{arg name, parent, declaration, definition;
		var actualDefinition, actualDeclaration;
		//Check if either definition or declaration are Symobol, in which case
		//they will be looked up in the global vtm library
		if(definition.isKindOf(Symbol), {
			actualDefinition = VTMLibrary.at(\definitions, definition);
		}, {
			actualDefinition = definition;
		});

		actualDefinition = this.makeDefinitionEnvironment(actualDefinition);

		if(declaration.isKindOf(Symbol), {
			actualDeclaration = VTMLibrary.at(\declarations, declaration);
		}, {
			actualDeclaration = declaration;
		});
		^super.new(name, parent, actualDeclaration, actualDefinition).initHardwareDevice;
	}

	*makeDefinitionEnvironment{arg definition;
		//----Temp hack for martin/sofia jam:
		var result, prototypes;
		if(definition.includesKey(\prototypes), {
			prototypes = definition[\prototypes];
			if(prototypes.first == 'MIDIDevice', {
				var midiDevice;
				midiDevice = Environment.new;
				midiDevice.use{
					~prepare = {arg device, cond;
						var decl = device.declaration;
						"Preparing MIDIDevice prototype".postln;
						MIDIClient.initialized.not.if({
							MIDIClient.init;
							MIDIIn.connectAll;
						});

						//find the input and output ports from the declaratiom
						~device = MIDIDevice.new(
							inDeviceName: decl[\inDeviceName].asString,
							inPortName: decl[\inPortName].asString,
							outDeviceName: decl[\outDeviceName].asString,
							outPortName: decl[\outPortName].asString,
							name: device.name
						);
					};
					~free = {
						~device.free;
					};
				};

				result = Environment.new(proto: midiDevice);
				//----mod def hackaton hack for volume parameter

				if(definition.includesKey(\parameters).not, {
					definition.put(\parameters, []);
				});
				// //Add volume parameter first
				// definition[\parameters] = [(
				// 	name: \volume, type: \decimal,
				// 	minVal: -96.0, maxVal: 6, clipmode: \both,
				// 	defaultValue: -96.0,
				// 	action: {|p| ~output.vol_(p.value.dbamp)};
				// )] ++ definition[\parameters];
				//
				//---end mod def hackaton hack
				result.putAll(definition);
			});
		}, {
			result = definition;
		});

		///-----End temp hackaton hack
		^result;

	}

	initHardwareDevice{
		"VTMHardwareDevice initialized".postln;
	}




	parameters{	^nonSubcontexts.value; }
	subdevices{	^subcontexts.value; }
	isSubdevice{ ^this.isSubcontext; }
	isParameter{ ^this.isSubcontext.not; }
	setup { ^parent; }
}
