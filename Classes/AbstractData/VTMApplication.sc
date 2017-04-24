VTMApplication : VTMContext {
	var <scenes;
	var <modules;
	var <hardwareDevices;
	var <libraries;
	var <attributes;

	*managerClass{ ^VTMLocalNetworkNode; }

	*new{arg name, attributes, manager, definition;
		^super.new(name, attributes, manager, definition).initApplication;
	}

	initApplication{
		// libraries = VTMDefinitionLibraryManager.new(this);
		// hardwareDevices = VTMHardwareSetup(this, attributes[\hardwareDevices]);
		// modules = VTMModuleHost(this, attributes[\module]);
		// scenes = VTMSceneOwner(this, attributes[\scenes]);
		// //Discover other application on the network
		// if(attributes.includesKey(\openView), {
		// 	if(attributes[\openView], {
		// 		var viewDesc, viewDef;
		// 		this.makeView( attributes[\viewDefinition], attributes[\viewAttributes] );
		// 	});
		// });
	}
	prComponents{ ^super.prComponents; }
	// prComponents{ ^super.prComponents ++ [hardwareDevices, modules, scenes, libraries]; }

}
