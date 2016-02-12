//Testing startup of a node
(
n = VTMNode.new(\aaa, NetAddr("192.168.1.53", 57120));
OSCdef(\hallo, {arg msg, time, addr, port;
	["HALLO", msg, time, addr, port].postln;
}, '/hallo');
)
n.name;
n.addr;
NetAddr.localAddr.sendMsg('/hei');

///Adding modules, just as paths as of this moment
n.loadModuleJSONCue('lamp.1'); //a module cue (json string)
n.loadModuleCue('lamp.1'); //a module cue (dictionary structure)
n.loadModuleFile('lamp.1'); //a file path
n.loadModule(); //A VTMModule object

n.loadSceneJSONCue('lamp.1'); //a Scene cue (json string)
n.loadSceneCue('lamp.1'); //a Scene cue (dictionary structure)
n.loadSceneFile('lamp.1'); //a file path
n.loadScene(); //A VTMScene object

n.loadHardwareScriptFile('LANBox'); //a filepath, the script will register and place itself in the namespace internally
n.loadHardwareScriptFile('soundCards');
n.loadHardwareScriptFile('pinneRobots');