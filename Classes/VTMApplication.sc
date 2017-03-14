VTMApplication : VTMContext {
	var <scenes;
	var <modules;
	var <hardwareDevices;
	var <libraries;
	var <attributes;

	//manager arg is just here for getting constructor args to match superclass
	*new{arg name, attributes, manager, definition;
		^super.new(name, attributes, nil, definition).initApplication;
	}

	initApplication{
		libraries = VTMDefinitionLibraryManager.new(this, attributes[\libraries]);
		hardwareDevices = VTMHardwareSetup(this, attributes[\hardwareDevices]);
		modules = VTMModuleHost(this, attributes[\module]);
		scenes = VTMSceneOwner(this, attributes[\scenes]);
		//Discover other application on the network
		if(attributes.includesKey(\openView), {
			if(attributes[\openView], {
				var viewDesc, viewDef;
				this.makeView( attributes[\viewDefinition], attributes[\viewAttributes] );
			});
		});
	}

	prComponents{ ^super.prComponent ++ [hardwareDevices, modules, scenes, libraries]; }

}
