VTMLibrary.postTree
MIDIFunc.trace
VTMLibrary.at(\definitions, \BehringerXTouchCompact)
(
var attributes = (
	inDeviceName: "X-TOUCH COMPACT",
	inPortName: "X-TOUCH COMPACT",
	outDeviceName: "X-TOUCH COMPACT",
	outPortName: "X-TOUCH COMPACT"
);
~xtouch = VTMHardwareDevice('xtouch', attributes, \BehringerXTouchCompact);
~xtouch.prepare;
)

VTMLibrary.at(\definitions, \LPD8)
(
var attributes = (
	inDeviceName: "LPD8",
	inPortName: "LPD8",
	outDeviceName: "LPD8",
	outPortName: "LPD8"
);
~lpd8 = VTMHardwareDevice('lpd8', attributes, \LPD8);
~lpd8.prepare;
)

(
var attributes = (
	inDeviceName: "nanoKONTROL",
	inPortName: "SLIDER/KNOB",
	outDeviceName: "nanoKONTROL",
	outPortName: "CTRL"
);
~nanoKontrol = VTMHardwareDevice('myNanoKontrol', attributes, 'KorgNanoKontrol');
~nanoKontrol.prepare;
)

(
var attributes = (
	inDeviceName: "X-TOUCH MINI",
	inPortName: "X-TOUCH MINI",
	outDeviceName: "X-TOUCH MINI",
	outPortName: "X-TOUCH MINI"
);
~xtouchMini = VTMHardwareDevice('xtouchMini', attributes, 'BehringerXTouchMini');
~xtouchMini.prepare;
)

(
var attributes = (
	inDeviceName: "BCF2000",
	inPortName: "BCF2000",
	outDeviceName: "BCF2000",
	outPortName: "BCF2000"
);
~bcf2000 = VTMHardwareDevice('bcf2000', attributes, 'BehringerBCF2000');
~bcf2000.prepare;
)



~bcf2000.envir[\device].components['fader.1'].action_({arg comp; NetAddr("lydserverlite.local", 57120).sendMsg('/hei', comp.value)})


//Niklas static IP
~bcf2000.envir[\device].components['fader.1'].action_({arg comp; NetAddr("192.168.1.115", 57120).sendMsg('/hei', comp.value)});
OSCFunc.trace;
MIDIFunc.trace
NetAddr.localAddr



(

~ting1 = VTMApplication(\ting1,

);

)
