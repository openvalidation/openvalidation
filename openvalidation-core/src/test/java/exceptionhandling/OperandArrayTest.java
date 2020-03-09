package exceptionhandling;

public class OperandArrayTest {

    ExceptionRunner runner = new ExceptionRunner();

//  @ParameterizedTest
//  @ValueSource(
//      strings = {
//        "a LESS AS b",
//        "a \n LESS \n AS b",
//        "IF a EQUALS THEN bla",
//        "IF a EQUALS \n  THEN bla",
//        "a MUST be LESS "
//      })
//  public void missing_left_operand(String rule) throws Exception {
//    runner.run(
//        rule,
//        r -> {
//          r.containsValidationMessage("missing right operand in condition");
//        });
//  }
}
