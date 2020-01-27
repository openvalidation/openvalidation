package exceptionhandling.lambdas;

import exceptionhandling.ExceptionRunner;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class WhereTest {

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
