VTMNamespace : LibraryBase {
	classvar global;

	*global { ^global }
	*global_ { arg obj; global = obj; }

	*initClass {
		global = this.new;
	}
}