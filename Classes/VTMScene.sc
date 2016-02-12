VTMScene : VTMContext {

	*new{arg owner;
		^super.new(owner).initScene;
	}

	initScene{
	}

	owner{
		^namespace.parent; //Should return object of class ModuleOwner
	}

	modules{}
}