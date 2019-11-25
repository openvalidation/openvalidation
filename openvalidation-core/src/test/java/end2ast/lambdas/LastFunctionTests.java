package end2ast.lambdas;

import end2ast.End2AstRunner;
import io.openvalidation.common.ast.ASTComparisonOperator;
import io.openvalidation.common.data.DataPropertyType;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class LastFunctionTests {
  // todo lazevedo 25.11.19 Since LAST and FIRST share the same signature their test classes can be
  // combined

  @Test
  void last_function_type_with_unknown_array_content() throws Exception {
    String rule = "a last item from addresses as a last address";
    String schema = "{addresses:[]}";

    End2AstRunner.run(
        rule,
        schema,
        r ->
            r.variables()
                .first()
                .operandFunction()
                .hasName("LAST")
                .hasType(DataPropertyType.Unknown));
  }

  @Test
  void last_function_type_simple_with_object_array_content() throws Exception {
    String rule = "the last item from addresses as the last few addresses";
    String schema = "{addresses:[{zip: 1234}]}";

    End2AstRunner.run(
        rule,
        schema,
        r ->
            r.variables()
                .first()
                .operandFunction()
                .hasName("LAST")
                .hasType(DataPropertyType.Object));
  }

  @Test
  void last_function_type_simple_with_decimal_array_content() throws Exception {
    String rule = "the last item from numbers as the last few addresses";
    String schema = "{numbers:[1,2,3,4]}";

    End2AstRunner.run(
        rule,
        schema,
        r ->
            r.variables()
                .first()
                .operandFunction()
                .hasName("LAST")
                .hasType(DataPropertyType.Decimal));
  }

  @Test
  void last_function_type_simple_with_array_array_content() throws Exception {
    String rule = "the last item from arrays as the last few addresses";
    String schema = "{arrays:[[1,2,3,4],[1,2,3,4],[1,2,3,4]]}";

    End2AstRunner.run(
        rule,
        schema,
        r ->
            r.variables()
                .first()
                .operandFunction()
                .hasName("LAST")
                .hasType(DataPropertyType.Array));
  }

  @Test
  void last_function_simple_on_numbers_array() throws Exception {
    String rule = "the last item from numbers as the last address";
    String schema = "{numbers:[1,2,3,4]}";

    End2AstRunner.run(
        rule,
        schema,
        r ->
            r.variables()
                .first()
                .operandFunction()
                .hasName("LAST")
                .hasType(DataPropertyType.Decimal)
                .sizeOfParameters(1)
                .parameters()
                .first()
                .property("numbers")
                .hasType(DataPropertyType.Array)
                .hasArrayContentType(DataPropertyType.Decimal));
  }

  @Test
  void last_function_simple_on_numbers_array_with_amount() throws Exception {
    String rule = "the last 2 items from numbers as the last address";
    String schema = "{numbers:[1,2,3,4]}";

    End2AstRunner.run(
        rule,
        schema,
        r ->
            r.variables()
                .first()
                .operandFunction()
                .hasName("LAST")
                .hasType(DataPropertyType.Array)
                .sizeOfParameters(2)
                .parameters()
                .first()
                .property("numbers")
                .hasType(DataPropertyType.Array)
                .hasArrayContentType(DataPropertyType.Decimal)
                .parentList()
                .second()
                .number()
                .hasValue(2.0));
  }

  @ParameterizedTest
  @CsvSource({
    "The last item from addresses with zip_code equals 12345 as a last address",
    "The last item from the addresses given with a zip_code number equal to 12345 as a last address",
    "The last item from addresses with zip_code equal to the number 12345 as a last address"
  })
  void last_function_simple_on_object_array_with_condition(String input) throws Exception {
    String rule = input;
    String schema = "{addresses:[{zip_code: 1, city: Berlin}]}";

    End2AstRunner.run(
        rule,
        schema,
        r ->
            r.variables()
                .hasSizeOf(1)
                .first()
                .hasName("a last address")
                .operandFunction()
                .hasName("LAST")
                .hasType(DataPropertyType.Object)
                .sizeOfParameters(1)
                .parameters()
                .first()
                .function()
                .hasName("WHERE")
                .sizeOfParameters(2)
                .parameters()
                .first()
                .property("addresses")
                .hasType(DataPropertyType.Array)
                .parentList()
                .second()
                .lambdaCondition()
                .hasOperator(ASTComparisonOperator.EQUALS)
                .hasNoConnector()
                .leftProperty("zip_code")
                .hasType(DataPropertyType.Decimal)
                .parentCondition()
                .rightNumber(12345.0));
  }

  @ParameterizedTest
  @CsvSource({
    "The last 2 items from addresses with zip_code equals 12345 as first2addresses",
    "The last 2 items from the addresses given with a zip_code number equal to 12345 as first2addresses",
    "The last 2 items from addresses with zip_code equal to the number 12345 as first2addresses"
  })
  void last_function_simple_on_object_array_with_condition_with_amount(String input)
      throws Exception {
    String rule = input;
    String schema = "{addresses:[{zip_code: 1, city: Berlin}]}";

    End2AstRunner.run(
        rule,
        schema,
        r ->
            r.variables()
                .hasSizeOf(1)
                .first()
                .hasName("first2addresses")
                .operandFunction()
                .hasName("LAST")
                .hasType(DataPropertyType.Array)
                .hasArrayContentType(DataPropertyType.Object)
                .sizeOfParameters(2)
                .parameters()
                .second()
                .number()
                .hasValue(2.0)
                .parentList()
                .first()
                .function()
                .hasName("WHERE")
                .sizeOfParameters(2)
                .parameters()
                .first()
                .property("addresses")
                .hasType(DataPropertyType.Array)
                .parentList()
                .second()
                .lambdaCondition()
                .hasOperator(ASTComparisonOperator.EQUALS)
                .hasNoConnector()
                .leftProperty("zip_code")
                .hasType(DataPropertyType.Decimal)
                .parentCondition()
                .rightNumber(12345.0));
  }

  @Test
  void last_function_on_object_array_explicit_path() throws Exception {
    String rule = "The last item from info.addresses with zip_code equals 12345 as a last address";
    String schema = "{info: {addresses:[{zip_code: 1, city: Berlin}]}}";

    End2AstRunner.run(
        rule,
        schema,
        r ->
            r.variables()
                .hasSizeOf(1)
                .first()
                .hasName("a last address")
                .operandFunction()
                .hasName("LAST")
                .hasType(DataPropertyType.Object)
                .sizeOfParameters(1)
                .parameters()
                .first()
                .function()
                .hasName("WHERE")
                .sizeOfParameters(2)
                .parameters()
                .first()
                .property("addresses")
                .hasType(DataPropertyType.Array)
                .parentList()
                .second()
                .lambdaCondition()
                .hasOperator(ASTComparisonOperator.EQUALS)
                .hasNoConnector()
                .leftProperty("zip_code")
                .hasType(DataPropertyType.Decimal)
                .parentCondition()
                .rightNumber(12345.0));
  }

  // todo lionelpa 7.10.19 simple array access with LAST may need changes in grammar
  @Disabled
  @Test
  void last_function_in_direct_comparison() throws Exception {
    String rule = "LAST item FROM numbers IS 1 as sadfasdf";
    String schema = "{numbers: [1,2,3,4]}";

    End2AstRunner.run(rule, schema, r -> r.variables());
  }

  @Test
  void last_function_with_get_array_of_with_decimal_property_has_correct_type() throws Exception {
    String rule = "LAST item FROM numbers.value as var";
    String schema = "{numbers: [{value: 1}]}";

    End2AstRunner.run(
        rule,
        schema,
        r ->
            r.variables()
                .first()
                .operandFunction()
                .hasName("LAST")
                .hasType(DataPropertyType.Decimal));
  }

  @Test
  void last_function_variable_in_condition_on_jsondata() throws Exception {
    String rule = "LAST FROM numbers as X \n\n" + "If X is greater than 2 then error";
    String schema = "{numbers: [1,2,3]}";

    End2AstRunner.run(rule, schema, r -> r.variables());
  }

  @Test
  void last_function_variable_in_condition_on_jsondata_with_amount() throws Exception {
    String rule = "LAST 1 FROM numbers as X \n\n" + "If X is greater than 2 then error";
    String schema = "{numbers: [1,2,3]}";

    End2AstRunner.run(rule, schema, r -> r.variables());
  }

  @Test
  void last_function_variable_in_condition_on_jsonschema() throws Exception {
    String rule = "LAST FROM numbers as X \n\n" + "If X is greater than 2 then error";
    String schema =
        "{\n"
            + "  \"definitions\": {},\n"
            + "  \"$schema\": \"http://json-schema.org/draft-07/schema#\",\n"
            + "  \"$id\": \"http://example.com/root.json\",\n"
            + "  \"type\": \"object\",\n"
            + "  \"title\": \"The Root Schema\",\n"
            + "  \"required\": [\n"
            + "    \"numbers\"\n"
            + "  ],\n"
            + "  \"properties\": {\n"
            + "    \"numbers\": {\n"
            + "      \"$id\": \"#/properties/numbers\",\n"
            + "      \"type\": \"array\",\n"
            + "      \"title\": \"The Numbers Schema\",\n"
            + "      \"items\": {\n"
            + "        \"$id\": \"#/properties/numbers/items\",\n"
            + "        \"type\": \"integer\",\n"
            + "        \"title\": \"The Items Schema\",\n"
            + "        \"default\": 0,\n"
            + "        \"examples\": [\n"
            + "          1,\n"
            + "          2\n"
            + "        ]\n"
            + "      }\n"
            + "    }\n"
            + "  }\n"
            + "}";

    End2AstRunner.run(rule, schema, r -> r.variables());
  }
}
