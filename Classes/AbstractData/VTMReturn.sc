VTMReturn : VTMValueElement {
	*managerClass{ ^VTMReturnManager; }

	value_{arg ...args;
		valueObj.value_(args);
	}
}
