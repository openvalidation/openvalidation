package exceptionhandling.lambdas;

import exceptionhandling.ExceptionRunner;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class SumOfTest {
  private ExceptionRunner runner = new ExceptionRunner();

  @Test
  void applied_on_non_array_property() throws Exception {
    runner.run(
        "SUM OF number.value AS var",
        "{number: {value: 12}}",
        r ->
            r.containsValidationMessage(
                "The first parameter of the function SUM_OF has to be an array property of type 'Array'. Type found: Decimal"));
  }

  @Test
  void applied_on_non_array_variable() throws Exception {
    runner.run(
        "numbers.val AS variable\n\n" + "SUM OF variable AS var",
        "{numbers: {val: 12}}",
        r ->
            r.containsValidationMessage(
                "The first parameter of the function SUM_OF has to be an array property of type 'Array'. Type found: Decimal"));
  }

  @Disabled
  @Test
  void applied_on_array_property_with_strings() throws Exception {
    runner.run(
        "SUM OF numbers AS var",
        "{numbers: [1,2,3]}",
        r ->
            r.containsValidationMessage(
                "The first parameter of the function SUM_OF has to be an array property of type 'Array'. Type found: Decimal"));
  }

  @Disabled
  @Test
  void applied_on_array_variable_with_strings() throws Exception {
    runner.run(
        "numbers AS variable\n\n" + "SUM OF variable AS var",
        "{numbers: [one, two, three]}",
        r ->
            r.containsValidationMessage(
                "The first parameter of the function SUM_OF has to be an array property of type 'Array'. Type found: Decimal"));
  }
}
