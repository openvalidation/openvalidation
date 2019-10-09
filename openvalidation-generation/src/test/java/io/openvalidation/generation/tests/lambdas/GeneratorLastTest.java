package io.openvalidation.generation.tests.lambdas;

import io.openvalidation.common.ast.ASTComparisonOperator;
import io.openvalidation.common.ast.builder.ASTConditionBuilder;
import io.openvalidation.common.ast.builder.ASTConditionGroupBuilder;
import io.openvalidation.common.ast.builder.ASTOperandFunctionBuilder;
import io.openvalidation.common.ast.condition.ASTConditionConnector;
import io.openvalidation.generation.tests.ExpectationBuilder;
import io.openvalidation.generation.tests.GTE;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

public class GeneratorLastTest {
        private static Stream<Arguments> last_test_with_specific_amount() {
    return ExpectationBuilder.newExpectation()
        .javaResult("huml.LAST(model.getAddresses(), 1.0)")
        .toStream();
  }

  @ParameterizedTest(name = GTE.PARAM_TEST_NAME)
  @MethodSource()
  public void last_test_with_specific_amount(String language, String expected)
      throws Exception {
    GTE.execute(
        expected,
        language,
        p -> {

          String[] addressPath = {"addresses"};

          ASTOperandFunctionBuilder builder = new ASTOperandFunctionBuilder();
          builder.createLastFunction(addressPath);

          return builder.getModel();
        });
  }

  private static Stream<Arguments> last_test_with_condition() {
    return ExpectationBuilder.newExpectation()
        .javaResult("huml.LAST(huml.WHERE(model.getAddresses(), x -> huml.EQUALS(x.getCity(), \"Berlin\")))")
        .toStream();
  }

  @ParameterizedTest(name = GTE.PARAM_TEST_NAME)
  @MethodSource()
  public void last_test_with_condition(String language, String expected)
      throws Exception {
    GTE.execute(
        expected,
        language,
        p -> {
            ASTConditionBuilder conditionBuilder = new ASTConditionBuilder();
            conditionBuilder.create().withLeftOperandAsProperty("city")
                .withRightOperandAsString("Berlin")
                .withOperator(ASTComparisonOperator.EQUALS);

          ASTOperandFunctionBuilder builder = new ASTOperandFunctionBuilder();
          builder.createFunction("LAST")
            .addParameterAsFunction("WHERE")
                .addPropertyParameter("addresses")
                .addLambdaConditionParameter(conditionBuilder.getModel(), "x");


          return builder.getModel();
        });
  }

  private static Stream<Arguments> last_test_with_condition_and_amount() {
    return ExpectationBuilder.newExpectation()
        .javaResult("huml.LAST(huml.WHERE(model.getAddresses(), x -> huml.EQUALS(x.getCity(), \"Berlin\")), 1.0)")
        .toStream();
  }

  @ParameterizedTest(name = GTE.PARAM_TEST_NAME)
  @MethodSource()
  public void last_test_with_condition_and_amount(String language, String expected)
      throws Exception {
    GTE.execute(
        expected,
        language,
        p -> {
            ASTConditionBuilder conditionBuilder = new ASTConditionBuilder();
            conditionBuilder.create().withLeftOperandAsProperty("city")
                .withRightOperandAsString("Berlin")
                .withOperator(ASTComparisonOperator.EQUALS);

          ASTOperandFunctionBuilder builder = new ASTOperandFunctionBuilder();
          builder.createFunction("LAST")
            .addParameterAsFunction("WHERE")
                .addPropertyParameter("addresses")
                .addLambdaConditionParameter(conditionBuilder.getModel(), "x")
          .getParentFuncBuilder()
            .addNumberParameter(1);


          return builder.getModel();
        });
  }

  private static Stream<Arguments> last_test_with_condition_group_and_amount() {
    return ExpectationBuilder.newExpectation()
        .javaResult("huml.LAST(huml.WHERE(model.getAddresses(), x -> (huml.EQUALS(x.getCity(), \"Berlin\") && huml.NOT_EQUALS(x.getCity(), x.getBirthplace()))), 1.0)")
        .toStream();
  }

  @ParameterizedTest(name = GTE.PARAM_TEST_NAME)
  @MethodSource()
  public void last_test_with_condition_group_and_amount(String language, String expected)
      throws Exception {
    GTE.execute(
        expected,
        language,
        p -> {
            ASTConditionGroupBuilder groupBuilder = new ASTConditionGroupBuilder();
            groupBuilder.create().
                    appendCondition(null)
                    .withLeftOperandAsProperty("city")
                    .withRightOperandAsString("Berlin")
                    .withOperator(ASTComparisonOperator.EQUALS)
            .parentGroup()
                    .appendCondition(ASTConditionConnector.AND)
                    .withLeftOperandAsProperty("city")
                    .withRightOperandAsProperty("birthplace")
                    .withOperator(ASTComparisonOperator.NOT_EQUALS);


          ASTOperandFunctionBuilder builder = new ASTOperandFunctionBuilder();
          builder.createFunction("LAST")
            .addParameterAsFunction("WHERE")
                .addPropertyParameter("addresses")
                .addLambdaConditionParameter(groupBuilder.getModel(), "x")
          .getParentFuncBuilder()
            .addNumberParameter(1);


          return builder.getModel();
        });
  }
}
