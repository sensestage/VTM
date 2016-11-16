+ Association {

	trulyEqual{arg anAssociation;
		^this.key == anAssociation.key and: {this.value == anAssociation.value};
	}
}