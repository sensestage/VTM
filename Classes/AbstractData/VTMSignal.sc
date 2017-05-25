VTMSignal : VTMValueElement{
	*managerClass{ ^VTMSignalManager; }

	emit{arg val;
		valueObj.valueAction_(val);
	}

	action_{arg func;
		valueObj.action_(func);
	}
}
