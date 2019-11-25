/*
 *    Copyright 2019 BROCKHAUS AG
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package end2ast.lambdas;

import end2ast.End2AstRunner;
import io.openvalidation.common.ast.ASTComparisonOperator;
import io.openvalidation.common.data.DataPropertyType;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class FirstFunctionsTests {

  /*
  todo evtl first 10 items -->OF
  todo sorting arrays

  die ersten 10 items/streets
  //simple type array
  first number from numbers

  //complex type array
  first item from addresses with A greater than B
  take 10 items from addresses
  take 10 street from addresses
  take 10 items from addresses with city gleich dortmund
  take 10 street from addresses with city gleich dortmund

  take street from addresses                                     //[hallo, abc, ddd]
  addresses.street
  take street from addresses where city equals dortmund          //[hallo]
  take first 2 street from addresses                             //[hallo, abc]
       first 2 street from addresses                             //[hallo, abc]
  take first street from addresses                               //hallo
       first street from addresses                               //hallo


  addresses:[{stree:'hallo', city:'test'},{stree:'abc', city:'test'},{stree:'dddd', city:'test'}]

  first from numbers
  first 2 from numbers
  first from addresses where zipcode = 5
  first 2 from addresses where zipcode = 5
  first from addresses.zipcode
  first 2 from addresses.zipcode



   */

  @Test
  void first_function_type_with_unknown_array_content() throws Exception {
    String rule = "a first item from addresses as a first address";
    String schema = "{addresses:[]}";

    End2AstRunner.run(
        rule,
        schema,
        r ->
            r.variables()
                .first()
                .operandFunction()
                .hasName("FIRST")
                .hasType(DataPropertyType.Unknown));
  }

  @Test
  void first_function_type_simple_with_object_array_content() throws Exception {
    String rule = "the first item from addresses as the first few addresses";
    String schema = "{addresses:[{zip: 1234}]}";

    End2AstRunner.run(
        rule,
        schema,
        r ->
            r.variables()
                .first()
                .operandFunction()
                .hasName("FIRST")
                .hasType(DataPropertyType.Object));
  }

  @Test
  void first_function_type_simple_with_decimal_array_content() throws Exception {
    String rule = "the first item from numbers as the first few addresses";
    String schema = "{numbers:[1,2,3,4]}";

    End2AstRunner.run(
        rule,
        schema,
        r ->
            r.variables()
                .first()
                .operandFunction()
                .hasName("FIRST")
                .hasType(DataPropertyType.Decimal));
  }

  @Test
  void first_function_type_simple_with_array_array_content() throws Exception {
    String rule = "the first item from arrays as the first few addresses";
    String schema = "{arrays:[[1,2,3,4],[1,2,3,4],[1,2,3,4]]}";

    End2AstRunner.run(
        rule,
        schema,
        r ->
            r.variables()
                .first()
                .operandFunction()
                .hasName("FIRST")
                .hasType(DataPropertyType.Array));
  }

  @Test
  void first_function_simple_on_numbers_array() throws Exception {
    String rule = "the first item from numbers as the first address";
    String schema = "{numbers:[1,2,3,4]}";

    End2AstRunner.run(
        rule,
        schema,
        r ->
            r.variables()
                .first()
                .operandFunction()
                .hasName("FIRST")
                .hasType(DataPropertyType.Decimal)
                .sizeOfParameters(1)
                .parameters()
                .first()
                .property("numbers")
                .hasType(DataPropertyType.Array)
                .hasArrayContentType(DataPropertyType.Decimal));
  }

  @Test
  void first_function_simple_on_numbers_array_with_amount() throws Exception {
    String rule = "the first 2 items from numbers as the first address";
    String schema = "{numbers:[1,2,3,4]}";

    End2AstRunner.run(
        rule,
        schema,
        r ->
            r.variables()
                .first()
                .operandFunction()
                .hasName("FIRST")
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
    "The first item from addresses with zip_code equals 12345 as a first address",
    "The first item from the addresses given with a zip_code number equal to 12345 as a first address",
    "The first item from addresses with zip_code equal to the number 12345 as a first address"
  })
  void first_function_simple_on_object_array_with_condition(String input) throws Exception {
    String rule = input;
    String schema = "{addresses:[{zip_code: 1, city: Berlin}]}";

    End2AstRunner.run(
        rule,
        schema,
        r ->
            r.variables()
                .hasSizeOf(1)
                .first()
                .hasName("a first address")
                .operandFunction()
                .hasName("FIRST")
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
    "The first 2 items from addresses with zip_code equals 12345 as first2addresses",
    "The first 2 items from the addresses given with a zip_code number equal to 12345 as first2addresses",
    "The first 2 items from addresses with zip_code equal to the number 12345 as first2addresses"
  })
  void first_function_simple_on_object_array_with_condition_with_amount(String input)
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
                .hasName("FIRST")
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
  void first_function_on_object_array_explicit_path() throws Exception {
    String rule =
        "The first item from info.addresses with zip_code equals 12345 as a first address";
    String schema = "{info: {addresses:[{zip_code: 1, city: Berlin}]}}";

    End2AstRunner.run(
        rule,
        schema,
        r ->
            r.variables()
                .hasSizeOf(1)
                .first()
                .hasName("a first address")
                .operandFunction()
                .hasName("FIRST")
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

  // todo lionelpa 7.10.19 simple array access with FIRST may need changes in grammar
  @Disabled
  @Test
  void first_function_in_direct_comparison() throws Exception {
    String rule = "FIRST item FROM numbers IS 1 as sadfasdf";
    String schema = "{numbers: [1,2,3,4]}";

    End2AstRunner.run(rule, schema, r -> r.variables());
  }

  @Test
  void first_function_with_get_array_of_with_decimal_property_has_correct_type() throws Exception {
    String rule = "FIRST item FROM numbers.value as var";
    String schema = "{numbers: [{value: 1}]}";

    End2AstRunner.run(
        rule,
        schema,
        r ->
            r.variables()
                .first()
                .operandFunction()
                .hasName("FIRST")
                .hasType(DataPropertyType.Decimal));
  }

  @Test
  void first_function_variable_in_condition_on_jsondata() throws Exception {
    String rule = "FIRST FROM numbers as X \n\n" + "If X is greater than 2 then error";
    String schema = "{numbers: [1,2,3]}";

    End2AstRunner.run(rule, schema, r -> r.variables());
  }

  @Test
  void first_function_variable_in_condition_on_jsondata_with_amount() throws Exception {
    String rule = "FIRST 1 FROM numbers as X \n\n" + "If X is greater than 2 then error";
    String schema = "{numbers: [1,2,3]}";

    End2AstRunner.run(rule, schema, r -> r.variables());
  }

  @Test
  void first_function_variable_in_condition_on_jsonschema() throws Exception {
    String rule = "FIRST FROM numbers as X \n\n" + "If X is greater than 2 then error";
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

  //  @Test
  //  void first_function_variable_in_condition_on_jsonschema() throws Exception {
  //    String rule = "FIRST FROM numbers as X \n\n" + "If X is greater than 2 then error";
  //    String schema =
  //        "{}";
  //
  //    End2AstRunner.run(rule, schema, r -> r.variables());
  //  }

  //  @Test
  //  void take_function_simple() throws Exception {
  //    String rule = "take 10 items from addresses as a first address";
  //    String schema = "{addresses:[{zip_code: 1, city: Berlin}]}";
  //
  //    End2AstRunner.run(
  //        rule,
  //        schema,
  //        r ->
  //            r.variables()
  //                .hasSizeOf(1)
  //                .first()
  //                  .hasName("a first address")
  //                  .operandFunction()
  //                    .hasName("TAKE")
  //                    .hasType(DataPropertyType.Array)
  //                    .sizeOfParameters(1)
  //                    .parameters()
  //                      .first()
  //
  //    );
  //  }
}
