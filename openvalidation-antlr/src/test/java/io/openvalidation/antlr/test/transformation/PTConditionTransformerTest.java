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

import io.openvalidation.antlr.ANTLRExecutor;
import io.openvalidation.antlr.test.ANTLRRunner;
import io.openvalidation.common.ast.ASTComparisonOperator;
import io.openvalidation.common.ast.ASTModel;
import io.openvalidation.common.data.DataPropertyType;
import io.openvalidation.common.data.DataSchema;
import io.openvalidation.common.utils.GrammarBuilder;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class PTConditionTransformerTest {
  // singleRule ASTCondition tests, testing the initial (first) condition
  @Test
  void should_create_simple_equals() throws Exception {
    // assemble
    String input = GrammarBuilder.createRule().with("A").EQ().with("B").getText();

    ASTModel astActual = ANTLRExecutor.run(input);

    assertRules(astActual)
        .hasSizeOf(1)
        .first()
        .condition()
        .hasOperator(ASTComparisonOperator.EQUALS)
        .leftString()
        .hasValue("A")
        .parentCondition()
        .rightString()
        .hasValue("B");
  }

  @Test
  void should_create_simple_equals_with_numbers() throws Exception {
    // assemble
    String input = GrammarBuilder.createRule().with(123).EQ().with(456).getText();

    ASTModel astActual = ANTLRExecutor.run(input);

    assertRules(astActual)
        .hasSizeOf(1)
        .first()
        .condition()
        .hasOperator(ASTComparisonOperator.EQUALS)
        .leftNumber()
        .hasValue(123d)
        .parentCondition()
        .rightNumber()
        .hasValue(456d);
  }

  // todo should this work?
  @Disabled
  @Test
  void should_create_simple_equals_with_numbers_and_sugar() throws Exception {
    // assemble
    String input =
        GrammarBuilder.createRule().with("Die Zahl 123 ist").EQ().with("der Zahl 456").getText();

    ASTModel astActual = ANTLRExecutor.run(input);

    assertRules(astActual)
        .hasSizeOf(1)
        .first()
        .condition()
        .hasOperator(ASTComparisonOperator.EQUALS)
        .leftNumber()
        .hasValue(123d)
        .parentCondition()
        .rightNumber()
        .hasValue(456d);
  }

  @Test
  void should_create_simple_equals_with_accessors_on_properties() throws Exception {
    // assemble
    String input =
        GrammarBuilder.createRule().with("Person.Alter").EQ().with("Person.Geburtsjahr").getText();

    String schema = "{Person: {Alter: 25, Geburtsjahr: 1994}}";

    // assert
    ANTLRRunner.run(
        input,
        schema,
        r -> {
          r.rules()
              .hasSizeOf(1)
              .first()
              .condition()
              .hasOperator(ASTComparisonOperator.EQUALS)
              .leftProperty()
              .hasType(DataPropertyType.Decimal)
              .hasPath("Person.Alter")
              .hasPreprocessedSource("Person.Alter")
              .parentCondition()
              .rightProperty()
              .hasType(DataPropertyType.Decimal)
              .hasPath("Person.Geburtsjahr")
              .hasPreprocessedSource("Person.Geburtsjahr");
        });
  }

  @Test
  void should_create_implicit_equals() throws Exception {
    // assemble
    String input = GrammarBuilder.createRule().with("isTrue").getText();

    String schema = "{isTrue: false}";

    // assert
    ANTLRRunner.run(
        input,
        schema,
        r -> {
          r.rules()
              .hasSizeOf(1)
              .first()
              .condition()
              .leftProperty()
              .hasPath("isTrue")
              .hasType(DataPropertyType.Boolean)
              .parentCondition()
              .rightBoolean()
              .isTrue();
        });
  }

  @Test
  void should_create_implicit_not_equals() throws Exception {
    // assemble
    String input = GrammarBuilder.createRule().NEQ("the applicant ", "be senior").getText();

    String schema = "{senior: true}";

    // assert
    ANTLRRunner.run(
        input,
        schema,
        r -> {
          r.rules()
              .hasSizeOf(1)
              .first()
              .condition()
              .leftBoolean()
              .isTrue()
              .parentCondition()
              .rightProperty()
              .hasPath("senior")
              .hasType(DataPropertyType.Boolean);
        });
  }

  @Test
  void should_create_implicit_equals_with_2_number_operand_on_the_left_side() throws Exception {
    // assemble
    String input =
        GrammarBuilder.createRule()
            .with("die Leistung des Motors 500 kw")
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

  @Test
  void should_create_exists_with_left_operand_property_and_without_right_operand()
      throws Exception {
    // assemble
    String input = GrammarBuilder.createRule().with("Das Person.Alter soll").EXISTS().getText();

    String schema = "{Person: {Alter: 25}}";

    // assert
    ANTLRRunner.run(
        input,
        schema,
        r -> {
          r.rules()
              .hasSizeOf(1)
              .first()
              .condition()
              .hasOperator(ASTComparisonOperator.EXISTS)
              .leftProperty()
              .parentCondition()
              .hasNoRightOperand();
        });
  }

  @Test
  void should_create_exists_with_left_operand_variable_and_without_right_operand()
      throws Exception {
    // assemble
    String input = GrammarBuilder.createRule().with("Das Alter soll").EXISTS().getText();

    DataSchema schema = new DataSchema();
    schema.addVariable("Alter", DataPropertyType.Decimal);

    ASTModel astActual = ANTLRExecutor.run(input, schema);

    assertRules(astActual)
        .hasSizeOf(1)
        .first()
        .condition()
        .hasOperator(ASTComparisonOperator.EXISTS)
        .leftVariable()
        .parentCondition()
        .hasNoRightOperand();
  }

  @Test
  void should_create_not_exists_with_left_operand_property_and_without_right_operand()
      throws Exception {
    // assemble
    String input = GrammarBuilder.createRule().with("Das Person.Alter soll").NOT_EXISTS().getText();

    String schema = "{Person: {Alter: 25}}";

    // assert
    ANTLRRunner.run(
        input,
        schema,
        r -> {
          r.rules()
              .hasSizeOf(1)
              .first()
              .condition()
              .hasOperator(ASTComparisonOperator.NOT_EXISTS)
              .leftProperty()
              .parentCondition()
              .hasNoRightOperand();
        });
  }

  @Test
  void should_create_not_exists_with_left_operand_variable_and_without_right_operand()
      throws Exception {
    // assemble
    String input = GrammarBuilder.createRule().with("Das Alter soll").NOT_EXISTS().getText();

    DataSchema schema = new DataSchema();
    schema.addVariable("Alter", DataPropertyType.Decimal);

    ASTModel astActual = ANTLRExecutor.run(input, schema);

    assertRules(astActual)
        .hasSizeOf(1)
        .first()
        .condition()
        .hasOperator(ASTComparisonOperator.NOT_EXISTS)
        .leftVariable()
        .parentCondition()
        .hasNoRightOperand();
  }
}
