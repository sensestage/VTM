+ IdentityDictionary {
	flattenedCopy{
		var getParent;
		getParent = {arg obj;
			var result;
			if(obj.parent.isNil, {
				result = obj.copy;
			}, {
				result = getParent.value(obj.parent);
				result.putAll(obj.copy);
			});
			result;
		};
		^getParent.value(this.copy);
	}
}