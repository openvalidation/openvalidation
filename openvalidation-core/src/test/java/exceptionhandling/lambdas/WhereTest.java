package exceptionhandling.lambdas;

import end2ast.End2AstRunner;
import exceptionhandling.ExceptionRunner;
import io.openvalidation.common.ast.ASTComparisonOperator;
import io.openvalidation.common.data.DataPropertyType;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class WhereTest {

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

  private ExceptionRunner runner = new ExceptionRunner();

  @Test
  @Disabled
  // todo lazevedo 22.01.20 faulty where function parsed as property 'numbers'. How to handle.
  void missing_condition() throws Exception {
    runner.run(
        "All from numbers with something AS var",
        "{numbers: [1,2,3]}",
        r -> r.containsValidationMessage("Error"));
  }

  @Test
  void property_not_of_type_array() throws Exception {
    runner.run(
        "All from number with value greater than 5 AS var",
        "{number: 1}",
        r ->
            r.containsValidationMessage(
                "The first parameter (property) of WHERE has to be of type 'Array'. Type found: Decimal."));
  }

  @Test
  void property_first_param_is_string() throws Exception {
    runner.run(
        "All from something with value greater than 5 AS var",
        "{numbers:[1,2,3]}",
        r ->
            r.containsValidationMessage(
                "The first parameter of the function WHERE has to be an array property or a nested function. Currently applied on string"));
  }

  @Test
  void both_parameters_in_condition_static_numbers() throws Exception {
    runner.run(
        "All from numbers with 5 greater than 1 AS var",
        "{numbers: [1,2,3]}",
        r ->
            r.containsValidationMessage(
                "at least one operand in comparison should not be static\n"
                    + "left operand is of type: 'astoperandstaticnumber' and right operand is of type: 'astoperandstaticnumber'"));
  }

  @Test
  void both_parameters_in_condition_static_strings() throws Exception {
    runner.run(
        "All from numbers with Hello equals Bye AS var",
        "{numbers: [1,2,3]}",
        r ->
            r.containsValidationMessage(
                "at least one operand in comparison should not be static\n"
                    + "left operand is of type: 'astoperandstaticstring' and right operand is of type: 'astoperandstaticstring'"));
  }
}
