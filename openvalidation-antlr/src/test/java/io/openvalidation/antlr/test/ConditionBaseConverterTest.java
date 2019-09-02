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

package io.openvalidation.antlr.test;

import static io.openvalidation.antlr.test.util.ConditionBaseConverter.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import io.openvalidation.common.ast.builder.ASTConditionBuilder;
import io.openvalidation.common.ast.builder.ASTConditionGroupBuilder;
import io.openvalidation.common.ast.builder.ASTRuleBuilder;
import io.openvalidation.common.ast.condition.ASTCondition;
import io.openvalidation.common.ast.condition.ASTConditionConnector;
import io.openvalidation.common.ast.operand.ASTOperandStaticString;
import org.junit.jupiter.api.Test;

public class ConditionBaseConverterTest {
  // testing toBracedExpressionString
  @Test
  void test_toBracedExpressionString_with_ASTCondition_without_logicalConnectionKeyword() {
    // Assemble
    ASTCondition inputCondition = new ASTCondition();
    inputCondition.setLeftOperand(new ASTOperandStaticString("A"));
    String expected = "A";
    // Act
    String actual = toBracedExpressionString(inputCondition);
    // Assert
    assertThat(actual, equalTo(expected));
  }

  @Test
  void test_toBracedExpressionString_with_ASTCondition_with_logicalConnectionKeyword() {
    // Assemble
    ASTConditionBuilder builder = new ASTConditionBuilder();
    builder.create().withLeftOperandAsString("A").withConnector(ASTConditionConnector.AND);

    String expected = "&& A";
    // Act
    String actual = toBracedExpressionString(builder.getModel());
    // Assert
    assertThat(actual, equalTo(expected));
  }

  @Test
  void
      test_toBracedExpressionString_with_ASTConditionGroup_with_1_Condition_without_LogicalConnectionKeyword() {
    // Assemble
    ASTConditionGroupBuilder builder = new ASTConditionGroupBuilder(new ASTRuleBuilder());
    builder.create().appendCondition(null).withLeftOperandAsString("A");

    String expected = "( A )";
    // Act
    String actual = toBracedExpressionString(builder.getModel());
    // Assert
    assertThat(actual, equalTo(expected));
  }

  @Test
  void
      test_toBracedExpressionString_with_ASTConditionGroup_with_1_Condition_with_LogicalConnectionKeyword() {
    // Assemble
    ASTConditionGroupBuilder builder = new ASTConditionGroupBuilder(new ASTRuleBuilder());
    builder.create().appendCondition(ASTConditionConnector.AND).withLeftOperandAsString("A");

    String expected = "( && A )";
    // Act
    String actual = toBracedExpressionString(builder.getModel());
    // Assert
    assertThat(actual, equalTo(expected));
  }

  @Test
  void
      test_toBracedExpressionString_with_ASTConditionGroup_with_2_Conditions_with_LogicalConnectionKeyword() {
    // Assemble
    ASTConditionGroupBuilder builder = new ASTConditionGroupBuilder(new ASTRuleBuilder());
    builder
        .create()
        .appendCondition(null)
        .withLeftOperandAsString("A")
        .parentGroup()
        .appendCondition(ASTConditionConnector.OR)
        .withLeftOperandAsString("B");

    String expected = "( A || B )";
    // Act
    String actual = toBracedExpressionString(builder.getModel());
    // Assert
    assertThat(actual, equalTo(expected));
  }

  @Test
  void test_toBracedExpressionString_with_ASTConditionGroup_nested() {
    // Assemble
    ASTConditionGroupBuilder builder = new ASTConditionGroupBuilder(new ASTRuleBuilder());
    builder
        .create()
        .appendCondition(null)
        .withLeftOperandAsString("A")
        .parentGroup()
        .appendConditionGroup(ASTConditionConnector.AND)
        .appendCondition(null)
        .withLeftOperandAsString("B")
        .parentGroup()
        .appendCondition(ASTConditionConnector.OR)
        .withLeftOperandAsString("C");

    String expected = "( A && ( B || C ) )";
    // Act
    String actual = toBracedExpressionString(builder.getModel());
    // Assert
    assertThat(actual, equalTo(expected));
  }
}
