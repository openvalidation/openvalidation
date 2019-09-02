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

import io.openvalidation.common.ast.ASTComparisonOperator;
import io.openvalidation.common.ast.builder.ASTBuilder;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class GeneratorNullChecksInConditionTest {

  private static Stream<Arguments> condition_should_contains_simple_property_accessor() {
    return ExpectationBuilder.newExpectation()
        .javascriptResult("huml.EQUALS(model.name, \"Alex\")")
        .javaResult("huml.EQUALS(model.getName(), \"Alex\")")
        .csharpResult("huml.EQUALS(model.Name, \"Alex\")")
        .toStream();
  }

  @ParameterizedTest(name = GTE.PARAM_TEST_NAME)
  @MethodSource()
  public void condition_should_contains_simple_property_accessor(String language, String expected)
      throws Exception {
    GTE.executeAssertContains(
        expected,
        language,
        p -> {
          ASTBuilder builder = new ASTBuilder();
          builder
              .createModel()
              .createRule()
              .createCondition()
              .withLeftOperandAsProperty("name")
              .withOperator(ASTComparisonOperator.EQUALS)
              .withRightOperandAsString("Alex");

          return builder.getModel().getRules().get(0);
        });
  }

  private static Stream<Arguments> condition_should_contains_NULL_PRECHECKS_property_accessor() {
    return ExpectationBuilder.newExpectation()
        .javascriptResult("((model.address) && (huml.EQUALS(model.address.name, \"Alex\")))")
        .javaResult(
            "((model.getAddress() != null) && (huml.EQUALS(model.getAddress().getName(), \"Alex\")))")
        .csharpResult("((model.Address != null) && (huml.EQUALS(model.Address.Name, \"Alex\")))")
        .toStream();
  }

  @ParameterizedTest(name = GTE.PARAM_TEST_NAME)
  @MethodSource()
  public void condition_should_contains_NULL_PRECHECKS_property_accessor(
      String language, String expected) throws Exception {
    GTE.executeAssertContains(
        expected,
        language,
        p -> {
          ASTBuilder builder = new ASTBuilder();
          builder
              .createModel()
              .createRule()
              .createCondition()
              .withLeftOperandAsProperty("address", "name")
              .withOperator(ASTComparisonOperator.EQUALS)
              .withRightOperandAsString("Alex");

          return builder.getModel().getRules().get(0);
        });
  }

  private static Stream<Arguments>
      condition_should_contains_NULL_PRECHECKS_recursive_property_accessor() {
    return ExpectationBuilder.newExpectation()
        .javascriptResult(
            "((model.order && model.order.address) && (huml.EQUALS(model.order.address.name, \"Alex\")))")
        .javaResult(
            "((model.getOrder() != null && model.getOrder().getAddress() != null) && (huml.EQUALS(model.getOrder().getAddress().getName(), \"Alex\")))")
        .csharpResult(
            "((model.Order != null && model.Order.Address != null) && (huml.EQUALS(model.Order.Address.Name, \"Alex\")))")
        .toStream();
  }

  @ParameterizedTest(name = GTE.PARAM_TEST_NAME)
  @MethodSource()
  public void condition_should_contains_NULL_PRECHECKS_recursive_property_accessor(
      String language, String expected) throws Exception {
    GTE.executeAssertContains(
        expected,
        language,
        p -> {
          ASTBuilder builder = new ASTBuilder();
          builder
              .createModel()
              .createRule()
              .createCondition()
              .withLeftOperandAsProperty("order", "address", "name")
              .withOperator(ASTComparisonOperator.EQUALS)
              .withRightOperandAsString("Alex");

          return builder.getModel().getRules().get(0);
        });
  }
}
