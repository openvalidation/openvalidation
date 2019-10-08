package end2ast.lambdas;

import end2ast.End2AstRunner;
import io.openvalidation.common.ast.ASTComparisonOperator;
import io.openvalidation.common.ast.condition.ASTConditionConnector;
import io.openvalidation.common.data.DataPropertyType;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class LastFunctionTests {
  @Test
  void last_function_simple() throws Exception {
    String rule = "a last item from addresses as a last address";
    String schema = "{addresses:[]}";

    End2AstRunner.run(
        rule,
        schema,
        r ->
            r.variables()
                .hasSizeOf(1)
                .first()
                .hasName("a last address")
                .operandFunction()
                .hasName("LAST")
                .hasType(DataPropertyType.Object)
                .sizeOfParameters(1)
                .parameters()
                .first()
                .property("addresses")
                .hasType(DataPropertyType.Array));
  }

  @Test
  void last_function_simple_with_specific_amount() throws Exception {
    String rule = "the last 5 items from addresses as the last few addresses";
    String schema = "{addresses:[]}";

    End2AstRunner.run(
        rule,
        schema,
        r ->
            r.variables()
                .hasSizeOf(1)
                .first()
                .hasName("the last few addresses")
                .operandFunction()
                .hasName("LAST")
                .hasType(DataPropertyType.Object)
                .sizeOfParameters(2)
                .parameters()
                .first()
                .property("addresses")
                .hasType(DataPropertyType.Array)
                .parentList()
                .second()
                .number()
                .hasValue(5.0));
  }

  @Test
  void last_function_with_simple_condition() throws Exception {
    String rule = "a last item from addresses with zip_code equals 12345 as a last address";
    String schema = "{addresses:[{zip_code: 1, city: Berlin}]}";

    End2AstRunner.run(
        rule,
        schema,
        r ->
            r.variables()
                .hasSizeOf(1)
                .first()
                .hasName("a last address")
                .operandFunction()
                .hasName("LAST")
                .hasType(DataPropertyType.Object)
                .sizeOfParameters(1)
                .parameters()
                .first()
                .function()
                .hasName("WHERE")
                .sizeOfParameters(2)
                .parameters()
                .first()
                .property("addresses")
                .hasType(DataPropertyType.Array)
                .parentList()
                .second()
                .lambdaCondition()
                .hasOperator(ASTComparisonOperator.EQUALS)
                .hasNoConnector()
                .leftProperty("zip_code")
                .hasType(DataPropertyType.Decimal)
                .parentCondition()
                .rightNumber(12345.0));
  }

  @Test
  void last_function_with_simple_condition_and_sugar_around_array_and_property() throws Exception {
    String rule =
        "The last item from the addresses given with a zip_code number greater than 12345 as a last address";
    String schema = "{addresses:[{zip_code: 1, city: Berlin}]}";

    End2AstRunner.run(
        rule,
        schema,
        r ->
            r.variables()
                .hasSizeOf(1)
                .first()
                .hasName("a last address")
                .operandFunction()
                .hasName("LAST")
                .hasType(DataPropertyType.Object)
                .sizeOfParameters(1)
                .parameters()
                .first()
                .function()
                .hasName("WHERE")
                .sizeOfParameters(2)
                .parameters()
                .first()
                .property("addresses")
                .hasType(DataPropertyType.Array)
                .parentList()
                .second()
                .lambdaCondition()
                .hasOperator(ASTComparisonOperator.GREATER_THAN)
                .hasNoConnector()
                .leftProperty("zip_code")
                .hasType(DataPropertyType.Decimal)
                .parentCondition()
                .rightNumber(12345.0));
  }

  @Test
  void last_function_with_simple_condition_and_sugar_around_number_in_condition() throws Exception {
    String rule =
        "The last item from addresses with zip_code equal to the number 12345 as a last address";
    String schema = "{addresses:[{zip_code: 1, city: Berlin}]}";

    End2AstRunner.run(
        rule,
        schema,
        r ->
            r.variables()
                .hasSizeOf(1)
                .first()
                .hasName("a last address")
                .operandFunction()
                .hasName("LAST")
                .hasType(DataPropertyType.Object)
                .sizeOfParameters(1)
                .parameters()
                .first()
                .function()
                .hasName("WHERE")
                .sizeOfParameters(2)
                .parameters()
                .first()
                .property("addresses")
                .hasType(DataPropertyType.Array)
                .parentList()
                .second()
                .lambdaCondition()
                .hasOperator(ASTComparisonOperator.EQUALS)
                .hasNoConnector()
                .leftProperty("zip_code")
                .hasType(DataPropertyType.Decimal)
                .parentCondition()
                .rightNumber(12345.0));
  }

  @Test
  void last_function_with_simple_condition_with_explicit_array_path() throws Exception {
    String rule = "a last item from info.addresses with zip_code equals 12345 as a last address";
    String schema = "{info: {addresses:[{zip_code: 1, city: Berlin}]}}";

    End2AstRunner.run(
        rule,
        schema,
        r ->
            r.variables()
                .hasSizeOf(1)
                .first()
                .hasName("a last address")
                .operandFunction()
                .hasName("LAST")
                .hasType(DataPropertyType.Object)
                .sizeOfParameters(1)
                .parameters()
                .first()
                .function()
                .hasName("WHERE")
                .sizeOfParameters(2)
                .parameters()
                .first()
                .property("addresses")
                .hasType(DataPropertyType.Array)
                .parentList()
                .second()
                .lambdaCondition()
                .hasOperator(ASTComparisonOperator.EQUALS)
                .hasNoConnector()
                .leftProperty("zip_code")
                .hasType(DataPropertyType.Decimal)
                .parentCondition()
                .rightNumber(12345.0));
  }

  @Test
  void last_function_with_condition_group_with_explicit_array_path() throws Exception {
    String rule =
        "a last item from info.addresses with zip_code equals 12345 and city equals Berlin as a last address";
    String schema = "{info: {addresses:[{zip_code: 1, city: Berlin}]}}";

    End2AstRunner.run(
        rule,
        schema,
        r ->
            r.variables()
                .hasSizeOf(1)
                .first()
                .hasName("a last address")
                .operandFunction()
                .hasName("LAST")
                .hasType(DataPropertyType.Object)
                .sizeOfParameters(1)
                .parameters()
                .first()
                .function()
                .hasName("WHERE")
                .sizeOfParameters(2)
                .parameters()
                .first()
                .property("addresses")
                .hasType(DataPropertyType.Array)
                .parentList()
                .second()
                .lambdaConditionGroup()
                .hasSize(2)
                .first()
                .hasOperator(ASTComparisonOperator.EQUALS)
                .hasNoConnector()
                .leftProperty("zip_code")
                .hasType(DataPropertyType.Decimal)
                .parentCondition()
                .rightNumber(12345.0)
                .parentConditionGroup()
                .second()
                .hasOperator(ASTComparisonOperator.EQUALS)
                .hasConnector(ASTConditionConnector.AND)
                .leftProperty("city")
                .hasType(DataPropertyType.String)
                .parentCondition()
                .rightString("Berlin"));
  }

  // todo lionelpa 7.10.19 simple array access with LAST may need changes in grammar
  @Disabled
  @Test
  void abcdef() throws Exception {
    String rule = "LAST item FROM numbers IS 1 as sadfasdf";
    String schema = "{numbers: [1,2,3,4]}";

    End2AstRunner.run(rule, schema, r -> r.variables());
  }
}
