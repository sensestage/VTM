VTMScene : VTMContext {

	*new{arg owner;
		^super.new(owner).initScene;
	}

	initScene{
	}

	owner{
		^namespaceElement.parent; //Should return object of class ModuleOwner
	}

	modules{}
}
