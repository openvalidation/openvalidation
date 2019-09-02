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
import io.openvalidation.common.ast.condition.ASTConditionConnector;
import io.openvalidation.common.data.DataPropertyType;
import io.openvalidation.common.data.DataSchema;
import io.openvalidation.common.data.DataVariableReference;
import io.openvalidation.common.utils.GrammarBuilder;
import org.junit.jupiter.api.Test;

class PTRuleConstrainedTransformerTest {

  @Test
  void test_constrained_rule() throws Exception {
    String input = GrammarBuilder.create().with("a").MUST().with("b").getText();

    ASTModel ast = ANTLRExecutor.run(input);

    // assert
    assertRules(ast)
        .hasSizeOf(1)
        .first()
        .condition()
        .hasOperator(ASTComparisonOperator.NOT_EQUALS)
        .leftString()
        .hasValue("a")
        .parentCondition()
        .rightString()
        .hasValue("b");
  }

  @Test
  void test_constrained_rule_with_combinator_and_comparison() throws Exception {
    String input =
        GrammarBuilder.create()
            .with("a")
            .MUST()
            .with("b")
            .AND()
            .with("c")
            .GREATER_THAN()
            .with("d")
            .getText();

    ASTModel ast = ANTLRExecutor.run(input);

    // assert
    assertRules(ast)
        .hasSizeOf(1)
        .first()
        .conditionGroup()
        .hasSize(2)
        .first()
        .hasOperator(ASTComparisonOperator.NOT_EQUALS)
        .leftString()
        .hasValue("a")
        .parentCondition()
        .rightString()
        .hasValue("b")
        .parentConditionGroup()
        .second()
        .hasConnector(ASTConditionConnector.OR)
        .hasOperator(ASTComparisonOperator.LESS_OR_EQUALS)
        .leftString()
        .hasValue("c")
        .parentCondition()
        .rightString()
        .hasValue("d");
  }

  @Test
  void test_constrained_rule_with_combinator_or_comparison() throws Exception {
    String input =
        GrammarBuilder.create()
            .with("a")
            .MUST()
            .with("b")
            .OR()
            .with("c")
            .GREATER_THAN()
            .with("d")
            .getText();

    ASTModel ast = ANTLRExecutor.run(input);

    // assert
    assertRules(ast)
        .hasSizeOf(1)
        .first()
        .conditionGroup()
        .hasSize(2)
        .first()
        .hasOperator(ASTComparisonOperator.NOT_EQUALS)
        .leftString()
        .hasValue("a")
        .parentCondition()
        .rightString()
        .hasValue("b")
        .parentConditionGroup()
        .second()
        .hasConnector(ASTConditionConnector.AND)
        .hasOperator(ASTComparisonOperator.LESS_OR_EQUALS)
        .leftString()
        .hasValue("c")
        .parentCondition()
        .rightString()
        .hasValue("d");
  }

  @Test
  void should_constrained_rule_with_combinator_and_implicit_boolean_condition() throws Exception {
    String input = GrammarBuilder.create().with("a").MUST().with("b").AND().with("hallo").getText();

    DataSchema schema = new DataSchema();
    schema.addVariable(new DataVariableReference("hallo", DataPropertyType.Boolean));
    ASTModel ast = ANTLRExecutor.run(input, schema);

    // assert
    assertRules(ast)
        .hasSizeOf(1)
        .first()
        .conditionGroup()
        .hasSize(2)
        .first()
        .hasOperator(ASTComparisonOperator.NOT_EQUALS)
        .leftOperand()
        .string()
        .hasValue("a")
        .parentCondition()
        .rightOperand()
        .string()
        .hasValue("b")
        .parentConditionGroup()
        .second()
        .hasOperator(ASTComparisonOperator.NOT_EQUALS)
        .leftVariable()
        .hasName("hallo")
        .hasType(DataPropertyType.Boolean)
        .parentCondition()
        .rightBoolean()
        .isTrue();
  }

  @Test
  void should_constrained_rule_with_combinator_and_another_combinator_and() throws Exception {
    String input =
        GrammarBuilder.create()
            .with("a")
            .MUST()
            .with("b")
            .AND()
            .with("hallo")
            .AND()
            .with("bye")
            .getText();

    DataSchema schema = new DataSchema();
    schema.addVariable(new DataVariableReference("hallo", DataPropertyType.Boolean));
    schema.addVariable(new DataVariableReference("bye", DataPropertyType.Boolean));
    ASTModel ast = ANTLRExecutor.run(input, schema);

    // assert
    assertRules(ast)
        .hasSizeOf(1)
        .first()
        .conditionGroup()
        .hasSize(3)
        .first()
        .hasOperator(ASTComparisonOperator.NOT_EQUALS)
        .leftOperand()
        .string()
        .hasValue("a")
        .parentCondition()
        .rightOperand()
        .string()
        .hasValue("b")
        .parentConditionGroup()
        .second()
        .hasConnector(ASTConditionConnector.OR)
        .hasOperator(ASTComparisonOperator.NOT_EQUALS)
        .leftVariable()
        .hasName("hallo")
        .hasType(DataPropertyType.Boolean)
        .parentCondition()
        .rightBoolean()
        .isTrue()
        .parentCondition()
        .parentConditionGroup()
        .atIndex(2)
        .hasConnector(ASTConditionConnector.OR)
        .hasOperator(ASTComparisonOperator.NOT_EQUALS)
        .leftVariable()
        .hasName("bye");
  }

  @Test
  void should_constrained_rule_with_combinator_or_another_combinator_and() throws Exception {
    String input =
        GrammarBuilder.create()
            .with("a")
            .MUST()
            .with("b")
            .OR()
            .with("hallo")
            .AND()
            .with("bye")
            .getText();

    DataSchema schema = new DataSchema();
    schema.addVariable(new DataVariableReference("hallo", DataPropertyType.Boolean));
    schema.addVariable(new DataVariableReference("bye", DataPropertyType.Boolean));
    ASTModel ast = ANTLRExecutor.run(input, schema);

    // assert
    assertRules(ast)
        .hasSizeOf(1)
        .first()
        .conditionGroup()
        .hasSize(3)
        .first()
        .hasOperator(ASTComparisonOperator.NOT_EQUALS)
        .leftOperand()
        .string()
        .hasValue("a")
        .parentCondition()
        .rightOperand()
        .string()
        .hasValue("b")
        .parentConditionGroup()
        .second()
        .hasConnector(ASTConditionConnector.AND)
        .hasOperator(ASTComparisonOperator.NOT_EQUALS)
        .leftVariable()
        .hasName("hallo")
        .hasType(DataPropertyType.Boolean)
        .parentCondition()
        .rightBoolean()
        .isTrue()
        .parentCondition()
        .parentConditionGroup()
        .atIndex(2)
        .hasConnector(ASTConditionConnector.OR)
        .hasOperator(ASTComparisonOperator.NOT_EQUALS)
        .leftVariable()
        .hasName("bye");
  }

  @Test
  void should_constrained_rule_with_combinator_or_another_combinator_or() throws Exception {
    String input =
        GrammarBuilder.create()
            .with("a")
            .MUST()
            .with("b")
            .OR()
            .with("hallo")
            .OR()
            .with("bye")
            .getText();

    DataSchema schema = new DataSchema();
    schema.addVariable(new DataVariableReference("hallo", DataPropertyType.Boolean));
    schema.addVariable(new DataVariableReference("bye", DataPropertyType.Boolean));
    ASTModel ast = ANTLRExecutor.run(input, schema);

    // assert
    assertRules(ast)
        .hasSizeOf(1)
        .first()
        .conditionGroup()
        .hasSize(3)
        .first()
        .hasOperator(ASTComparisonOperator.NOT_EQUALS)
        .leftOperand()
        .string()
        .hasValue("a")
        .parentCondition()
        .rightOperand()
        .string()
        .hasValue("b")
        .parentConditionGroup()
        .second()
        .hasConnector(ASTConditionConnector.AND)
        .hasOperator(ASTComparisonOperator.NOT_EQUALS)
        .leftVariable()
        .hasName("hallo")
        .hasType(DataPropertyType.Boolean)
        .parentCondition()
        .rightBoolean()
        .isTrue()
        .parentCondition()
        .parentConditionGroup()
        .atIndex(2)
        .hasConnector(ASTConditionConnector.AND)
        .hasOperator(ASTComparisonOperator.NOT_EQUALS)
        .leftVariable()
        .hasName("bye");
  }

  @Test
  void should_not_constrained_rule_with_combinator_and_other_combinator_or() throws Exception {
    String input =
        GrammarBuilder.create()
            .with("a")
            .MUST_NOT()
            .with("b")
            .OR()
            .with("hallo")
            .OR()
            .with("bye")
            .getText();
    // a MUST NOT b OR hello OR bye
    // => (a == b) && hello && bye

    DataSchema schema = new DataSchema();
    schema.addVariable(new DataVariableReference("hallo", DataPropertyType.Boolean));
    schema.addVariable(new DataVariableReference("bye", DataPropertyType.Boolean));
    ASTModel ast = ANTLRExecutor.run(input, schema);

    // assert
    assertRules(ast)
        .hasSizeOf(1)
        .first()
        .conditionGroup()
        .hasSize(3)
        .first()
        .hasOperator(ASTComparisonOperator.EQUALS)
        .leftOperand()
        .string()
        .hasValue("a")
        .parentCondition()
        .rightOperand()
        .string()
        .hasValue("b")
        .parentConditionGroup()
        .second()
        .hasConnector(ASTConditionConnector.AND)
        .hasOperator(ASTComparisonOperator.NOT_EQUALS)
        .leftVariable()
        .hasName("hallo")
        .hasType(DataPropertyType.Boolean)
        .parentCondition()
        .rightBoolean()
        .isTrue()
        .parentCondition()
        .parentConditionGroup()
        .atIndex(2)
        .hasConnector(ASTConditionConnector.AND)
        .hasOperator(ASTComparisonOperator.NOT_EQUALS)
        .leftVariable()
        .hasName("bye");
  }

  @Test
  void your_age_must_not_be_less_than_18_or_your_name_must_be_tim() throws Exception {
    String input =
        GrammarBuilder.create()
            .with("your age")
            .MUST_NOT()
            .with("be")
            .LESS_THAN()
            .with("18 years old")
            .OR()
            .with("your name")
            .MUST()
            .with("be tim")
            .getText();

    DataSchema schema = new DataSchema();
    schema.addVariable(new DataVariableReference("name", DataPropertyType.String));
    schema.addVariable(new DataVariableReference("age", DataPropertyType.Decimal));
    ASTModel ast = ANTLRExecutor.run(input, schema);

    // assert
    assertRules(ast)
        .hasSizeOf(1)
        .first()
        .conditionGroup()
        .hasSize(2)
        .first()
        .hasOperator(ASTComparisonOperator.LESS_THAN)
        .leftOperand()
        .variable("age")
        .parentCondition()
        .rightNumber()
        .hasValue(18.0)
        .parentCondition()
        .parentConditionGroup()
        .second()
        .hasConnector(ASTConditionConnector.AND)
        .leftVariable()
        .hasName("name")
        .parentCondition()
        .hasOperator(ASTComparisonOperator.NOT_EQUALS)
        .rightString()
        .hasValue("tim");
  }

  @Test
  void your_age_must_be_at_least_18_years_old_and_your_location_must_be_dortmund()
      throws Exception {
    String input =
        GrammarBuilder.create()
            .with("your age")
            .MUST()
            .with("be")
            .GREATER_OR_EQUALS("AT_LEAST", "")
            .with("18 years old")
            .AND()
            .with("your location")
            .MUST()
            .with("be Dortmund")
            .getText();

    DataSchema schema = new DataSchema();
    schema.addVariable(new DataVariableReference("location", DataPropertyType.String));
    schema.addVariable(new DataVariableReference("age", DataPropertyType.Decimal));
    ASTModel ast = ANTLRExecutor.run(input, schema);

    // assert
    assertRules(ast)
        .hasSizeOf(1)
        .first()
        .conditionGroup()
        .hasSize(2)
        .first()
        .hasOperator(ASTComparisonOperator.LESS_THAN)
        .leftOperand()
        .variable("age")
        .parentCondition()
        .rightNumber()
        .hasValue(18.0)
        .parentCondition()
        .parentConditionGroup()
        .second()
        .hasConnector(ASTConditionConnector.OR)
        .leftVariable()
        .hasName("location")
        .parentCondition()
        .hasOperator(ASTComparisonOperator.NOT_EQUALS)
        .rightString()
        .hasValue("Dortmund");
  }

  @Test
  void constrained_rule_with_schema_attribute() throws Exception {
    String input =
        GrammarBuilder.create()
            .with("the age of applicant")
            .MUST()
            .with("be")
            .GREATER_THAN("GREATER than", "18")
            .getText();

    //        String expectedError = input.replaceAll("ʬconstraintʬmust", "MUSS"); //TODO: reverse
    // Alias resolution should be implemented!!!
    //                                    .replaceAll(Constants.MUST_TOKEN, "MUST")
    //                                    .replaceAll(Constants.GREATER_THAN_TOKEN, "GREATER THAN");

    String schema = "{age: 25}";

    ANTLRRunner.run(
        input,
        schema,
        r -> {
          r.rules()
              .hasSizeOf(1)
              .first()
              .condition()
              .hasOperator(ASTComparisonOperator.LESS_OR_EQUALS) // negated comparison operator!!!
              .leftProperty()
              .hasPath("age")
              .hasType(DataPropertyType.Decimal)
              .parentCondition()
              .rightNumber()
              .hasValue(18d)
              .parentRule()
              .hasError("the age of applicant MUST be GREATER than 18");
        });
  }

  @Test
  void constrained_rule_with_implicit_equals_attribute() throws Exception {
    String input =
        GrammarBuilder.create()
            .with("der Ort des Bewerbers")
            .MUST("MUSS")
            .with("Dortmund sein")
            .getText();

    String schema = "{Ort: 25}";

    ANTLRRunner.run(
        input,
        schema,
        r -> {
          r.rules()
              .hasSizeOf(1)
              .first()
              .hasError("der Ort des Bewerbers MUSS Dortmund sein")
              .condition()
              .hasOperator(ASTComparisonOperator.NOT_EQUALS) // negated comparison operator!!!
              .leftProperty()
              .hasPath("Ort")
              .parentCondition()
              .rightString()
              .hasValue("Dortmund");
        });
  }

  @Test
  void constrained_rule_with_implicit_boolean_property() throws Exception {
    String input =
        GrammarBuilder.create().with("the applicant").MUST().with("be student").getText();

    String schema = "{student: false}";

    ANTLRRunner.run(
        input,
        schema,
        r -> {
          r.rules()
              .hasSizeOf(1)
              .first()
              .hasError("the applicant MUST be student")
              .condition()
              .hasOperator(ASTComparisonOperator.NOT_EQUALS) // negated comparison operator!!!
              .leftProperty()
              .hasPath("student")
              .parentCondition()
              .rightBoolean()
              .isTrue();
        });
  }

  @Test
  void test_case_from_documentation() throws Exception {
    // assemble
    String input =
        GrammarBuilder.create()
            .with("the location of the applicant")
            .MUST()
            .with("be Dortmund")
            .getText();

    String schema = "{location: Lisbon}";

    ANTLRRunner.run(
        input,
        schema,
        r -> {
          r.rules()
              .hasSizeOf(1)
              .first()
              .condition()
              .hasOperator(ASTComparisonOperator.NOT_EQUALS)
              .leftProperty()
              .hasType(DataPropertyType.String)
              .hasPath("location")
              .parentCondition()
              .rightString()
              .hasValue("Dortmund");
        });
  }
}
