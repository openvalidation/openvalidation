package exceptionhandling.lambdas;

import exceptionhandling.ExceptionRunner;
import org.junit.jupiter.api.Test;

public class FirstTest {

  private ExceptionRunner runner = new ExceptionRunner();

  // applied on property
  @Test
  void one_parameter_missing_array_property() throws Exception {
    runner.run(
        "FIRST from AS var",
        "{numbers: [1,2,3]}",
        r -> r.containsValidationMessage("The function FIRST requires at least one parameter"));
  }

  @Test
  void one_parameter_applied_on_string_property() throws Exception {
    runner.run(
        "FIRST from name AS var",
        "{name: Peter}",
        r ->
            r.containsValidationMessage(
                "The function FIRST has to be applied on a property of type 'Array'. But is applied on property of type 'String'"));
  }

  @Test
  void one_parameter_applied_on_decimal_property() throws Exception {
    runner.run(
        "FIRST from number AS var",
        "{number: 1}",
        r ->
            r.containsValidationMessage(
                "The function FIRST has to be applied on a property of type 'Array'. But is applied on property of type 'Decimal'"));
  }

  @Test
  void one_parameter_applied_on_boolean_property() throws Exception {
    runner.run(
        "FIRST from married AS var",
        "{married: true}",
        r ->
            r.containsValidationMessage(
                "The function FIRST has to be applied on a property of type 'Array'. But is applied on property of type 'Boolean'"));
  }

  // applied on static
  @Test
  void one_parameter_applied_on_static_string() throws Exception {
    runner.run(
        "FIRST from something no one knows about AS var",
        r ->
            r.containsValidationMessage(
                "The function FIRST has to be applied on a property, variable or a nested function of type 'Array'. Currently applied on a string(ASTOperandStaticString)."));
  }

  @Test
  void one_parameter_applied_on_static_number() throws Exception {
    runner.run(
        "FIRST from 5 AS var",
        r ->
            r.containsValidationMessage(
                "The function FIRST has to be applied on a property, variable or a nested function of type 'Array'. Currently applied on a number(ASTOperandStaticNumber)."));
  }

  @Test
  void one_parameter_applied_with_negative_amount() throws Exception {
    runner.run(
        "FIRST -1 from numbers AS var",
        "{numbers: [1,2,3]}",
        r ->
            r.containsValidationMessage(
                "The function FIRST only takes numbers with a value greater or equal to 1 as the second parameter. Current value is -1.0"));
  }

  // applied on variables
  @Test
  void one_parameter_applied_on_variable_of_type_string() throws Exception {
    runner.run(
        "name AS var\n\n" + "FIRST from var AS var2",
        "{name: Peter}",
        r ->
            r.containsValidationMessage(
                "The function FIRST has to be applied on a variable of type 'Array'. But is applied on variable of type 'String'"));
  }

  @Test
  void one_parameter_applied_on_variable_of_type_decimal() throws Exception {
    runner.run(
        "number AS var\n\n" + "FIRST from var AS var2",
        "{number: 42}",
        r ->
            r.containsValidationMessage(
                "The function FIRST has to be applied on a variable of type 'Array'. But is applied on variable of type 'Decimal'"));
  }

  @Test
  void one_parameter_applied_on_variable_of_type_bool() throws Exception {
    runner.run(
        "married AS var\n\n" + "FIRST from var AS var2",
        "{married: true}",
        r ->
            r.containsValidationMessage(
                "The function FIRST has to be applied on a variable of type 'Array'. But is applied on variable of type 'Boolean'"));
  }

  @Test
  void one_parameter_applied_on_variable_of_type_object() throws Exception {
    runner.run(
        "person AS var\n\n" + "FIRST from var AS var2",
        "{person: {name: Peter}}",
        r ->
            r.containsValidationMessage(
                "The function FIRST has to be applied on a variable of type 'Array'. But is applied on variable of type 'Object'"));
  }

  // applied on variables containing functions
  @Test
  void one_parameter_applied_on_variable_containing_function_of_type_string() throws Exception {
    runner.run(
        "FIRST from names AS var\n\n" + "FIRST from var AS var2",
        "{names: [Peter]}",
        r ->
            r.containsValidationMessage(
                "The function FIRST has to be applied on a variable of type 'Array'. But is applied on variable of type 'String'"));
  }

  @Test
  void one_parameter_applied_on_variable_containing_function_of_type_decimal() throws Exception {
    runner.run(
        "FIRST from numbers AS var\n\n" + "FIRST from var AS var2",
        "{numbers: [1,2,3]}",
        r ->
            r.containsValidationMessage(
                "The function FIRST has to be applied on a variable of type 'Array'. But is applied on variable of type 'Decimal'"));
  }

  @Test
  void one_parameter_applied_on_variable_containing_function_of_type_bool() throws Exception {
    runner.run(
        "FIRST from bools AS var\n\n" + "FIRST from var AS var2",
        "{bools: [true, false]}",
        r ->
            r.containsValidationMessage(
                "The function FIRST has to be applied on a variable of type 'Array'. But is applied on variable of type 'Boolean'"));
  }

  @Test
  void one_parameter_applied_on_variable_containing_function_of_type_object() throws Exception {
    runner.run(
        "FIRST from staff AS var\n\n" + "FIRST from var AS var2",
        "{staff: [{name: Peter}]}",
        r ->
            r.containsValidationMessage(
                "The function FIRST has to be applied on a variable of type 'Array'. But is applied on variable of type 'Object'"));
  }

  // nesting
  @Test
  void nested_where_function_with_lambda_condition_with_missing_right_operand() throws Exception {
    // check if validation of parameters is triggered
    runner.run(
        "FIRST from numbers with val greater than AS var",
        "{numbers: [{val: 12}]}",
        r -> r.containsValidationMessage("missing right operand in condition"));
  }

  @Test
  void nested_where_function_with_lambda_condition_with_missing_left_operand() throws Exception {
    // check if validation of parameters is triggered
    runner.run(
        "FIRST from numbers with greater than val AS var",
        "{numbers: [{val: 12}]}",
        r -> r.containsValidationMessage("missing left operand in condition"));
  }
}
