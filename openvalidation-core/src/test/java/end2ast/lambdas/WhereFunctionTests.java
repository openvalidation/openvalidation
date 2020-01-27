package end2ast.lambdas;

import end2ast.End2AstRunner;
import io.openvalidation.common.ast.ASTComparisonOperator;
import io.openvalidation.common.data.DataPropertyType;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class WhereFunctionTests {

  @Test
  void where_on_simple_type_array_without_sugar() throws Exception {
    String input = "first from numbers with a value greater than 1 AS myNumber";
    String schema = "{numbers:[1,2,3]}";

    End2AstRunner.run(
        input,
        schema,
        r ->
            r.variables()
                .first()
                .function()
                .parameters()
                .first()
                .function("WHERE")
                .sizeOfParameters(2)
                .firstProperty()
                .hasPath("numbers")
                .hasType(DataPropertyType.Array)
                .hasArrayContentType(DataPropertyType.Decimal)
                .parentFunction()
                .secondLambda()
                .condition()
                .leftProperty()
                .hasEmptyPath()
                .hasType(DataPropertyType.Decimal)
                .hasSameLambdaTokenAsParentLambdaCondition());
  }

  @Test
  void where_on_simple_type_array_with_sugar() throws Exception {
    String input = "first from numbers with a value greater than the number 1 AS myNumber";
    String schema = "{numbers:[1,2,3]}";

    End2AstRunner.run(
        input,
        schema,
        r ->
            r.variables()
                .first()
                .function()
                .parameters()
                .first()
                .function("WHERE")
                .sizeOfParameters(2)
                .firstProperty()
                .hasPath("numbers")
                .hasType(DataPropertyType.Array)
                .hasArrayContentType(DataPropertyType.Decimal)
                .parentFunction()
                .secondLambda()
                .condition()
                .leftProperty()
                .hasEmptyPath()
                .hasType(DataPropertyType.Decimal)
                .hasSameLambdaTokenAsParentLambdaCondition());
  }

  @Test
  void where_on_simple_type_array_has_correct_return_types() throws Exception {
    String input = "first from numbers with a value greater than 1 AS myNumber";
    String schema = "{numbers:[0.5, 1.1, 1, 2,3]}";

    End2AstRunner.run(
        input,
        schema,
        r ->
            r.variables()
                .first()
                .function()
                .parameters()
                .first()
                .function("WHERE")
                .hasType(DataPropertyType.Array)
                .hasArrayContentType(DataPropertyType.Decimal));
  }

  @Test
  void where_function_in_first_function_has_correct_type() throws Exception {
    String rule = "a first item from addresses with zip_code equals 12345 as a first address";
    String schema = "{addresses:[{zip_code: 1, city: Berlin}]}";

    End2AstRunner.run(
        rule,
        schema,
        r ->
            r.variables()
                .first()
                .operandFunction()
                .parameters()
                .first()
                .function()
                .hasName("WHERE")
                .hasType(DataPropertyType.Array));
  }

  @Test
  void where_function_in_first_function_has_correct_array_content_type() throws Exception {
    String rule = "a first item from addresses with zip_code equals 12345 as a first address";
    String schema = "{addresses:[{zip_code: 1, city: Berlin}]}";

    End2AstRunner.run(
        rule,
        schema,
        r ->
            r.variables()
                .first()
                .operandFunction()
                .parameters()
                .first()
                .function()
                .hasName("WHERE")
                .hasArrayContentType(DataPropertyType.Object));
  }

  @Test
  void where_function_in_first_function_on_int_array() throws Exception {
    String rule = "a first item from numbers with item equals 42 as myNumber";
    String schema = "{numbers:[1.1,2,3,4,5]}";

    End2AstRunner.run(
        rule,
        schema,
        r ->
            r.variables()
                .first()
                .operandFunction()
                .parameters()
                .first()
                .function()
                .hasName("WHERE")
                .hasArrayContentType(DataPropertyType.Decimal));
  }

  @Test
  void compare_primitive_datatype_number_after_where_filter() throws Exception {
    String rule = "first from numbers with a value greater than 1 AS myNumber";
    String schema = "{numbers:[0.5,1,2,3]}";

    End2AstRunner.run(
        rule,
        schema,
        r ->
            r.variables()
                .first()
                .operandFunction()
                .parameters()
                .first()
                .function()
                .hasName("WHERE")
                .hasArrayContentType(DataPropertyType.Decimal));
  }

  @Test
  void compare_primitive_datatype_string_after_where_filter() throws Exception {
    String rule = "first from strings with a value equals peter 1 AS myNumber";
    String schema = "{strings:['peter', 'paul', 'marry']}";

    End2AstRunner.run(
        rule,
        schema,
        r ->
            r.variables()
                .first()
                .operandFunction()
                .parameters()
                .first()
                .function()
                .hasName("WHERE")
                .hasArrayContentType(DataPropertyType.String));
  }

  @Test
  void compare_primitive_datatype_number_cascading_after_where_filter() throws Exception {
    String rule = "first 2 number from numbers with number greater 2 AS X";
    String schema = "{numbers:[0.5,1,2,3]}";

    End2AstRunner.run(
        rule,
        schema,
        r ->
            r.variables()
                .first()
                .operandFunction()
                .parameters()
                .first()
                .function()
                .hasName("WHERE")
                .hasArrayContentType(DataPropertyType.Decimal));
  }

  @Test
  void lambda_with_static_component_compare() throws Exception {
    String rule = "first from person with value 5 equal to age as a_variable";
    String schema = "{person:[{age:18, name:'Adam'}]}";

    End2AstRunner.run(
        rule,
        schema,
        r ->
            r.variables()
                .first()
                .operandFunction()
                .parameters()
                .first()
                .function()
                .hasName("WHERE")
                .hasArrayContentType(DataPropertyType.Object)
                .parameters()
                .hasSizeOf(2)
                .second()
                .lambdaCondition()
                .leftNumber()
                .hasValue(5.0)
                .parentCondition()
                .rightProperty()
                .hasPath("age"));
  }

  // implicid booleans in WHERE-function
  @Test
  @Disabled
  void implicit_condition_in_where_with_boolean_value_simple_bool_array() throws Exception {
    String rule = "first from bools with a value AS myBool";
    String schema = "{bools:[false,false,false,true]}";

    End2AstRunner.run(
        rule,
        schema,
        r ->
            r.variables()
                .first()
                .operandFunction()
                .parameters()
                .first()
                .function()
                .hasName("WHERE")
                .hasArrayContentType(DataPropertyType.Boolean));
  }

  @ParameterizedTest
  @ValueSource(
      strings = {
        "first from people with married AS myPerson",
        "first from people with married is AS myPerson",
        "first from people with married is true AS myPerson",
      })
  void implicit_condition_in_where_with_boolean_value_complex_object_1(String input)
      throws Exception {
    String rule = input;
    String schema =
        "{people:[{name:'paul', married:true}, {name:'peter', married:false}, {name:'marry', married:false}]}";

    End2AstRunner.run(
        rule,
        schema,
        r ->
            r.variables()
                .first()
                .operandFunction()
                .parameters()
                .first()
                .function()
                .hasName("WHERE")
                .parameters()
                .second()
                .lambdaCondition()
                .leftProperty()
                .hasType(DataPropertyType.Boolean)
                .hasPath("married")
                .hasSameLambdaTokenAsParentLambdaCondition()
                .parentCondition()
                .hasOperator(ASTComparisonOperator.EQUALS)
                .rightBoolean(true));
  }

  @ParameterizedTest
  @ValueSource(
      strings = {
        "first from people with is married AS myPerson",
        "first from people with true is married true AS myPerson",
      })
  void implicit_condition_in_where_with_boolean_value_complex_object_2(String input)
      throws Exception {
    String rule = input;
    String schema =
        "{people:[{name:'paul', married:true}, {name:'peter', married:false}, {name:'marry', married:false}]}";

    End2AstRunner.run(
        rule,
        schema,
        r ->
            r.variables()
                .first()
                .operandFunction()
                .parameters()
                .first()
                .function()
                .hasName("WHERE")
                .parameters()
                .second()
                .lambdaCondition()
                .rightProperty()
                .hasType(DataPropertyType.Boolean)
                .hasPath("married")
                .hasSameLambdaTokenAsParentLambdaCondition()
                .parentCondition()
                .hasOperator(ASTComparisonOperator.EQUALS)
                .leftBoolean(true));
  }
}
