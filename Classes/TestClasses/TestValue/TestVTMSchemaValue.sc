TestVTMSchemaValue : TestVTMDictionaryValue {

	*makeRandomValue{arg params;
		^super.makeRandomValue(params);
	}

	test_DefaultAttributes{
		var testValue, testSchemas;
		var param = VTMSchemaValue.new;
		//value should be be nil
		// this.assertEquals(
		// 	param.value, Dictionary.new,
		// 	"SchemaValue value is an empty Dictionary by default"
		// );

		//defaultValue should be nil
		// this.assertEquals(
		// 	param.defaultValue, Dictionary.new,
		// 	"SchemaValue defaultValue is an empty Dictionary by default"
		// );

		//Properites are an empty Dictionary by default.
		// this.assertEquals(
		// 	param.schema, nil,
		// 	"SchemaValue schema is nil by default"
		// );
	}
}
