package exceptionhandling.lambdas;

import exceptionhandling.ExceptionRunner;
import org.junit.jupiter.api.Test;

public class FirstTest {

  private ExceptionRunner runner = new ExceptionRunner();

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
                  r -> r.containsValidationMessage("The function FIRST has to be applied on a property of type 'Array'. But is applied on property of type 'String'"));
      }

      @Test
      void one_parameter_applied_on_decimal_property() throws Exception {
          runner.run(
                  "FIRST from number AS var",
                  "{number: 1}",
                  r -> r.containsValidationMessage("The function FIRST has to be applied on a property of type 'Array'. But is applied on property of type 'Decimal'"));
      }

      @Test
      void one_parameter_applied_on_boolean_property() throws Exception {
          runner.run(
                  "FIRST from married AS var",
                  "{married: true}",
                  r -> r.containsValidationMessage("The function FIRST has to be applied on a property of type 'Array'. But is applied on property of type 'Boolean'"));
      }

      @Test
      void one_parameter_applied_on_static_string() throws Exception {
          runner.run(
                  "FIRST from something no one knows about AS var",
                  r -> r.containsValidationMessage("The function FIRST has to be applied on an array property or a nested function. Currently applied on ASTOperandStaticString"));
      }
}
