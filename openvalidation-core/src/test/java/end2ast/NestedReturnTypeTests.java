package end2ast;

import io.openvalidation.common.ast.ASTComparisonOperator;
import io.openvalidation.common.data.DataPropertyType;
import org.junit.jupiter.api.Test;

public class NestedReturnTypeTests {

  @Test
  void boolean_variable_containing_a_function() throws Exception {

    String input = "The first item from booleans AS X\n\n" + "If X then error";

    End2AstRunner.run(
        input,
        "{booleans: [true]}",
        "en",
        r ->
            r.rules()
                .first()
                .condition()
                .hasOperator(ASTComparisonOperator.EQUALS)
                .leftVariable()
                .hasType(DataPropertyType.Boolean)
                .hasName("X")
                .parentCondition()
                .rightBoolean(true));
  }

  @Test
  void boolean_variable_containing_a_function_containing_a_variable() throws Exception {

    String input = "booleans AS var\n\n" + "The first item from var AS X\n\n" + "If X then error";

    End2AstRunner.run(
        input,
        "{booleans: [true]}",
        "en",
        r ->
            r.rules()
                .first()
                .condition()
                .hasOperator(ASTComparisonOperator.EQUALS)
                .leftVariable()
                .hasType(DataPropertyType.Boolean)
                .hasName("X")
                .parentCondition()
                .rightBoolean(true));
  }

  @Test
  void boolean_function_containing_a_variable() throws Exception {

    String input = "booleans AS var\n\n" + "The first item from var AS X\n\n" + "If X then error";

    End2AstRunner.run(
        input,
        "{booleans: [true]}",
        "en",
        r ->
            r.variables()
                .second()
                .operandFunction()
                .hasType(DataPropertyType.Boolean)
                .hasName("FIRST"));
  }

  @Test
  void boolean_function_containing_a_variable_containing_a_function() throws Exception {

    String input =
        "first 2 from booleans AS var\n\n" + "The first item from var AS X\n\n" + "If X then error";

    End2AstRunner.run(
        input,
        "{booleans: [true]}",
        "en",
        r ->
            r.variables()
                .second()
                .operandFunction()
                .hasType(DataPropertyType.Boolean)
                .hasName("FIRST"));
  }
}
