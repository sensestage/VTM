~node = VTMNode(\bbb, "127.0.0.1");

~node.network;
~node.moduleHost;
~node.hardwareSetup;
~node.sceneOwner;

~node.network.name;
~node.moduleHost.name;
~node.hardwareSetup.name;
~node.sceneOwner.name;

//all 'children' of the node should have access to network and the node
~node.moduleHost.node;
~node.moduleHost.network;
~node.sceneOwner.node;
~node.sceneOwner.network;
~node.hardwareSetup.node;
~node.hardwareSetup.network;

~node.network.localNode.dump;

//modules are loaded from a declaration and definition
~node.moduleHost.loadModule