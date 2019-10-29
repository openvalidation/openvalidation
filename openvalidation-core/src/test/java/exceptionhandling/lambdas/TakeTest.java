package exceptionhandling.lambdas;

import exceptionhandling.ExceptionRunner;
import org.junit.jupiter.api.Test;

public class TakeTest {
    
    private ExceptionRunner runner = new ExceptionRunner();

  @Test
  void one_parameter_missing_array_property() throws Exception {
    runner.run(
        "TAKE 10 from AS var",
        "{numbers: [1,2,3]}",
        r -> r.containsValidationMessage("The function TAKE requires at least two parameters"));
  }

  @Test
  void one_parameter_missing_amount() throws Exception {
    runner.run(
        "TAKE from numbers with with val greater than 10 AS var",
        "{numbers: [{val: 12}]}",
        r -> r.containsValidationMessage("The function TAKE requires at least two parameters"));
  }

  @Test
  void one_parameter_applied_on_string_property() throws Exception {
    runner.run(
        "TAKE 10 from name AS var",
        "{name: Peter}",
        r ->
            r.containsValidationMessage(
                "The function TAKE has to be applied on a property of type 'Array'. But is applied on property of type 'String'"));
  }

  @Test
  void one_parameter_applied_on_decimal_property() throws Exception {
    runner.run(
        "TAKE 10 from number AS var",
        "{number: 1}",
        r ->
            r.containsValidationMessage(
                "The function TAKE has to be applied on a property of type 'Array'. But is applied on property of type 'Decimal'"));
  }

  @Test
  void one_parameter_applied_on_boolean_property() throws Exception {
    runner.run(
        "TAKE 10 from married AS var",
        "{married: true}",
        r ->
            r.containsValidationMessage(
                "The function TAKE has to be applied on a property of type 'Array'. But is applied on property of type 'Boolean'"));
  }

  @Test
  void one_parameter_applied_on_static_string() throws Exception {
    runner.run(
        "TAKE 10 from something no one knows about AS var",
        r ->
            r.containsValidationMessage(
                "The function TAKE has to be applied on an array property or a nested function. Currently applied on ASTOperandStaticString"));
  }
  
  @Test
  void lambda_condition_with_missing_right_operand() throws Exception {
    // check if validation of parameters is triggered
    runner.run(
        "TAKE 10 from numbers with val greater than AS var",
        "{numbers: [{val: 12}]}",
        r -> r.containsValidationMessage("missing right operand in condition"));
  }

  @Test
  void lambda_condition_with_missing_left_operand() throws Exception {
    // check if validation of parameters is triggered
    runner.run(
        "TAKE 10 from numbers with greater than val AS var",
        "{numbers: [{val: 12}]}",
        r -> r.containsValidationMessage("missing left operand in condition"));
  }
}
