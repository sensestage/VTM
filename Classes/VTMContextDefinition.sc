VTMContextDefinition {
	var definition;
	var context;

	*new{arg def, context;
		^super.new.initContextDefinition(def, context);
	}

	initContextDefinition{arg def_, context_;
		definition = def_ ? Environment[
			\parameters -> IdentityDictionary.new,
			\commands -> IdentityDictionary.new,
			\presets -> IdentityDictionary.new,
			\cues -> IdentityDictionary.new,
			\mappings -> IdentityDictionary.new,
			\scores -> IdentityDictionary.new
		];
		context = context_;
	}

	makeEnvir{
		^definition.deepCopy.put(\self, context);
	}

	parameters{
		^definition[\parameters] ? IdentityDictionary.new;
	}
	commands{
		^definition[\command] ? IdentityDictionary.new;
	}
	presets{
		^definition[\presets] ? IdentityDictionary.new;
	}
	cues{
		^definition[\cues] ? IdentityDictionary.new;
	}
	mappings{
		^definition[\mappings] ? IdentityDictionary.new;
	}
	scores{
		^definition[\scores] ? IdentityDictionary.new;
	}
}
