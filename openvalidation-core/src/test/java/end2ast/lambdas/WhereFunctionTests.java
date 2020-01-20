package end2ast.lambdas;

import end2ast.End2AstRunner;
import io.openvalidation.common.data.DataPropertyType;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class WhereFunctionTests {

  @Disabled
  @Test
  void only_for_debugging_purposes() throws Exception {
    String rule =
        "a first item from numbers with number equals 42 as myNumber\n"
            + "\n"
            + "first item from numbers with number greater than 30 as bigNumber\n"
            + "\n"
            + "if myNumber equals bigNumber then error";
    String schema = "{numbers:[{number: 1234}]}";

    End2AstRunner.run(rule, schema, r -> r.variables());
  }

  @Test
  void where_on_simple_type_array_without_sugar() throws Exception {
    String input = "first from numbers with a value greater than 1 AS myNumber";
    String schema = "{numbers:[1,2,3]}";

    End2AstRunner.run(
        input,
        schema,
        r ->
            r.variables()
                .first()
                .function()
                .parameters()
                .first()
                .function("WHERE")
                .sizeOfParameters(2)
                .firstProperty()
                .hasPath("numbers")
                .hasType(DataPropertyType.Array)
                .hasArrayContentType(DataPropertyType.Decimal)
                .parentFunction()
                .secondLambda()
                .condition()
                .leftProperty()
                .hasEmptyPath()
                .hasType(DataPropertyType.Decimal)
                .hasSameLambdaTokenAsParentLambdaCondition());
  }

  @Test
  void where_on_simple_type_array_with_sugar() throws Exception {
    String input = "first from numbers with a value greater than the number 1 AS myNumber";
    String schema = "{numbers:[1,2,3]}";

    End2AstRunner.run(
        input,
        schema,
        r ->
            r.variables()
                .first()
                .function()
                .parameters()
                .first()
                .function("WHERE")
                .sizeOfParameters(2)
                .firstProperty()
                .hasPath("numbers")
                .hasType(DataPropertyType.Array)
                .hasArrayContentType(DataPropertyType.Decimal)
                .parentFunction()
                .secondLambda()
                .condition()
                .leftProperty()
                .hasEmptyPath()
                .hasType(DataPropertyType.Decimal)
                .hasSameLambdaTokenAsParentLambdaCondition());
  }

  @Test
  void where_on_simple_type_array_has_correct_return_types() throws Exception {
    String input = "first from numbers with a value greater than 1 AS myNumber";
    String schema = "{numbers:[0.5, 1.1, 1, 2,3]}";

    End2AstRunner.run(
        input,
        schema,
        r ->
            r.variables()
                .first()
                .function()
                .parameters()
                .first()
                .function("WHERE")
                .hasType(DataPropertyType.Array)
                .hasArrayContentType(DataPropertyType.Decimal));
  }
}
