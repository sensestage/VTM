/*
Not yet an implementation of a standardized schema syntax, but follows similar principles
as with JSON schema.
The semantics as for now built on the VTM Parameter settings and types.
In the future a translator from/to JSON schema might be made.

properties:
The keys that will be received in the messages.

additionalProperties:
Setting additionalProperties to false makes it not valid to add other properties for
messages, i.e. they will become fail validation.
If additionalProperties defines a type, only additionalProperties of that type will be accepted.

string enum:
Type string enum resolves in VTM to a option-type parameter.

required:
Defines the properties that are required for a valid message.

minPorperties / maxProperties:
The number of properties can be set with this.

Property dependancies:

Schema dependancies:

Pattern properties:
Regex pattern for additionalProperty keys. Can additionally define types etc.

*/
VTMSchemaValue : VTMDictionaryValue {
	var <schema;
	var <properties;

	isValidType{arg val;
		var result = false;
		if(super.isValidType(val), {
			result = this.validate(val);
		});
		^result;
	}
	*type{ ^\schema; }
	*prDefaultValueForType{
		^[];
	}
	validate{arg val;
		^true;//temp always validate to true
	}
}
