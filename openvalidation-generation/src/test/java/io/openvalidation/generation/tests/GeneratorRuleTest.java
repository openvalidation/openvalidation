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
import io.openvalidation.common.ast.builder.ASTRuleBuilder;
import io.openvalidation.common.ast.builder.ASTVariableBuilder;
import io.openvalidation.common.ast.condition.ASTConditionConnector;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class GeneratorRuleTest {

  private static Stream<Arguments> simple_condition_as_rule() {
    return Stream.of(
        //                            language      expected
        Arguments.of(
            "javascript",
            "huml.appendRule(\"simple_condition_as_rule\",\n"
                + "                   [\"Eigenkapital\"],\n"
                + "                   \"Eigenkapital muss größer 10.000,00€ sein.\",\n"
                + "                   function(model) { return huml.GREATER_THAN(model.Eigenkapital, 10000.0);},\n"
                + "                   false\n"
                + "                );"),
        Arguments.of(
            "csharp",
            "huml.appendRule(\"simple_condition_as_rule\",\n"
                + "                   new String[]{ \"Eigenkapital\" },\n"
                + "                   \"Eigenkapital muss größer 10.000,00€ sein.\",\n"
                + "                   ( model) => huml.GREATER_THAN(model.Eigenkapital, 10000.0),\n"
                + "                   false\n"
                + "                );"),
        Arguments.of(
            "java",
            "huml.appendRule(\"simple_condition_as_rule\",\n"
                + "           new String[]{ \"Eigenkapital\" },\n"
                + "           \"Eigenkapital muss größer 10.000,00€ sein.\",\n"
                + "           ( model) -> huml.GREATER_THAN(model.getEigenkapital(), 10000.0),\n"
                + "           false\n"
                + "        );"));
  }

  @ParameterizedTest(name = GTE.PARAM_TEST_NAME)
  @MethodSource()
  public void simple_condition_as_rule(String language, String expected) throws Exception {
    GTE.execute(
        expected,
        language,
        p -> {
          ASTRuleBuilder builder = new ASTRuleBuilder();
          builder
              .createRule("simple_condition_as_rule", "Eigenkapital muss größer 10.000,00€ sein.")
              .createCondition()
              .withLeftOperandAsProperty("Eigenkapital")
              .withOperator(ASTComparisonOperator.GREATER_THAN)
              .withRightOperandAsNumber(10000);
          return builder.getModel();
        });
  }

  private static Stream<Arguments> simple_condition_as_arithmetic_rule() {
    return Stream.of(
        //                            language      expected
        Arguments.of(
            "javascript",
            "huml.appendRule(\"simple_condition_as_arithmetic_rule\",\n"
                + "                   [\"investment\"],\n"
                + "                   \"the investment of applicant HAS NOT EQUALS 20000 MOD 18200.\",\n"
                + "                   function(model) { return huml.EQUALS(model.investment, (20000.0%18200.0));},\n"
                + "                   false\n"
                + "                );"),
        Arguments.of(
            "csharp",
            "huml.appendRule(\"simple_condition_as_arithmetic_rule\",\n"
                + "                   new String[]{ \"investment\" },\n"
                + "                   \"the investment of applicant HAS NOT EQUALS 20000 MOD 18200.\",\n"
                + "                   ( model) => huml.EQUALS(model.Investment, (20000.0%18200.0)),\n"
                + "                   false\n"
                + "                );"),
        Arguments.of(
            "java",
            "huml.appendRule(\"simple_condition_as_arithmetic_rule\",\n"
                + "           new String[]{ \"investment\" },\n"
                + "           \"the investment of applicant HAS NOT EQUALS 20000 MOD 18200.\",\n"
                + "           ( model) -> huml.EQUALS(model.getInvestment(), (20000.0%18200.0)),\n"
                + "           false\n"
                + "        );"));
  }

  @ParameterizedTest(name = GTE.PARAM_TEST_NAME)
  @MethodSource()
  public void simple_condition_as_arithmetic_rule(String language, String expected)
      throws Exception {
    GTE.execute(
        expected,
        language,
        p -> {
          ASTRuleBuilder builder = new ASTRuleBuilder();
          builder
              .createRule(
                  "simple_condition_as_arithmetic_rule",
                  "the investment of applicant HAS NOT EQUALS 20000 MOD 18200.")
              .createCondition()
              .withLeftOperandAsProperty("investment")
              .withOperator(ASTComparisonOperator.EQUALS)
              .createRightOperandAsArithmeticalOperation()
              .withNumber(20000)
              .mod(18200);
          return builder.getModel();
        });
  }

  private static Stream<Arguments> simple_not_condition_as_arithmetic_rule() {
    return Stream.of(
        //                            language      expected
        Arguments.of(
            "javascript",
            "huml.appendRule(\"simple_not_condition_as_arithmetic_rule\",\n"
                + "                   [\"investment\"],\n"
                + "                   \"the investment of applicant MUSTN&#x27;T be GREATER OR EQUALS 1001 + 2\",\n"
                + "                   function(model) { return huml.LESS_THAN(model.investment, (1001.0+2.0));},\n"
                + "                   false\n"
                + "                );"),
        Arguments.of(
            "csharp",
            "huml.appendRule(\"simple_not_condition_as_arithmetic_rule\",\n"
                + "                   new String[]{ \"investment\" },\n"
                + "                   \"the investment of applicant MUSTN&#x27;T be GREATER OR EQUALS 1001 + 2\",\n"
                + "                   ( model) => huml.LESS_THAN(model.Investment, (1001.0+2.0)),\n"
                + "                   false\n"
                + "                );"),
        Arguments.of(
            "java",
            "huml.appendRule(\"simple_not_condition_as_arithmetic_rule\",\n"
                + "           new String[]{ \"investment\" },\n"
                + "           \"the investment of applicant MUSTN&#x27;T be GREATER OR EQUALS 1001 + 2\",\n"
                + "           ( model) -> huml.LESS_THAN(model.getInvestment(), (1001.0+2.0)),\n"
                + "           false\n"
                + "        );"));
  }

  @ParameterizedTest(name = GTE.PARAM_TEST_NAME)
  @MethodSource()
  public void simple_not_condition_as_arithmetic_rule(String language, String expected)
      throws Exception {
    GTE.execute(
        expected,
        language,
        p -> {
          ASTRuleBuilder builder = new ASTRuleBuilder();
          builder
              .createRule(
                  "simple_not_condition_as_arithmetic_rule",
                  "the investment of applicant MUSTN'T be GREATER OR EQUALS 1001 + 2")
              .createCondition()
              .withLeftOperandAsProperty("investment")
              .withOperator(ASTComparisonOperator.LESS_THAN)
              .createRightOperandAsArithmeticalOperation()
              .withNumber(1001)
              .plus(2);
          return builder.getModel();
        });
  }

  private static Stream<Arguments> simple_not_condition_as_arithmetic_rule_second() {
    return Stream.of(
        //                            language      expected
        Arguments.of(
            "javascript",
            "huml.appendRule(\"simple_condition_as_arithemtic_rule\",\n"
                + "                   [\"investment\"],\n"
                + "                   \"the investment of applicant SHOULDN&#x27;T be LESS OR EQUALS 1001 + 2\",\n"
                + "                   function(model) { return huml.GREATER_THAN(model.investment, (1001.0+2.0));},\n"
                + "                   false\n"
                + "                );"),
        Arguments.of(
            "csharp",
            "huml.appendRule(\"simple_condition_as_arithemtic_rule\",\n"
                + "                   new String[]{ \"investment\" },\n"
                + "                   \"the investment of applicant SHOULDN&#x27;T be LESS OR EQUALS 1001 + 2\",\n"
                + "                   ( model) => huml.GREATER_THAN(model.Investment, (1001.0+2.0)),\n"
                + "                   false\n"
                + "                );"),
        Arguments.of(
            "java",
            "huml.appendRule(\"simple_condition_as_arithemtic_rule\",\n"
                + "           new String[]{ \"investment\" },\n"
                + "           \"the investment of applicant SHOULDN&#x27;T be LESS OR EQUALS 1001 + 2\",\n"
                + "           ( model) -> huml.GREATER_THAN(model.getInvestment(), (1001.0+2.0)),\n"
                + "           false\n"
                + "        );"));
  }

  @ParameterizedTest(name = GTE.PARAM_TEST_NAME)
  @MethodSource()
  public void simple_not_condition_as_arithmetic_rule_second(String language, String expected)
      throws Exception {
    GTE.execute(
        expected,
        language,
        p -> {
          ASTRuleBuilder builder = new ASTRuleBuilder();
          builder
              .createRule(
                  "simple_condition_as_arithemtic_rule",
                  "the investment of applicant SHOULDN'T be LESS OR EQUALS 1001 + 2")
              .createCondition()
              .withLeftOperandAsProperty("investment")
              .withOperator(ASTComparisonOperator.GREATER_THAN)
              .createRightOperandAsArithmeticalOperation()
              .withNumber(1001)
              .plus(2);
          return builder.getModel();
        });
  }

  private static Stream<Arguments> simple_not_condition_as_arithmetic_rule_third() {
    return Stream.of(
        //                            language      expected
        Arguments.of(
            "javascript",
            "huml.appendRule(\"simple_not_condition_as_arithmetic_rule_third\",\n"
                + "                   [\"investment\"],\n"
                + "                   \"the investment of applicant HASN&#x27;T be LESS 1001 + 2\",\n"
                + "                   function(model) { return huml.GREATER_OR_EQUALS(model.investment, (1001.0+2.0));},\n"
                + "                   false\n"
                + "                );"),
        Arguments.of(
            "csharp",
            "huml.appendRule(\"simple_not_condition_as_arithmetic_rule_third\",\n"
                + "                   new String[]{ \"investment\" },\n"
                + "                   \"the investment of applicant HASN&#x27;T be LESS 1001 + 2\",\n"
                + "                   ( model) => huml.GREATER_OR_EQUALS(model.Investment, (1001.0+2.0)),\n"
                + "                   false\n"
                + "                );"),
        Arguments.of(
            "java",
            "huml.appendRule(\"simple_not_condition_as_arithmetic_rule_third\",\n"
                + "           new String[]{ \"investment\" },\n"
                + "           \"the investment of applicant HASN&#x27;T be LESS 1001 + 2\",\n"
                + "           ( model) -> huml.GREATER_OR_EQUALS(model.getInvestment(), (1001.0+2.0)),\n"
                + "           false\n"
                + "        );"));
  }

  @ParameterizedTest(name = GTE.PARAM_TEST_NAME)
  @MethodSource()
  public void simple_not_condition_as_arithmetic_rule_third(String language, String expected)
      throws Exception {
    GTE.execute(
        expected,
        language,
        p -> {
          ASTRuleBuilder builder = new ASTRuleBuilder();
          builder
              .createRule(
                  "simple_not_condition_as_arithmetic_rule_third",
                  "the investment of applicant HASN'T be LESS 1001 + 2")
              .createCondition()
              .withLeftOperandAsProperty("investment")
              .withOperator(ASTComparisonOperator.GREATER_OR_EQUALS)
              .createRightOperandAsArithmeticalOperation()
              .withNumber(1001)
              .plus(2);
          return builder.getModel();
        });
  }

  private static Stream<Arguments> simple_not_condition_as_arithmetic_rule_fourth() {
    return Stream.of(
        //                            language      expected
        Arguments.of(
            "javascript",
            "huml.appendRule(\"simple_not_condition_as_arithmetic_rule_fourth\",\n"
                + "                   [\"investment\"],\n"
                + "                   \"the investment of applicant HAVEN&#x27;T be GREATER 1001 + 2\",\n"
                + "                   function(model) { return huml.LESS_OR_EQUALS(model.investment, (1001.0+2.0));},\n"
                + "                   false\n"
                + "                );"),
        Arguments.of(
            "csharp",
            "huml.appendRule(\"simple_not_condition_as_arithmetic_rule_fourth\",\n"
                + "                   new String[]{ \"investment\" },\n"
                + "                   \"the investment of applicant HAVEN&#x27;T be GREATER 1001 + 2\",\n"
                + "                   ( model) => huml.LESS_OR_EQUALS(model.Investment, (1001.0+2.0)),\n"
                + "                   false\n"
                + "                );"),
        Arguments.of(
            "java",
            "huml.appendRule(\"simple_not_condition_as_arithmetic_rule_fourth\",\n"
                + "           new String[]{ \"investment\" },\n"
                + "           \"the investment of applicant HAVEN&#x27;T be GREATER 1001 + 2\",\n"
                + "           ( model) -> huml.LESS_OR_EQUALS(model.getInvestment(), (1001.0+2.0)),\n"
                + "           false\n"
                + "        );"));
  }

  @ParameterizedTest(name = GTE.PARAM_TEST_NAME)
  @MethodSource()
  public void simple_not_condition_as_arithmetic_rule_fourth(String language, String expected)
      throws Exception {
    GTE.execute(
        expected,
        language,
        p -> {
          ASTRuleBuilder builder = new ASTRuleBuilder();
          builder
              .createRule(
                  "simple_not_condition_as_arithmetic_rule_fourth",
                  "the investment of applicant HAVEN'T be GREATER 1001 + 2")
              .createCondition()
              .withLeftOperandAsProperty("investment")
              .withOperator(ASTComparisonOperator.LESS_OR_EQUALS)
              .createRightOperandAsArithmeticalOperation()
              .withNumber(1001)
              .plus(2);
          return builder.getModel();
        });
  }

  private static Stream<Arguments> simple_condition_as_rule_group() {
    return Stream.of(
        //                            language      expected
        Arguments.of(
            "javascript",
            "huml.appendRule(\"simple_condition_as_rule_group\",\n"
                + "                   [\"Age\"],\n"
                + "                   \"Der Kunde muss mindestens 18 Jahre alt sein!\",\n"
                + "                   function(model) { return (huml.GREATER_OR_EQUALS(model.Age, 18.0));},\n"
                + "                   false\n"
                + "                );"),
        Arguments.of(
            "csharp",
            "huml.appendRule(\"simple_condition_as_rule_group\",\n"
                + "                   new String[]{ \"Age\" },\n"
                + "                   \"Der Kunde muss mindestens 18 Jahre alt sein!\",\n"
                + "                   ( model) => (huml.GREATER_OR_EQUALS(model.Age, 18.0)),\n"
                + "                   false\n"
                + "                );"),
        Arguments.of(
            "java",
            "huml.appendRule(\"simple_condition_as_rule_group\",\n"
                + "           new String[]{ \"Age\" },\n"
                + "           \"Der Kunde muss mindestens 18 Jahre alt sein!\",\n"
                + "           ( model) -> (huml.GREATER_OR_EQUALS(model.getAge(), 18.0)),\n"
                + "           false\n"
                + "        );"));
  }

  @ParameterizedTest(name = GTE.PARAM_TEST_NAME)
  @MethodSource()
  public void simple_condition_as_rule_group(String language, String expected) throws Exception {
    GTE.execute(
        expected,
        language,
        p -> {
          ASTRuleBuilder builder = new ASTRuleBuilder();
          builder
              .createRule(
                  "simple_condition_as_rule_group", "Der Kunde muss mindestens 18 Jahre alt sein!")
              .createConditionGroup()
              .createCondition()
              .withLeftOperandAsProperty("Age")
              .withOperator(ASTComparisonOperator.GREATER_OR_EQUALS)
              .withRightOperandAsNumber(18);
          return builder.getModel();
        });
  }

  private static Stream<Arguments> simple_condition_as_rule_group_variable() {
    return Stream.of(
        //                            language      expected
        Arguments.of(
            "javascript",
            "huml.appendRule(\"simple_condition_as_rule_group\",\n"
                + "                   [\"Age\"],\n"
                + "                   \"Der Kunde muss mindestens 18 Jahre alt sein!\",\n"
                + "                   function(model) { return (huml.GREATER_OR_EQUALS(model.Age, underage.GetValue(model)));},\n"
                + "                   false\n"
                + "                );"),
        Arguments.of(
            "csharp",
            "huml.appendRule(\"simple_condition_as_rule_group\",\n"
                + "                   new String[]{ \"Age\" },\n"
                + "                   \"Der Kunde muss mindestens 18 Jahre alt sein!\",\n"
                + "                   ( model) => (huml.GREATER_OR_EQUALS(model.Age, underage.GetValue(model))),\n"
                + "                   false\n"
                + "                );"),
        Arguments.of(
            "java",
            "huml.appendRule(\"simple_condition_as_rule_group\",\n"
                + "           new String[]{ \"Age\" },\n"
                + "           \"Der Kunde muss mindestens 18 Jahre alt sein!\",\n"
                + "           ( model) -> (huml.GREATER_OR_EQUALS(model.getAge(), underage.GetValue(model))),\n"
                + "           false\n"
                + "        );"));
  }

  @ParameterizedTest(name = GTE.PARAM_TEST_NAME)
  @MethodSource()
  public void simple_condition_as_rule_group_variable(String language, String expected)
      throws Exception {
    GTE.execute(
        expected,
        language,
        p -> {
          ASTVariableBuilder variableBuilder = new ASTVariableBuilder();
          variableBuilder.createVariable("underage").withValueAsNumber(18);

          ASTRuleBuilder builder = new ASTRuleBuilder();
          builder
              .createRule(
                  "simple_condition_as_rule_group", "Der Kunde muss mindestens 18 Jahre alt sein!")
              .createConditionGroup()
              .createCondition()
              .withLeftOperandAsProperty("Age")
              .withOperator(ASTComparisonOperator.GREATER_OR_EQUALS)
              .withRightOperandAsVariable("underage");
          return builder.getModel();
        });
  }

  private static Stream<Arguments> simple_AND_condition_group_as_rule() {
    return Stream.of(
        //                            language      expected
        Arguments.of(
            "javascript",
            "huml.appendRule(\"simple_AND_condition_group_as_rule\",\n"
                + "                   [\"Age\", \"Name\"],\n"
                + "                   \"Der Kunde muss 18 Jahre alt sein und Lisa heißen.\",\n"
                + "                   function(model) { return (huml.EQUALS(model.Age, 18.0) && huml.IS(\"Lisa\", model.Name));},\n"
                + "                   false\n"
                + "                );"),
        Arguments.of(
            "csharp",
            "huml.appendRule(\"simple_AND_condition_group_as_rule\",\n"
                + "                   new String[]{ \"Age\", \"Name\" },\n"
                + "                   \"Der Kunde muss 18 Jahre alt sein und Lisa heißen.\",\n"
                + "                   ( model) => (huml.EQUALS(model.Age, 18.0) && huml.IS(\"Lisa\", model.Name)),\n"
                + "                   false\n"
                + "                );"),
        Arguments.of(
            "java",
            "huml.appendRule(\"simple_AND_condition_group_as_rule\",\n"
                + "           new String[]{ \"Age\", \"Name\" },\n"
                + "           \"Der Kunde muss 18 Jahre alt sein und Lisa heißen.\",\n"
                + "           ( model) -> (huml.EQUALS(model.getAge(), 18.0) && huml.IS(\"Lisa\", model.getName())),\n"
                + "           false\n"
                + "        );"));
  }

  @ParameterizedTest(name = GTE.PARAM_TEST_NAME)
  @MethodSource()
  public void simple_AND_condition_group_as_rule(String language, String expected)
      throws Exception {
    GTE.execute(
        expected,
        language,
        p -> {
          ASTRuleBuilder builder = new ASTRuleBuilder();
          builder
              .createRule(
                  "simple_AND_condition_group_as_rule",
                  "Der Kunde muss 18 Jahre alt sein und Lisa heißen.")
              .createConditionGroup()
              .createCondition()
              .withLeftOperandAsProperty("Age")
              .withOperator(ASTComparisonOperator.EQUALS)
              .withRightOperandAsNumber(18)
              .parentGroup()
              .appendCondition(ASTConditionConnector.AND)
              .withLeftOperandAsString("Lisa")
              .withOperator(ASTComparisonOperator.IS)
              .withRightOperandAsProperty("Name");
          return builder.getModel();
        });
  }

  private static Stream<Arguments> simple_OR_condition_group_as_rule() {
    return Stream.of(
        //                            language      expected
        Arguments.of(
            "javascript",
            "huml.appendRule(\"simple_OR_condition_group_as_rule\",\n"
                + "                   [\"Age\"],\n"
                + "                   \"Das Alter ist nicht erlaubt.\",\n"
                + "                   function(model) { return (huml.LESS_OR_EQUALS(model.Age, 50.0) || huml.IS_BETWEEN(18.0, 50.0));},\n"
                + "                   false\n"
                + "                );"),
        Arguments.of(
            "csharp",
            "huml.appendRule(\"simple_OR_condition_group_as_rule\",\n"
                + "                   new String[]{ \"Age\" },\n"
                + "                   \"Das Alter ist nicht erlaubt.\",\n"
                + "                   ( model) => (huml.LESS_OR_EQUALS(model.Age, 50.0) || huml.IS_BETWEEN(18.0, 50.0)),\n"
                + "                   false\n"
                + "                );"),
        Arguments.of(
            "java",
            "huml.appendRule(\"simple_OR_condition_group_as_rule\",\n"
                + "           new String[]{ \"Age\" },\n"
                + "           \"Das Alter ist nicht erlaubt.\",\n"
                + "           ( model) -> (huml.LESS_OR_EQUALS(model.getAge(), 50.0) || huml.IS_BETWEEN(18.0, 50.0)),\n"
                + "           false\n"
                + "        );"));
  }

  @ParameterizedTest(name = GTE.PARAM_TEST_NAME)
  @MethodSource()
  public void simple_OR_condition_group_as_rule(String language, String expected) throws Exception {
    GTE.execute(
        expected,
        language,
        p -> {
          ASTRuleBuilder builder = new ASTRuleBuilder();
          builder
              .createRule("simple_OR_condition_group_as_rule", "Das Alter ist nicht erlaubt.")
              .createConditionGroup()
              .createCondition()
              .withLeftOperandAsProperty("Age")
              .withOperator(ASTComparisonOperator.LESS_OR_EQUALS)
              .withRightOperandAsNumber(50)
              .parentGroup()
              .appendCondition(ASTConditionConnector.OR)
              .withLeftOperandAsNumber(18)
              .withOperator(ASTComparisonOperator.IS_BETWEEN)
              .withRightOperandAsNumber(50);
          // .withRightOperandAsVariable("maximales Alter");

          return builder.getModel();
        });
  }
}
