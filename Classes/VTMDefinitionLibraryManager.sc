VTMDefinitionLibraryManager : VTMAbstractDataManager {
	classvar <global;

	*initClass{
		//TODO: Read and init global library
	}

	*vtmPath{ ^PathName(this.filenameSymbol.asString).pathOnly; }

	*dataClass{ ^VTMDefinitionLibrary; }
	name{ ^\libraries; }
}
