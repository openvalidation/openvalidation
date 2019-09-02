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
import static io.openvalidation.common.unittesting.astassertion.ModelRootAssertion.assertVariable;

import io.openvalidation.antlr.ANTLRExecutor;
import io.openvalidation.antlr.test.ANTLRRunner;
import io.openvalidation.common.ast.ASTComparisonOperator;
import io.openvalidation.common.ast.ASTModel;
import io.openvalidation.common.ast.condition.ASTConditionConnector;
import io.openvalidation.common.data.DataPropertyType;
import io.openvalidation.common.utils.GrammarBuilder;
import org.junit.jupiter.api.Test;

class PTConditionGroupTransformerTest {
  // ASTConditionGroup tests
  @Test
  void should_create_simple_condition_group_with_2_conditions() throws Exception {
    // assemble
    String input =
        GrammarBuilder.createRule()
            .with("A")
            .EQ()
            .with("B")
            .AND()
            .with("C")
            .EQ()
            .with("D")
            .getText();

    // run
    ASTModel astActual = ANTLRExecutor.run(input);

    assertRules(astActual)
        .hasSizeOf(1)
        .first()
        .conditionGroup()
        .hasPreprocessedSource()
        .hasSize(2)
        .first()
        .hasIndentationLevel(0)
        .hasOperator(ASTComparisonOperator.EQUALS)
        .leftString()
        .hasValue("A")
        .parentCondition()
        .rightString()
        .hasValue("B")
        .parentConditionGroup()
        .second()
        .hasIndentationLevel(0)
        .hasConnector(ASTConditionConnector.AND)
        .hasOperator(ASTComparisonOperator.EQUALS)
        .leftString()
        .hasValue("C")
        .parentCondition()
        .rightString()
        .hasValue("D");
  }

  @Test
  void should_create_simple_condition_group_with_2_conditions_and_specific_source()
      throws Exception {
    // assemble
    String input =
        GrammarBuilder.createRule()
            .with("A")
            .EQ()
            .with("B")
            .AND()
            .with("C")
            .EQ()
            .with("D")
            .getText();

    String expectedSource = input.substring(input.indexOf("A"));

    // run
    ASTModel astActual = ANTLRExecutor.run(input);

    assertRules(astActual)
        .hasSizeOf(1)
        .first()
        .conditionGroup()
        .hasPreprocessedSource(expectedSource)
        .hasSize(2)
        .first()
        .hasIndentationLevel(0)
        .hasOperator(ASTComparisonOperator.EQUALS)
        .leftString()
        .hasValue("A")
        .parentCondition()
        .rightString()
        .hasValue("B")
        .parentConditionGroup()
        .second()
        .hasIndentationLevel(0)
        .hasConnector(ASTConditionConnector.AND)
        .hasOperator(ASTComparisonOperator.EQUALS)
        .leftString()
        .hasValue("C")
        .parentCondition()
        .rightString()
        .hasValue("D");
  }

  @Test
  void should_create_condition_group_with_implicit_condition() throws Exception {
    // assemble
    String input =
        GrammarBuilder.createRule().with("A").EQ().with("B").AND().with("isTrue").getText();

    // run
    ANTLRRunner.run(
        input,
        "{isTrue: true}",
        r -> {
          r.rules()
              .hasSizeOf(1)
              .first()
              .conditionGroup()
              .hasPreprocessedSource()
              .hasSize(2)
              .first()
              .hasIndentationLevel(0)
              .hasOperator(ASTComparisonOperator.EQUALS)
              .leftString()
              .hasValue("A")
              .parentCondition()
              .rightString()
              .hasValue("B")
              .parentConditionGroup()
              .second()
              .hasIndentationLevel(0)
              .leftProperty()
              .hasPath("isTrue")
              .hasType(DataPropertyType.Boolean)
              .parentCondition()
              .hasConnector(ASTConditionConnector.AND)
              .rightBoolean()
              .isTrue();
        });
  }

  @Test
  void rootConditionGroup_should_have_3_elements() throws Exception {
    // assemble
    String input =
        GrammarBuilder.createRule().EQ("A", "B").AND().EQ("C", "D").OR().EQ("E", "F").getText();

    // run
    ASTModel astActual = ANTLRExecutor.run(input);

    assertRules(astActual)
        .hasSizeOf(1)
        .first()
        .conditionGroup()
        .hasPreprocessedSource()
        .hasSize(3)
        .first()
        .hasIndentationLevel(0)
        .hasOperator(ASTComparisonOperator.EQUALS)
        .leftString()
        .hasValue("A")
        .parentCondition()
        .rightString()
        .hasValue("B")
        .parentConditionGroup()
        .second()
        .hasIndentationLevel(0)
        .hasConnector(ASTConditionConnector.AND)
        .hasOperator(ASTComparisonOperator.EQUALS)
        .leftString()
        .hasValue("C")
        .parentCondition()
        .rightString()
        .hasValue("D")
        .parentConditionGroup()
        .atIndex(2)
        .hasIndentationLevel(0)
        .hasConnector(ASTConditionConnector.OR)
        .hasOperator(ASTComparisonOperator.EQUALS)
        .leftString()
        .hasValue("E")
        .parentCondition()
        .rightString()
        .hasValue("F");
  }

  @Test
  void rootConditionGroup_with_2_conditions_with_second_indented() throws Exception {
    // assemble
    String input =
        GrammarBuilder.createRule()
            .EQ("A", "B")
            .indentationDepth(4, ASTConditionConnector.AND)
            .EQ("C", "D")
            .getText();

    // run
    ASTModel astActual = ANTLRExecutor.run(input);

    assertRules(astActual)
        .hasSizeOf(1)
        .first()
        .conditionGroup()
        .hasPreprocessedSource()
        .hasSize(2)
        .first()
        .hasIndentationLevel(0)
        .hasOperator(ASTComparisonOperator.EQUALS)
        .leftString()
        .hasValue("A")
        .parentCondition()
        .rightString()
        .hasValue("B")
        .parentConditionGroup()
        .second()
        .hasIndentationLevel(4)
        .hasConnector(ASTConditionConnector.AND)
        .hasOperator(ASTComparisonOperator.EQUALS)
        .leftString()
        .hasValue("C")
        .parentCondition()
        .rightString()
        .hasValue("D");
  }

  @Test
  void rootConditionGroup_should_have_a_subgroup() throws Exception {
    // assemble
    String input =
        GrammarBuilder.create()
            .EQ("A", "B")
            .AND()
            .EQ("C", "D")
            .indentationDepth(4, ASTConditionConnector.OR)
            .EQ("E", "F")
            .AS("var")
            .toString();

    //    A = B
    // AND C = D
    //    OR E = F

    // run
    ASTModel ast = ANTLRExecutor.run(input);

    // assert
    assertVariable(ast)
        .hasSizeOf(1)
        .first()
        .conditionGroup()
        .hasPreprocessedSource()
        .hasSize(2)
        .first()
        .hasOperator(ASTComparisonOperator.EQUALS)
        .leftString()
        .hasValue("A")
        .parentCondition()
        .rightString()
        .hasValue("B")
        .parentConditionGroup()
        .firstConditionGroup()
        .hasSize(2)
        .hasConnector(ASTConditionConnector.AND)
        .first()
        .hasOperator(ASTComparisonOperator.EQUALS)
        .leftString()
        .hasValue("C")
        .parentCondition()
        .rightString()
        .hasValue("D")
        .parentConditionGroup()
        .second()
        .hasConnector(ASTConditionConnector.OR)
        .hasOperator(ASTComparisonOperator.EQUALS)
        .leftString()
        .hasValue("E")
        .parentCondition()
        .rightString()
        .hasValue("F");
  }

  @Test
  void should_have_ASTConditionGroup_with_an_ASTConditionGroup() throws Exception {
    // assemble
    String input =
        GrammarBuilder.create()
            .EQ("A", "B")
            .indentationDepth(4, ASTConditionConnector.AND)
            .EQ("C", "D")
            .AS("varx")
            .toString();

    // run
    ASTModel ast = ANTLRExecutor.run(input);

    // assert
    assertVariable(ast)
        .hasSizeOf(1)
        .first()
        .conditionGroup()
        .hasPreprocessedSource()
        .hasSize(2)
        .first()
        .hasOperator(ASTComparisonOperator.EQUALS)
        .leftString()
        .hasValue("A")
        .parentCondition()
        .rightString()
        .hasValue("B")
        .parentConditionGroup()
        .second()
        .hasConnector(ASTConditionConnector.AND)
        .hasOperator(ASTComparisonOperator.EQUALS)
        .leftString()
        .hasValue("C")
        .parentCondition()
        .rightString()
        .hasValue("D");
  }

  @Test
  void test_complex_case() throws Exception {
    // assemble
    String input =
        GrammarBuilder.create()
            .EQ("A", "B")
            .indentationDepth(4, ASTConditionConnector.AND)
            .EQ("C", "D")
            .indentationDepth(8, ASTConditionConnector.OR)
            .EQ("E", "F")
            .indentationDepth(12, ASTConditionConnector.AND)
            .EQ("G", "H")
            .AND()
            .EQ("I", "J")
            .AS("varx")
            .toString();

    // run
    ASTModel ast = ANTLRExecutor.run(input);

    // assert
    assertVariable(ast)
        .hasSizeOf(1)
        .first()
        .conditionGroup()
        .hasPreprocessedSource()
        .hasSize(2)
        .firstConditionGroup()
        .hasSize(2)
        .first()
        .hasOperator(ASTComparisonOperator.EQUALS)
        .leftString()
        .hasValue("A")
        .parentCondition()
        .rightString()
        .hasValue("B")
        .parentConditionGroup()
        .firstConditionGroup()
        .hasConnector(ASTConditionConnector.AND)
        .hasSize(2)
        .first()
        .hasOperator(ASTComparisonOperator.EQUALS)
        .leftString()
        .hasValue("C")
        .parentCondition()
        .rightString()
        .hasValue("D")
        .parentConditionGroup()
        .firstConditionGroup()
        .hasSize(2)
        .hasConnector(ASTConditionConnector.OR)
        .first()
        .hasOperator(ASTComparisonOperator.EQUALS)
        .leftString()
        .hasValue("E")
        .parentCondition()
        .rightString()
        .hasValue("F")
        .parentConditionGroup()
        .second()
        .hasConnector(ASTConditionConnector.AND)
        .hasOperator(ASTComparisonOperator.EQUALS)
        .leftString()
        .hasValue("G")
        .parentCondition()
        .rightString()
        .hasValue("H")
        .parentConditionGroup()
        .parentConditionGroup()
        .parentConditionGroup()
        .parentConditionGroup()
        .first()
        .hasConnector(ASTConditionConnector.AND)
        .hasOperator(ASTComparisonOperator.EQUALS)
        .leftString()
        .hasValue("I")
        .parentCondition()
        .rightString()
        .hasValue("J");
  }

  @Test
  void deeper_indentation_than_necessary_should_have_no_effect_on_the_logical_structure()
      throws Exception {
    // assemble
    String input =
        GrammarBuilder.create()
            .EQ("A", "B")
            .indentationDepth(8, ASTConditionConnector.AND)
            .EQ("C", "D")
            .indentationDepth(16, ASTConditionConnector.OR)
            .EQ("E", "F")
            .indentationDepth(24, ASTConditionConnector.AND)
            .EQ("G", "H")
            .AND()
            .EQ("I", "J")
            .AS("varx")
            .toString();

    // run
    ASTModel ast = ANTLRExecutor.run(input);

    // assert
    assertVariable(ast)
        .hasSizeOf(1)
        .first()
        .conditionGroup()
        .hasPreprocessedSource()
        .hasSize(2)
        .firstConditionGroup()
        .hasSize(2)
        .first()
        .hasOperator(ASTComparisonOperator.EQUALS)
        .leftString()
        .hasValue("A")
        .parentCondition()
        .rightString()
        .hasValue("B")
        .parentConditionGroup()
        .firstConditionGroup()
        .hasConnector(ASTConditionConnector.AND)
        .hasSize(2)
        .first()
        .hasOperator(ASTComparisonOperator.EQUALS)
        .leftString()
        .hasValue("C")
        .parentCondition()
        .rightString()
        .hasValue("D")
        .parentConditionGroup()
        .firstConditionGroup()
        .hasSize(2)
        .hasConnector(ASTConditionConnector.OR)
        .first()
        .hasOperator(ASTComparisonOperator.EQUALS)
        .leftString()
        .hasValue("E")
        .parentCondition()
        .rightString()
        .hasValue("F")
        .parentConditionGroup()
        .second()
        .hasConnector(ASTConditionConnector.AND)
        .hasOperator(ASTComparisonOperator.EQUALS)
        .leftString()
        .hasValue("G")
        .parentCondition()
        .rightString()
        .hasValue("H")
        .parentConditionGroup()
        .parentConditionGroup()
        .parentConditionGroup()
        .parentConditionGroup()
        .first()
        .hasConnector(ASTConditionConnector.AND)
        .hasOperator(ASTComparisonOperator.EQUALS)
        .leftString()
        .hasValue("I")
        .parentCondition()
        .rightString()
        .hasValue("J");
  }

  @Test
  void conditionGroup_as_first_element_of_subGroup_Beispiel_von_Ilja() throws Exception {
    // assemble
    String input =
        GrammarBuilder.create()
            .EQ("A", "B")
            .AND()
            .EQ("C", "D")
            .indentationDepth(8, ASTConditionConnector.OR)
            .EQ("E", "F")
            .indentationDepth(8, ASTConditionConnector.AND)
            .EQ("G", "H")
            .indentationDepth(4, ASTConditionConnector.AND)
            .EQ("I", "J")
            .AS("varx")
            .toString();

    // run
    ASTModel ast = ANTLRExecutor.run(input);

    // Assert
    assertVariable(ast)
        .hasSizeOf(1)
        .first()
        .conditionGroup()
        .hasPreprocessedSource()
        .hasSize(2)
        .first()
        .parentConditionGroup()
        .firstConditionGroup()
        .hasSize(2)
        .hasConnector(ASTConditionConnector.AND)
        .firstConditionGroup()
        .hasNoConnector()
        .hasSize(3)
        .first()
        .hasNoConnector()
        .parentConditionGroup()
        .second()
        .hasConnector(ASTConditionConnector.OR)
        .parentConditionGroup()
        .atIndex(2)
        .hasConnector(ASTConditionConnector.AND)
        .parentConditionGroup()
        .parentConditionGroup()
        .first()
        .hasConnector(ASTConditionConnector.AND);
  }

  @Test
  void condition_group_formatted_like_staircase() throws Exception {
    String input =
        GrammarBuilder.createRule()
            .EQ("Domicile", "city1\n")
            .indentationDepth(17, ASTConditionConnector.AND)
            .EQ("Competence", "Genius\n")
            .indentationDepth(24, ASTConditionConnector.OR)
            .EQ("Age", "17")
            .THEN("error")
            .getText();

    ASTModel ast = ANTLRExecutor.run(input);

    assertRules(ast)
        .hasSizeOf(1)
        .first()
        .conditionGroup()
        .hasPreprocessedSource()
        .hasSize(2)
        .first()
        .leftString()
        .hasValue("Domicile")
        .parentCondition()
        .rightString()
        .hasValue("city1")
        .parentConditionGroup()
        .firstConditionGroup()
        .hasSize(2);
  }

  @Test
  void test_creation_of_2_condition_groups_of_2_subsequent_rules() throws Exception {
    String input =
        GrammarBuilder.create()
            .IF()
            .EQ("A", "B")
            .indentationDepth(4, ASTConditionConnector.AND)
            .EQ("C", "D")
            .THEN("error message")
            .PARAGRAPH()
            .IF()
            .EQ("A", "B")
            .AND()
            .EQ("C", "D")
            .indentationDepth(9, ASTConditionConnector.OR)
            .EQ("E", "F")
            .THEN("error message")
            .getText();

    ASTModel ast = ANTLRExecutor.run(input);

    assertRules(ast)
        .hasSizeOf(2)
        .first()
        .conditionGroup()
        .hasPreprocessedSource()
        .hasSize(2)
        .first()
        .hasIndentationLevel(0)
        .hasOperator(ASTComparisonOperator.EQUALS)
        .leftString()
        .hasValue("A")
        .parentCondition()
        .rightString()
        .hasValue("B")
        .parentConditionGroup()
        .second()
        .hasIndentationLevel(4)
        .hasConnector(ASTConditionConnector.AND)
        .hasOperator(ASTComparisonOperator.EQUALS)
        .leftString()
        .hasValue("C")
        .parentCondition()
        .rightString()
        .hasValue("D");

    assertRules(ast)
        .second()
        .conditionGroup()
        .hasPreprocessedSource()
        .hasSize(2)
        .first()
        .hasIndentationLevel(0)
        .hasOperator(ASTComparisonOperator.EQUALS)
        .leftString()
        .hasValue("A")
        .parentCondition()
        .rightString()
        .hasValue("B")
        .parentConditionGroup()
        .firstConditionGroup()
        .hasSize(2)
        .hasConnector(ASTConditionConnector.AND)
        .first()
        .hasIndentationLevel(0)
        .hasOperator(ASTComparisonOperator.EQUALS)
        .leftString()
        .hasValue("C")
        .parentCondition()
        .rightString()
        .hasValue("D")
        .parentConditionGroup()
        .second()
        .hasIndentationLevel(8)
        .hasConnector(ASTConditionConnector.OR)
        .hasOperator(ASTComparisonOperator.EQUALS)
        .leftString()
        .hasValue("E")
        .parentCondition()
        .rightString()
        .hasValue("F");
  }

  @Test
  void condition_group_with_4_conditions_reverse_staircase_format() throws Exception {
    String input =
        GrammarBuilder.createRule()
            .EQ("A", "A")
            .indentationDepth(12, ASTConditionConnector.AND)
            .EQ("B", "B")
            .indentationDepth(8, ASTConditionConnector.AND)
            .EQ("C", "C")
            .indentationDepth(4, ASTConditionConnector.AND)
            .EQ("D", "D")
            .THEN("error")
            .getText();

    ASTModel ast = ANTLRExecutor.run(input);

    // ( ( ( A & B ) || C ) && D )
    assertRules(ast)
        .hasSizeOf(1)
        .first()
        .conditionGroup()
        .hasPreprocessedSource()
        .hasSize(2)
        .first()
        .parentConditionGroup()
        .firstConditionGroup()
        .hasSize(2)
        .first()
        .parentConditionGroup()
        .firstConditionGroup()
        .hasSize(2)
        .first()
        .parentConditionGroup()
        .second();
  }

  @Test
  void condition_group_with_4_conditions_reverse_staircase_format2() throws Exception {
    String input =
        GrammarBuilder.createRule()
            .EQ("A", "A")
            .indentationDepth(12, ASTConditionConnector.AND)
            .EQ("B", "B")
            .indentationDepth(12, ASTConditionConnector.AND)
            .EQ("BB", "BB")
            .indentationDepth(8, ASTConditionConnector.AND)
            .EQ("C", "C")
            .indentationDepth(4, ASTConditionConnector.AND)
            .EQ("D", "D")
            .THEN("error")
            .getText();

    ASTModel ast = ANTLRExecutor.run(input);

    // ( ( ( A & B & BB ) && C ) && D )
    assertRules(ast)
        .hasSizeOf(1)
        .first()
        .conditionGroup()
        .hasPreprocessedSource()
        .hasSize(2)
        .first()
        .parentConditionGroup()
        .firstConditionGroup()
        .hasSize(2)
        .first()
        .parentConditionGroup()
        .firstConditionGroup()
        .hasSize(3)
        .first()
        .parentConditionGroup()
        .second()
        .parentConditionGroup()
        .atIndex(2);
  }

  @Test
  void test_condition_group_with_alternating_indentations() throws Exception {
    String input =
        GrammarBuilder.createRule()
            .EQ("A", "A")
            .indentationDepth(8, ASTConditionConnector.AND)
            .EQ("B", "B")
            .indentationDepth(4, ASTConditionConnector.OR)
            .EQ("C", "C")
            .indentationDepth(8, ASTConditionConnector.AND)
            .EQ("D", "D")
            .THEN("error")
            .getText();

    ASTModel ast = ANTLRExecutor.run(input);

    assertRules(ast)
        .hasSizeOf(1)
        .first()
        .conditionGroup()
        .hasPreprocessedSource()
        .hasSize(2)
        .firstConditionGroup()
        .hasSize(2)
        .hasNoConnector()
        .first()
        .hasNoConnector()
        .hasIndentationLevel(0)
        .hasOperator(ASTComparisonOperator.EQUALS)
        .parentConditionGroup()
        .second()
        .hasConnector(ASTConditionConnector.AND)
        .hasIndentationLevel(8)
        .hasOperator(ASTComparisonOperator.EQUALS)
        .parentConditionGroup()
        .parentConditionGroup()
        .secondConditionGroup()
        .hasSize(2)
        .hasConnector(ASTConditionConnector.OR)
        .first()
        .hasIndentationLevel(4)
        .hasNoConnector()
        .hasOperator(ASTComparisonOperator.EQUALS)
        .parentConditionGroup()
        .second()
        .hasConnector(ASTConditionConnector.AND)
        .hasOperator(ASTComparisonOperator.EQUALS)
        .hasIndentationLevel(8);
  }

  @Test
  void test_condition_group_with_alternating_indentations2() throws Exception {
    String input =
        GrammarBuilder.createRule()
            .EQ("A", "A")
            .indentationDepth(8, ASTConditionConnector.AND)
            .EQ("B", "B")
            .indentationDepth(12, ASTConditionConnector.OR)
            .EQ("C", "C")
            .indentationDepth(8, ASTConditionConnector.AND)
            .EQ("D", "D")
            .indentationDepth(4, ASTConditionConnector.OR)
            .EQ("E", "E")
            .THEN("error")
            .getText();

    ASTModel ast = ANTLRExecutor.run(input);

    assertRules(ast)
        .hasSizeOf(1)
        .first()
        .conditionGroup()
        .hasPreprocessedSource()
        .hasSize(2)
        .firstConditionGroup()
        .hasSize(3)
        .hasNoConnector()
        .first()
        .hasNoConnector()
        .hasIndentationLevel(0)
        .hasOperator(ASTComparisonOperator.EQUALS)
        .parentConditionGroup()
        .firstConditionGroup()
        .hasSize(2)
        .hasConnector(ASTConditionConnector.AND)
        .first()
        .hasIndentationLevel(8)
        .hasOperator(ASTComparisonOperator.EQUALS)
        .hasNoConnector()
        .parentConditionGroup()
        .second()
        .hasIndentationLevel(12)
        .hasOperator(ASTComparisonOperator.EQUALS)
        .hasConnector(ASTConditionConnector.OR)
        .parentConditionGroup()
        .parentConditionGroup()
        .second()
        .hasConnector(ASTConditionConnector.AND)
        .hasIndentationLevel(8)
        .hasOperator(ASTComparisonOperator.EQUALS)
        .parentConditionGroup()
        .parentConditionGroup()
        .first()
        .hasIndentationLevel(4)
        .hasConnector(ASTConditionConnector.OR)
        .hasOperator(ASTComparisonOperator.EQUALS);
  }
}
