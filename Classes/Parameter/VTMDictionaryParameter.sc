VTMDictionaryParameter : VTMCollectionParameter {
	prDefaultValueForType {^Dictionary.new}
	isValidType{arg val;
		^val.isKindOf(Dictionary);
	}
	value{^value.copy}
	type{ ^\dictionary; }

}
