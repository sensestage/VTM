VTMParameter {
	var <name;
	var <path;
	var <description;
	var >action;
	var <enabled;
	var <responders;

	//factory type constructor only
	*makeFromDescription{arg name, description;
		^super.new.makeParameter(name, description);
	}

	makeParameter{arg name_, description_;
		name = name_;
		description = description_;
	}

	doAction{
		action.value(this);
	}

}


/*
Types to implement:
- Value
  - Boolean
  - Generic
  - Scalar
    - Integer
    - Decimal
  - Option
  - Symbol
  - String
  - Timecode
  - Dictionary
    - JSON
- Message
- Return
- Array (encapsulates internal types)
  - IntegerArray
  - DecimalArray
  - GenericArray
  - StringArray
  - SymbolArray
  - etc.


*/