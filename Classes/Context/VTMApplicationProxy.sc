VTMApplicationProxy : VTMContextProxy {

	*new{arg name, parent, declaration, defintion;
		^super.new(name, parent, declaration, defintion).initApplicationProxy;
	}

	initApplicationProxy{
	}
}
