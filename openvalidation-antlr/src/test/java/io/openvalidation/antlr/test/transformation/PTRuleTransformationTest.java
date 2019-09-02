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

package io.openvalidation.antlr.test.transformation;

import static io.openvalidation.common.unittesting.astassertion.ModelRootAssertion.assertRules;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

import io.openvalidation.antlr.ANTLRExecutor;
import io.openvalidation.antlr.test.ANTLRRunner;
import io.openvalidation.common.ast.ASTComparisonOperator;
import io.openvalidation.common.ast.ASTModel;
import io.openvalidation.common.ast.ASTRule;
import io.openvalidation.common.ast.condition.ASTConditionConnector;
import io.openvalidation.common.data.DataPropertyType;
import io.openvalidation.common.data.DataSchema;
import io.openvalidation.common.data.DataVariableReference;
import io.openvalidation.common.utils.GrammarBuilder;
import org.junit.jupiter.api.Test;

class PTRuleTransformationTest {
  @Test
  void create_simple_rule_with_single_condition() throws Exception {
    // assemble
    String input = GrammarBuilder.createRule().EQ("a", "b").THEN("error").getText();

    // act
    ASTModel actualAst = ANTLRExecutor.run(input, null);

    assertRules(actualAst).hasSizeOf(1).first().hasPreprocessedSource(input).hasError("error");
  }

  @Test
  void create_simple_rule_with_condition_group() throws Exception {
    // assemble
    String input =
        GrammarBuilder.createRule()
            .EQ("a", "b")
            .indentationDepth(4, ASTConditionConnector.OR)
            .EQ("c", "d")
            .AND()
            .EQ("e", "f")
            .THEN("error")
            .getText();

    // act
    ASTModel actualAst = ANTLRExecutor.run(input, null);

    assertRules(actualAst).hasSizeOf(1).first().hasPreprocessedSource(input).hasError("error");
  }

  @Test
  void create_simple_rule_with_arithmetic() throws Exception {
    // assemble
    String input = GrammarBuilder.createRule().with(3).ADD(7).EQ().with(10).THEN("error").getText();

    // act
    ASTModel actualAst = ANTLRExecutor.run(input, null);

    assertRules(actualAst).hasSizeOf(1).first().hasPreprocessedSource(input).hasError("error");
  }

  @Test
  void create_simple_rule_with_known_boolean_variable() throws Exception {
    // assemble
    String input = GrammarBuilder.createRule().with("variable").THEN("error").getText();

    DataSchema schema = new DataSchema();
    schema.addVariable(new DataVariableReference("variable", DataPropertyType.Boolean));
    // act
    ASTModel actualAst = ANTLRExecutor.run(input, schema);

    assertRules(actualAst).hasSizeOf(1).first().hasPreprocessedSource(input).hasError("error");
  }

  @Test
  void create_simple_rule_with_known_boolean_property() throws Exception {
    // assemble
    String input = GrammarBuilder.createRule().with("Prop").THEN("error").getText();

    String schema = "{Prop: 25}";

    // assert
    ANTLRRunner.run(
        input,
        schema,
        r -> {
          r.rules().hasSizeOf(1).first().hasPreprocessedSource(input).hasError("error");
        });
  }

  @Test
  void create_simple_rule_with_name_as_error() {
    // assemble
    String input = GrammarBuilder.createRule().EQ("a", "b").THEN("error").AS("name").getText();

    // act
    ASTModel actualAst = ANTLRExecutor.run(input);

    assertRules(actualAst).hasSizeOf(1).first().hasError("error as name");

    // TODO: Bei assertRules fehlt hasName
    // Laut Jan sollen voraussichtlich keine benannten Regeln mehr unterstützt werden.
    ASTRule rule = actualAst.getRules().get(0);
    assertThat(rule, notNullValue());
    assertThat(rule.getName(), nullValue());
  }

  @Test
  void create_rule_with_condition_only() throws Exception {
    // assemble
    String input = GrammarBuilder.createRule().EQ("a", "b").getText();

    // act
    ASTModel actualAst = ANTLRExecutor.run(input, null);

    assertRules(actualAst)
        .hasSizeOf(1)
        .first()
        .condition()
        .hasOperator(ASTComparisonOperator.EQUALS)
        .leftOperand()
        .string("a")
        .parentCondition()
        .rightOperand()
        .string("b");
  }

  @Test
  void number_of_rules_in_ast_expected_2() throws Exception {
    // assemble
    String input = GrammarBuilder.createRule().EQ("a", "b").PARAGRAPH().IF().EQ("c", "d").getText();

    // act
    ASTModel actualAst = ANTLRExecutor.run(input);

    assertRules(actualAst)
        .hasSizeOf(2)
        .first()
        .condition()
        .hasOperator(ASTComparisonOperator.EQUALS)
        .leftOperand()
        .string()
        .hasValue("a")
        .parentCondition()
        .rightOperand()
        .string()
        .hasValue("b")
        .parentModel()
        .rules()
        .second()
        .condition()
        .hasOperator(ASTComparisonOperator.EQUALS)
        .leftOperand()
        .string()
        .hasValue("c")
        .parentCondition()
        .rightOperand()
        .string()
        .hasValue("d");
  }

  @Test
  void should_resolve_implicit_bool_rule() throws Exception {
    // assemble
    String input =
        GrammarBuilder.create()
            .with("Alter")
            .LESS_THAN()
            .with(18)
            .AS("Senior")
            .PARAGRAPH()
            .IF()
            .with("der Bewerber")
            .NEQ()
            .with("Senior ist")
            .THEN("Sie sind noch kein Senior!")
            .toString();

    String schema = "{Alter: 25}";

    // assert
    ANTLRRunner.run(
        input,
        schema,
        r -> {
          r.rules()
              .hasSizeOf(1)
              .first()
              .condition()
              .leftVariable()
              .hasName("Senior")
              .hasType(DataPropertyType.Boolean)
              .parentCondition()
              .rightBoolean()
              .isTrue();
        });
  }
}
