package exceptionhandling.lambdas;

import exceptionhandling.ExceptionRunner;
import org.junit.jupiter.api.Test;

public class LastTest {
  private ExceptionRunner runner = new ExceptionRunner();

  @Test
  void one_parameter_missing_array_property() throws Exception {
    runner.run(
        "LAST from AS var",
        "{numbers: [1,2,3]}",
        r -> r.containsValidationMessage("The function LAST requires at least one parameter"));
  }

  @Test
  void one_parameter_applied_on_string_property() throws Exception {
    runner.run(
        "LAST from name AS var",
        "{name: Peter}",
        r ->
            r.containsValidationMessage(
                "The function LAST has to be applied on a property of type 'Array'. But is applied on property of type 'String'"));
  }

  @Test
  void one_parameter_applied_on_decimal_property() throws Exception {
    runner.run(
        "LAST from number AS var",
        "{number: 1}",
        r ->
            r.containsValidationMessage(
                "The function LAST has to be applied on a property of type 'Array'. But is applied on property of type 'Decimal'"));
  }

  @Test
  void one_parameter_applied_on_boolean_property() throws Exception {
    runner.run(
        "LAST from married AS var",
        "{married: true}",
        r ->
            r.containsValidationMessage(
                "The function LAST has to be applied on a property of type 'Array'. But is applied on property of type 'Boolean'"));
  }

  @Test
  void one_parameter_applied_on_static_string() throws Exception {
    runner.run(
        "LAST from something no one knows about AS var",
        r ->
            r.containsValidationMessage(
                "The function LAST has to be applied on an array property or a nested function. Currently applied on ASTOperandStaticString"));
  }
  
  @Test
  void lambda_condition_with_missing_right_operand() throws Exception {
    // check if validation of parameters is triggered
    runner.run(
        "LAST from numbers with val greater than AS var",
        "{numbers: [{val: 12}]}",
        r -> r.containsValidationMessage("missing right operand in condition"));
  }

  @Test
  void lambda_condition_with_missing_left_operand() throws Exception {
    // check if validation of parameters is triggered
    runner.run(
        "LAST from numbers with greater than val AS var",
        "{numbers: [{val: 12}]}",
        r -> r.containsValidationMessage("missing left operand in condition"));
  }
}
