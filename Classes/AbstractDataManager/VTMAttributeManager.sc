VTMAttributeManager : VTMElementComponent {
	var presets;

	*dataClass{ ^VTMAttribute; }
	name{ ^\attributes; }

	*new{arg context, declaration;
		^super.new(context, declaration).initAttributeManager;
	}

	initAttributeManager{
		//presets = VTMPresetManager(declaration[\presets]);
	}
}
