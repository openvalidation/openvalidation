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

import io.openvalidation.antlr.test.ANTLRRunner;
import io.openvalidation.common.ast.ASTComparisonOperator;
import io.openvalidation.common.ast.condition.ASTConditionConnector;
import io.openvalidation.common.data.DataPropertyType;
import io.openvalidation.common.utils.GrammarBuilder;
import org.junit.jupiter.api.Test;

class PTConditionConstrainedTransformerTest {

  @Test
  void test_constrained_rule_with_variable() throws Exception {
    String input =
        GrammarBuilder.create()
            .with("Dortmund")
            .AS()
            .with("city")
            .PARAGRAPH()
            .with("the location")
            .MUST()
            .with("be city")
            .getText();

    String schema = "{location: Paris}";

    // assert
    ANTLRRunner.run(
        input,
        schema,
        r -> {
          r.rules()
              .hasSizeOf(1)
              .first()
              .condition()
              .hasPreprocessedSource()
              .hasOperator(ASTComparisonOperator.NOT_EQUALS)
              .leftProperty()
              .hasPath("location")
              .parentCondition()
              .rightVariable()
              .hasName("city");
        });
  }

  @Test
  void test_constrained_rule_with_variable_with_spaces() throws Exception {
    String input =
        GrammarBuilder.create()
            .with("Dortmund")
            .AS()
            .with("city of dreams")
            .PARAGRAPH()
            .with("the location")
            .MUST()
            .with("be city of dreams")
            .getText();

    String schema = "{location: Paris}";

    // assert
    ANTLRRunner.run(
        input,
        schema,
        r -> {
          r.rules()
              .hasSizeOf(1)
              .first()
              .condition()
              .hasPreprocessedSource()
              .hasOperator(ASTComparisonOperator.NOT_EQUALS)
              .leftProperty()
              .hasPath("location")
              .parentCondition()
              .rightVariable()
              .hasName("city of dreams");
        });
  }

  @Test
  void test_constrained_rule_with_implicit_string_comparison_double_negation() throws Exception {
    String input = GrammarBuilder.create().with("status").MUST_NOT().with("be new").getText();

    String schema = "{status: deprecated}";

    // assert
    ANTLRRunner.run(
        input,
        schema,
        r -> {
          r.rules()
              .hasSizeOf(1)
              .first()
              .condition()
              .hasPreprocessedSource()
              .hasOperator(ASTComparisonOperator.EQUALS)
              .leftProperty()
              .hasPath("status")
              .parentCondition()
              .rightString()
              .hasValue("new");
        });
  }

  @Test
  void constrained_with_and_combinator_should_be_converted_to_or() throws Exception {
    String input =
        GrammarBuilder.create()
            .with("status")
            .MUST()
            .with("be new")
            .AND()
            .with("status2")
            .MUST_NOT()
            .with("be old")
            .getText();

    String schema = "{status: new, status2: old}";

    // assert
    ANTLRRunner.run(
        input,
        schema,
        r -> {
          r.rules()
              .hasSizeOf(1)
              .first()
              .conditionGroup()
              .first()
              .hasPreprocessedSource()
              .hasOperator(ASTComparisonOperator.NOT_EQUALS)
              .leftProperty()
              .hasPath("status")
              .parentCondition()
              .rightString()
              .hasValue("new")
              .parentConditionGroup()
              .second()
              .hasPreprocessedSource()
              .hasOperator(ASTComparisonOperator.EQUALS)
              .leftProperty()
              .hasPath("status2")
              .parentCondition()
              .rightString()
              .hasValue("old")
              .parentCondition()
              .hasConnector(ASTConditionConnector.OR);
        });
  }

  @Test
  void should_create_implicit_equals_with_2_number_operand_on_the_left_side() throws Exception {
    // assemble
    String input =
        GrammarBuilder.create()
            .with("die Leistung des Motors")
            .MUST_NOT()
            .with("500 kw")
            .GREATER_THAN("UEBERSTEIGT", "")
            .getText();

    String schema = "{Leistung: 100}";

    // assert
    ANTLRRunner.run(
        input,
        schema,
        r -> {
          r.rules()
              .hasSizeOf(1)
              .first()
              .condition()
              .hasOperator(ASTComparisonOperator.GREATER_THAN)
              .leftProperty()
              .hasPath("Leistung")
              .hasType(DataPropertyType.Decimal)
              .parentCondition()
              .rightNumber()
              .hasValue(500.0);
        });
  }
}
