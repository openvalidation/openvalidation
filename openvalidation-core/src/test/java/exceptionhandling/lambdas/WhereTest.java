package exceptionhandling.lambdas;

import end2ast.End2AstRunner;
import io.openvalidation.common.data.DataPropertyType;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

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

  @Disabled
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
                .hasArrayContentType(DataPropertyType.Object));
  }

  @Disabled
  @Test
  void where_function_in_first_function_on_int_array_simplified() throws Exception {
    String rule = "first numbers with 42 as myNumber";
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
                .hasArrayContentType(DataPropertyType.Object));
  }
}
