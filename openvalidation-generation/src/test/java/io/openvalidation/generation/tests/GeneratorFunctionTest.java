/*
 *    Copyright 2019 BROCKHAUS AG
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package io.openvalidation.generation.tests;

import io.openvalidation.common.ast.builder.ASTConditionBuilder;
import io.openvalidation.common.ast.condition.ASTCondition;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class GeneratorFunctionTest {

  private static Stream<Arguments> schould_generate_function() {
    return Stream.of(
        Arguments.of(
            "java",
            "(huml.SUM_OF(huml.GET_ARRAY_OF(model.getPortfolio().getShares(), s -> s.getPercentage())))")
        // Arguments.of("csharp", "huml.SUM_OF(huml.GET_ARRAY_OF(model.Portfolio.Shares, s =>
        // s.Percentage))")
        // Arguments.of("javascript", "huml.SUM_OF(huml.GET_ARRAY_OF(model.Portfolio.Shares, s =>
        // s.Percentage))")
        );
  }

  @ParameterizedTest(name = GTE.PARAM_TEST_NAME)
  @MethodSource()
  public void schould_generate_function(String language, String expected) throws Exception {
    GTE.execute(
        expected,
        language,
        p -> {
          ASTConditionBuilder conditionBuilder = new ASTConditionBuilder();
          conditionBuilder.create();

          String[] arrayPath = {"portfolio", "shares"};
          String[] lambdaPath = {"percentage"};

          ASTCondition condition =
              conditionBuilder
                  .createLeftOperandAsFunction("SUM_OF")
                  .addParameterAsArrayOfFunction(arrayPath, "s", lambdaPath)
                  .getParentFuncBuilder()
                  .parent()
                  .getModel();

          return condition;
        });
  }

  private static Stream<Arguments> schould_generate_function_with_lambda() {
    return Stream.of(
        Arguments.of(
            "java",
            "(huml.SUM_OF(huml.GET_ARRAY_OF(model.getPortfolio().getShares(), s -> s.getPercentage())))"),
        Arguments.of(
            "csharp",
            "(huml.SUM_OF(huml.GET_ARRAY_OF(model.Portfolio.Shares, s => s.Percentage)))"),
        // Arguments.of("javascript", "huml.SUM_OF(huml.GET_ARRAY_OF(model.Portfolio.Shares, s =>
        // s.Percentage))")
        Arguments.of(
            "python",
            "(huml.SUM_OF(huml.GET_ARRAY_OF(model.portfolio.shares, lambda s: s.percentage)))"));
  }

  @ParameterizedTest(name = GTE.PARAM_TEST_NAME)
  @MethodSource()
  public void schould_generate_function_with_lambda(String language, String expected)
      throws Exception {
    GTE.execute(
        expected,
        language,
        p -> {
          ASTConditionBuilder conditionBuilder = new ASTConditionBuilder();
          conditionBuilder.create();

          String[] array = {"portfolio", "shares"};
          String[] proparray = {"percentage"};

          conditionBuilder
              .createLeftOperandAsFunction("SUM_OF")
              .addParameterAsArrayOfFunction(array, "s", proparray);

          return conditionBuilder.getModel();
        });
  }

  private static Stream<Arguments> schould_generate_first_function() {
    return Stream.of(
        Arguments.of("java", "(huml.FIRST(model.getPortfolio().getShares()))"),
        Arguments.of("csharp", "(huml.FIRST(model.Portfolio.Shares))"),
        // Arguments.of("javascript", "huml.SUM_OF(huml.GET_ARRAY_OF(model.Portfolio.Shares, s =>
        // s.Percentage))")
        Arguments.of("python", "(huml.FIRST(model.portfolio.shares))"));
  }

  @ParameterizedTest(name = GTE.PARAM_TEST_NAME)
  @MethodSource()
  public void schould_generate_first_function(String language, String expected) throws Exception {
    GTE.execute(
        expected,
        language,
        p -> {
          ASTConditionBuilder conditionBuilder = new ASTConditionBuilder();
          conditionBuilder.create();

          conditionBuilder
              .createLeftOperandAsFunction("FIRST")
              .addPropertyParameter("portfolio", "shares");

          return conditionBuilder.getModel();
        });
  }

  private static Stream<Arguments> schould_generate_first_10_elements_function() {
    return Stream.of(
        Arguments.of("java", "(huml.FIRST(model.getPortfolio().getShares(), 10.0))"),
        Arguments.of("csharp", "(huml.FIRST(model.Portfolio.Shares, 10.0))"),
        // Arguments.of("javascript", "huml.SUM_OF(huml.GET_ARRAY_OF(model.Portfolio.Shares, s =>
        // s.Percentage))")
        Arguments.of("python", "(huml.FIRST(model.portfolio.shares, 10.0))"));
  }

  @ParameterizedTest(name = GTE.PARAM_TEST_NAME)
  @MethodSource()
  public void schould_generate_first_10_elements_function(String language, String expected)
      throws Exception {
    GTE.execute(
        expected,
        language,
        p -> {
          ASTConditionBuilder conditionBuilder = new ASTConditionBuilder();
          conditionBuilder.create();

          conditionBuilder
              .createLeftOperandAsFunction("FIRST")
              .addPropertyParameter("portfolio", "shares")
              .addNumberParameter(10);

          return conditionBuilder.getModel();
        });
  }
}
