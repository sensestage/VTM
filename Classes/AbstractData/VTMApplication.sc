VTMApplication : VTMContext {
	var <scenes;
	var <modules;
	var <hardwareDevices;
	var <libraries;

	*managerClass{ ^VTMLocalNetworkNode; }

	*new{arg name, attributes, manager, definition;
		^super.new(name, attributes, manager, definition).initApplication;
	}

	initApplication{
		libraries = VTMDefinitionLibraryManager.new(this);
		hardwareDevices = VTMHardwareSetup(this);
		modules = VTMModuleHost(this);
		scenes = VTMSceneOwner(this);
		// //Discover other application on the network
		// if(attributes.includesKey(\openView), {
		// 	if(attributes[\openView], {
		// 		var viewDesc, viewDef;
		// 		this.makeView( attributes[\viewDefinition], attributes[\viewAttributes] );
		// 	});
		// });
	}

	prComponents{ ^super.prComponents ++ [hardwareDevices, modules, scenes, libraries]; }

	*attributeKeys{
		super.attributeKeys ++ [\libraries, \devices, \modules, \scenes];
	}

}
