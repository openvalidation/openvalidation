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

import io.openvalidation.common.ast.ASTActionError;
import io.openvalidation.common.ast.ASTComparisonOperator;
import io.openvalidation.common.ast.ASTRule;
import io.openvalidation.common.ast.builder.ASTBuilder;
import io.openvalidation.common.ast.condition.ASTCondition;
import io.openvalidation.common.ast.operand.property.ASTOperandProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class GeneratorNullChecksAsPreConditionTest {

  private static Stream<Arguments> should_contains_nullcheck_rules() {
    return ExpectationBuilder.newExpectation()
        .javascriptResult("huml.EMPTY(model.user)")
        .javaResult("huml.EMPTY(model.getUser())")
        .csharpResult("huml.EMPTY(model.User)")
        .toStream();
  }

  @ParameterizedTest(name = GTE.PARAM_TEST_NAME)
  @MethodSource()
  public void should_contains_nullcheck_rules(String language, String expected) throws Exception {
    GTE.executeAssertContains(
        expected,
        language,
        p -> {
          ASTBuilder builder = new ASTBuilder();
          builder
              .createModel()
              .createRule()
              .createCondition()
              .withLeftOperandAsProperty("user", "name")
              .withOperator(ASTComparisonOperator.EQUALS)
              .withRightOperandAsString("Alex");

          ASTRule rule = new ASTRule();
          ASTOperandProperty opleft =
              ((ASTOperandProperty)
                      ((ASTCondition) builder.getFirstRule().getCondition()).getLeftOperand())
                  .getAllParentProperties()
                  .get(0);

          ASTCondition condition = new ASTCondition();
          condition.setLeftOperand(opleft);
          condition.setOperator(ASTComparisonOperator.EMPTY);
          rule.setCondition(condition);
          rule.setAction(
              new ASTActionError(
                  "field: '" + opleft.getPathAsString() + "' should not be null or empty"));

          List<ASTRule> nullCheckRules = new ArrayList<>();
          nullCheckRules.add(rule);
          builder.getModel().setNullCheckRules(nullCheckRules);

          return builder.getModel();
        });
  }
}
