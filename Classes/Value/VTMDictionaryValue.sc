VTMDictionaryValue : VTMCollectionValue {
	*prDefaultValueForType {^Dictionary.new}
	isValidType{arg val;
		^val.isKindOf(Dictionary);
	}
	value{^super.value.copy}
	*type{ ^\dictionary; }

}
