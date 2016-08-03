//children may be Parameter and HardwareDevice
VTMHardwareDevice : VTMComposableContext {

	*new{arg name, parent, declaration, defintion;
		^super.new(name, parent, declaration, defintion).initHardwareDevice;
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
