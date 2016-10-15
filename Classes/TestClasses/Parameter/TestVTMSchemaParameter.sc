TestVTMSchemaParameter : TestVTMDictionaryParameter {

	*makeRandomValue{arg params;
		^super.makeRandomValue(params);
	}

	setUp{
		"Setting up a VTMSchemaParameterTest".postln;
	}

	tearDown{
		"Tearing down a VTMSchemaParameterTest".postln;
	}

	test_DefaultAttributes{
		var testValue, testSchemas;
		var param = VTMSchemaParameter.new('mySchema');
		//value should be be nil
		// this.assertEquals(
		// 	param.value, Dictionary.new,
		// 	"SchemaParameter value is an empty Dictionary by default"
		// );

		//defaultValue should be nil
		// this.assertEquals(
		// 	param.defaultValue, Dictionary.new,
		// 	"SchemaParameter defaultValue is an empty Dictionary by default"
		// );

		//Properites are an empty Dictionary by default.
		// this.assertEquals(
		// 	param.schema, nil,
		// 	"SchemaParameter schema is nil by default"
		// );
	}
}
