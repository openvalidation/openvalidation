package exceptionhandling;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class OperandArrayTest {

  ExceptionRunner runner = new ExceptionRunner();

  @ParameterizedTest
  @ValueSource(
      strings = {
        "staticString, 42, boolProp AS var",
        "stringProp, decimalProp, boolProp AS var",
        "stringVar, decimalVar, boolVar AS var",
        "stringVar, decimalProp, boolVar AS var",
      })
  public void different_types_in_array(String rule) throws Exception {
    rule =
        "Peter AS stringVar\n\n"
            + "100 AS decimalVar\n\n"
            + "decimalProp is 42 AS boolVar\n\n"
            + rule;

    runner.run(
        rule,
        "{stringProp: Peter, decimalProp: 100, boolProp: true}",
        r -> {
          r.containsValidationMessage(
              "Array contains elements of different types when it is supposed to only contain elements of a singular type."
                  + " Found types [String, Decimal, Boolean]");
        });
  }
}
