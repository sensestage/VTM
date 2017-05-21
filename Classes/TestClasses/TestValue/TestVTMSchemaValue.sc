TestVTMSchemaValue : TestVTMDictionaryValue {

	*makeRandomValue{arg params;
		^super.makeRandomValue(params);
	}

	test_DefaultProperties{
		var testValue, testSchemas;
		var valueObj = VTMSchemaValue.new;
		//value should be be nil
		// this.assertEquals(
		// 	valueObj.value, Dictionary.new,
		// 	"SchemaValue value is an empty Dictionary by default"
		// );

		//defaultValue should be nil
		// this.assertEquals(
		// 	valueObj.defaultValue, Dictionary.new,
		// 	"SchemaValue defaultValue is an empty Dictionary by default"
		// );

		//Properites are an empty Dictionary by default.
		// this.assertEquals(
		// 	valueObj.schema, nil,
		// 	"SchemaValue schema is nil by default"
		// );
	}
}
