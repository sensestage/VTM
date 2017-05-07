VTMDictionaryValue : VTMCollectionValue {
	*prDefaultValueForType {^Dictionary.new}
	isValidType{arg val;
		^val.isKindOf(Dictionary);
	}
	value{^this.value.copy}
	*type{ ^\dictionary; }

}
