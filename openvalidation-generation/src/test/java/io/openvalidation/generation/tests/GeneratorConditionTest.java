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
import io.openvalidation.common.ast.builder.ASTConditionBuilder;
import io.openvalidation.common.ast.builder.ASTConditionGroupBuilder;
import io.openvalidation.common.ast.builder.ASTRuleBuilder;
import io.openvalidation.common.ast.builder.ASTVariableBuilder;
import io.openvalidation.common.ast.condition.ASTConditionConnector;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class GeneratorConditionTest {
  private static Stream<Arguments> simple_condition() {
    return Stream.of(
        //                            language      expected
        Arguments.of("javascript", "huml.LESS_THAN(model.Age, 18.0)"),
        Arguments.of("csharp", "huml.LESS_THAN(model.Age, 18.0)"),
        Arguments.of("java", "huml.LESS_THAN(model.getAge(), 18.0)"));
  }

  @ParameterizedTest(name = GTE.PARAM_TEST_NAME)
  @MethodSource()
  public void simple_condition(String language, String expected) throws Exception {
    GTE.execute(
        expected,
        language,
        p -> {
          ASTConditionBuilder builder = new ASTConditionBuilder();
          builder
              .create()
              .withLeftOperandAsProperty("Age")
              .withOperator(ASTComparisonOperator.LESS_THAN)
              .withRightOperandAsNumber(18);

          return builder.getModel();
        });
  }

  private static Stream<Arguments> simple_condition_variable() {
    return Stream.of(
        //                            language      expected
        Arguments.of("javascript", "huml.LESS_THAN(model.Age, number.GetValue(model))"),
        Arguments.of("csharp", "huml.LESS_THAN(model.Age, number.GetValue(model))"),
        Arguments.of("java", "huml.LESS_THAN(model.getAge(), number.GetValue(model))"));
  }

  @ParameterizedTest(name = GTE.PARAM_TEST_NAME)
  @MethodSource()
  public void simple_condition_variable(String language, String expected) throws Exception {
    GTE.execute(
        expected,
        language,
        p -> {
          ASTVariableBuilder variableBuilder = new ASTVariableBuilder();
          variableBuilder.createVariable("number").withValueAsNumber(10);

          ASTConditionBuilder builder = new ASTConditionBuilder();
          builder
              .create()
              .withLeftOperandAsProperty("Age")
              .withOperator(ASTComparisonOperator.LESS_THAN)
              .withRightOperandAsVariable("number");

          return builder.getModel();
        });
  }

  private static Stream<Arguments> simple_condition_reverse() {
    return Stream.of(
        //                            language      expected
        Arguments.of("javascript", "huml.GREATER_OR_EQUALS(18.1, age.GetValue(model))"),
        Arguments.of("csharp", "huml.GREATER_OR_EQUALS(18.1, age.GetValue(model))"),
        Arguments.of("java", "huml.GREATER_OR_EQUALS(18.1, age.GetValue(model))"));
  }

  @ParameterizedTest(name = GTE.PARAM_TEST_NAME)
  @MethodSource()
  public void simple_condition_reverse(String language, String expected) throws Exception {
    GTE.execute(
        expected,
        language,
        p -> {
          ASTVariableBuilder variableBuilder = new ASTVariableBuilder();
          variableBuilder.createVariable("age").withValueAsNumber(10.1);

          ASTConditionBuilder builder = new ASTConditionBuilder();
          builder
              .create()
              .withLeftOperandAsNumber(18.1)
              .withOperator(ASTComparisonOperator.GREATER_OR_EQUALS)
              .withRightOperandAsVariable("age");

          return builder.getModel();
        });
  }

  private static Stream<Arguments> simple_condition_variable_reverse() {
    return Stream.of(
        //                            language      expected
        Arguments.of("javascript", "huml.IS(number.GetValue(model), model.Age)"),
        Arguments.of("csharp", "huml.IS(number.GetValue(model), model.Age)"),
        Arguments.of("java", "huml.IS(number.GetValue(model), model.getAge())"));
  }

  @ParameterizedTest(name = GTE.PARAM_TEST_NAME)
  @MethodSource()
  public void simple_condition_variable_reverse(String language, String expected) throws Exception {
    GTE.execute(
        expected,
        language,
        p -> {
          ASTVariableBuilder variableBuilder = new ASTVariableBuilder();
          variableBuilder.createVariable("number").withValueAsNumber(10);

          ASTConditionBuilder builder = new ASTConditionBuilder();
          builder
              .create()
              .withLeftOperandAsVariable("number")
              .withOperator(ASTComparisonOperator.IS)
              .withRightOperandAsProperty("Age");

          return builder.getModel();
        });
  }

  private static Stream<Arguments> condition_group() {
    return Stream.of(
        //                            language      expected
        Arguments.of(
            "javascript", "(huml.EQUALS(model.Age, 18.0) || huml.GREATER_THAN(model.Age, 18.0))"),
        Arguments.of(
            "csharp", "(huml.EQUALS(model.Age, 18.0) || huml.GREATER_THAN(model.Age, 18.0))"),
        Arguments.of(
            "java",
            "(huml.EQUALS(model.getAge(), 18.0) || huml.GREATER_THAN(model.getAge(), 18.0))"));
  }

  @ParameterizedTest(name = GTE.PARAM_TEST_NAME)
  @MethodSource()
  public void condition_group(String language, String expected) throws Exception {
    GTE.execute(
        expected,
        language,
        p -> {
          ASTConditionGroupBuilder builder = new ASTConditionGroupBuilder(new ASTRuleBuilder());
          builder
              .create()
              .appendCondition(null)
              .withLeftOperandAsProperty("Age")
              .withOperator(ASTComparisonOperator.EQUALS)
              .withRightOperandAsNumber(18)
              .parentGroup()
              .appendCondition(ASTConditionConnector.OR)
              .withLeftOperandAsProperty("Age")
              .withOperator(ASTComparisonOperator.GREATER_THAN)
              .withRightOperandAsNumber(18);

          return builder.getModel();
        });
  }

  private static Stream<Arguments> condition_group_reverse() {
    return Stream.of(
        //                            language      expected
        Arguments.of(
            "javascript", "(huml.CONTAINS(18.0, model.Age) || huml.EXISTS(18.0, model.Age))"),
        Arguments.of("csharp", "(huml.CONTAINS(18.0, model.Age) || huml.EXISTS(18.0, model.Age))"),
        Arguments.of(
            "java", "(huml.CONTAINS(18.0, model.getAge()) || huml.EXISTS(18.0, model.getAge()))"));
  }

  @ParameterizedTest(name = GTE.PARAM_TEST_NAME)
  @MethodSource()
  public void condition_group_reverse(String language, String expected) throws Exception {
    GTE.execute(
        expected,
        language,
        p -> {
          ASTConditionGroupBuilder builder = new ASTConditionGroupBuilder(new ASTRuleBuilder());
          builder
              .create()
              .appendCondition(null)
              .withLeftOperandAsNumber(18)
              .withOperator(ASTComparisonOperator.CONTAINS)
              .withRightOperandAsProperty("Age")
              .parentGroup()
              .appendCondition(ASTConditionConnector.OR)
              .withRightOperandAsProperty("Age")
              .withOperator(ASTComparisonOperator.EXISTS)
              .withLeftOperandAsNumber(18);

          return builder.getModel();
        });
  }

  private static Stream<Arguments> complex_AND_condition_group() {
    return Stream.of(
        //                            language      expected
        Arguments.of(
            "javascript",
            "(huml.EQUALS(model.Age, 18.0) && (huml.EQUALS(model.Job, \"Professor\") || huml.NOT_EQUALS(model.Job, \"Consultant\")))"),
        Arguments.of(
            "csharp",
            "(huml.EQUALS(model.Age, 18.0) && (huml.EQUALS(model.Job, \"Professor\") || huml.NOT_EQUALS(model.Job, \"Consultant\")))"),
        Arguments.of(
            "java",
            "(huml.EQUALS(model.getAge(), 18.0) && (huml.EQUALS(model.getJob(), \"Professor\") || huml.NOT_EQUALS(model.getJob(), \"Consultant\")))"));
  }

  @ParameterizedTest(name = GTE.PARAM_TEST_NAME)
  @MethodSource()
  public void complex_AND_condition_group(String language, String expected) throws Exception {
    GTE.execute(
        expected,
        language,
        p -> {
          ASTConditionGroupBuilder builder = new ASTConditionGroupBuilder(new ASTRuleBuilder());
          builder
              .create()
              .appendCondition(null)
              .withLeftOperandAsProperty("Age")
              .withOperator(ASTComparisonOperator.EQUALS)
              .withRightOperandAsNumber(18)
              .parentGroup()
              .appendConditionGroup(ASTConditionConnector.AND)
              .appendCondition(null)
              .withLeftOperandAsProperty("Job")
              .withOperator(ASTComparisonOperator.EQUALS)
              .withRightOperandAsString("Professor")
              .parentGroup()
              .appendCondition(ASTConditionConnector.OR)
              .withLeftOperandAsProperty("Job")
              .withOperator(ASTComparisonOperator.NOT_EQUALS)
              .withRightOperandAsString("Consultant");

          return builder.getModel();
        });
  }

  private static Stream<Arguments> complex_AND_condition_group_reverse() {
    return Stream.of(
        //                            language      expected
        Arguments.of(
            "javascript",
            "((huml.EQUALS(model.Job, \"Professor\") || huml.NOT_EQUALS(model.Job, \"Consultant\") && (huml.EQUALS(model.Age, 18.0))))"),
        Arguments.of(
            "csharp",
            "((huml.EQUALS(model.Job, \"Professor\") || huml.NOT_EQUALS(model.Job, \"Consultant\") && (huml.EQUALS(model.Age, 18.0))))"),
        Arguments.of(
            "java",
            "((huml.EQUALS(model.getJob(), \"Professor\") || huml.NOT_EQUALS(model.getJob(), \"Consultant\") && (huml.EQUALS(model.getAge(), 18.0))))"));
  }

  @ParameterizedTest(name = GTE.PARAM_TEST_NAME)
  @MethodSource()
  public void complex_AND_condition_group_reverse(String language, String expected)
      throws Exception {
    GTE.execute(
        expected,
        language,
        p -> {
          ASTConditionGroupBuilder builder = new ASTConditionGroupBuilder(new ASTRuleBuilder());
          builder
              .create()
              .appendConditionGroup(null)
              .appendCondition(null)
              .withLeftOperandAsProperty("Job")
              .withOperator(ASTComparisonOperator.EQUALS)
              .withRightOperandAsString("Professor")
              .parentGroup()
              .appendCondition(ASTConditionConnector.OR)
              .withLeftOperandAsProperty("Job")
              .withOperator(ASTComparisonOperator.NOT_EQUALS)
              .withRightOperandAsString("Consultant")
              .parentGroup()
              .appendConditionGroup(ASTConditionConnector.AND)
              .appendCondition(null)
              .withLeftOperandAsProperty("Age")
              .withOperator(ASTComparisonOperator.EQUALS)
              .withRightOperandAsNumber(18);

          return builder.getModel();
        });
  }

  private static Stream<Arguments> complex_OR_condition_group() {
    return Stream.of(
        //                            language      expected
        Arguments.of(
            "javascript",
            "(huml.ALL_OF(model.Skills, model.Skills) || (huml.GREATER_OR_EQUALS(model.Grade, 3.0) && huml.ONE_OF(model.Degree, [1.0,2.0,3.0])))"),
        Arguments.of(
            "csharp",
            "(huml.ALL_OF(model.Skills, model.Skills) || (huml.GREATER_OR_EQUALS(model.Grade, 3.0) && huml.ONE_OF(model.Degree, 1.0,2.0,3.0)))"),
        Arguments.of(
            "java",
            "(huml.ALL_OF(model.getSkills(), model.getSkills()) || (huml.GREATER_OR_EQUALS(model.getGrade(), 3.0) && huml.ONE_OF(model.getDegree(), 1.0,2.0,3.0)))"));
  }

  @ParameterizedTest(name = GTE.PARAM_TEST_NAME)
  @MethodSource()
  public void complex_OR_condition_group(String language, String expected) throws Exception {
    GTE.execute(
        expected,
        language,
        p -> {
          ASTConditionGroupBuilder builder = new ASTConditionGroupBuilder(new ASTRuleBuilder());
          builder
              .create()
              .appendCondition(null)
              .withLeftOperandAsProperty("Skills")
              .withOperator(ASTComparisonOperator.ALL_OF)
              .withRightOperandAsProperty("Skills")
              .parentGroup()
              .appendConditionGroup(ASTConditionConnector.OR)
              .appendCondition(null)
              .withLeftOperandAsProperty("Grade")
              .withOperator(ASTComparisonOperator.GREATER_OR_EQUALS)
              .withRightOperandAsNumber(3)
              .parentGroup()
              .appendCondition(ASTConditionConnector.AND)
              .withLeftOperandAsProperty("Degree")
              .withOperator(ASTComparisonOperator.ONE_OF)
              .withRightOperandAsArray(1, 2, 3);
          // .withRightOperandAsArray("Master","Bachelor","Doctor");

          return builder.getModel();
        });
  }

  private static Stream<Arguments> complex_OR_condition_group_reverse() {
    return Stream.of(
        //                            language      expected
        Arguments.of(
            "javascript",
            "((huml.GREATER_OR_EQUALS(model.Grade, 3.0) && huml.ALL_OF(model.Skills, model.Skills) || (huml.ONE_OF(model.Degree, [1.0,2.0,3.0]))))"),
        Arguments.of(
            "csharp",
            "((huml.GREATER_OR_EQUALS(model.Grade, 3.0) && huml.ALL_OF(model.Skills, model.Skills) || (huml.ONE_OF(model.Degree, 1.0,2.0,3.0))))"),
        Arguments.of(
            "java",
            "((huml.GREATER_OR_EQUALS(model.getGrade(), 3.0) && huml.ALL_OF(model.getSkills(), model.getSkills()) || (huml.ONE_OF(model.getDegree(), 1.0,2.0,3.0))))"));
  }

  @ParameterizedTest(name = GTE.PARAM_TEST_NAME)
  @MethodSource()
  public void complex_OR_condition_group_reverse(String language, String expected)
      throws Exception {
    GTE.execute(
        expected,
        language,
        p -> {
          ASTConditionGroupBuilder builder = new ASTConditionGroupBuilder(new ASTRuleBuilder());
          builder
              .create()
              .appendConditionGroup(null)
              .appendCondition(null)
              .withLeftOperandAsProperty("Grade")
              .withOperator(ASTComparisonOperator.GREATER_OR_EQUALS)
              .withRightOperandAsNumber(3)
              .parentGroup()
              // .parent()

              .appendCondition(ASTConditionConnector.AND)
              .withLeftOperandAsProperty("Skills")
              .withOperator(ASTComparisonOperator.ALL_OF)
              .withRightOperandAsProperty("Skills")
              // .withRightOperandAsArray("Master","Bachelor","Doctor");

              .parentGroup()
              .appendConditionGroup(ASTConditionConnector.OR)
              .appendCondition(null)
              .withLeftOperandAsProperty("Degree")
              .withOperator(ASTComparisonOperator.ONE_OF)
              .withRightOperandAsArray(1, 2, 3);

          return builder.getModel();
        });
  }

  private static Stream<Arguments> complex_AND_OR_AND_condition_group() {
    return Stream.of(
        //                            language      expected
        Arguments.of(
            "javascript",
            "(huml.LESS_THAN(model.Age, 18.0) && huml.LESS_OR_EQUALS(model.Age, 18.0) || (huml.NOT_EQUALS(model.Age, 18.0) && huml.LESS_OR_EQUALS(model.Age, 18.0)))"),
        Arguments.of(
            "csharp",
            "(huml.LESS_THAN(model.Age, 18.0) && huml.LESS_OR_EQUALS(model.Age, 18.0) || (huml.NOT_EQUALS(model.Age, 18.0) && huml.LESS_OR_EQUALS(model.Age, 18.0)))"),
        Arguments.of(
            "java",
            "(huml.LESS_THAN(model.getAge(), 18.0) && huml.LESS_OR_EQUALS(model.getAge(), 18.0) || (huml.NOT_EQUALS(model.getAge(), 18.0) && huml.LESS_OR_EQUALS(model.getAge(), 18.0)))"));
  }

  @ParameterizedTest(name = GTE.PARAM_TEST_NAME)
  @MethodSource()
  public void complex_AND_OR_AND_condition_group(String language, String expected)
      throws Exception {
    GTE.execute(
        expected,
        language,
        p -> {
          ASTConditionGroupBuilder builder = new ASTConditionGroupBuilder(new ASTRuleBuilder());
          builder
              .create()

              // .appendConditionGroup(null)
              .appendCondition(null)
              .withLeftOperandAsProperty("Age")
              .withOperator(ASTComparisonOperator.LESS_THAN)
              .withRightOperandAsNumber(18)
              .parentGroup()
              .appendCondition(ASTConditionConnector.AND)
              .withLeftOperandAsProperty("Age")
              .withOperator(ASTComparisonOperator.LESS_OR_EQUALS)
              .withRightOperandAsNumber(18)
              .parentGroup()
              .appendConditionGroup(ASTConditionConnector.OR)
              .appendCondition(null)
              .withLeftOperandAsProperty("Age")
              .withOperator(ASTComparisonOperator.NOT_EQUALS)
              .withRightOperandAsNumber(18)
              .parentGroup()
              .appendCondition(ASTConditionConnector.AND)
              .withLeftOperandAsProperty("Age")
              .withOperator(ASTComparisonOperator.LESS_OR_EQUALS)
              .withRightOperandAsNumber(18);

          return builder.getModel();
        });
  }

  private static Stream<Arguments> complex_AND_OR_OR_condition_group() {
    return Stream.of(
        //                            language      expected
        Arguments.of(
            "javascript",
            "(huml.LESS_THAN(model.Age, 18.0) && huml.LESS_OR_EQUALS(model.Age, 18.0) || (huml.NOT_EQUALS(model.Age, 18.0) || huml.LESS_OR_EQUALS(model.Age, 18.0)))"),
        Arguments.of(
            "csharp",
            "(huml.LESS_THAN(model.Age, 18.0) && huml.LESS_OR_EQUALS(model.Age, 18.0) || (huml.NOT_EQUALS(model.Age, 18.0) || huml.LESS_OR_EQUALS(model.Age, 18.0)))"),
        Arguments.of(
            "java",
            "(huml.LESS_THAN(model.getAge(), 18.0) && huml.LESS_OR_EQUALS(model.getAge(), 18.0) || (huml.NOT_EQUALS(model.getAge(), 18.0) || huml.LESS_OR_EQUALS(model.getAge(), 18.0)))"));
  }

  @ParameterizedTest(name = GTE.PARAM_TEST_NAME)
  @MethodSource()
  public void complex_AND_OR_OR_condition_group(String language, String expected) throws Exception {
    GTE.execute(
        expected,
        language,
        p -> {
          ASTConditionGroupBuilder builder = new ASTConditionGroupBuilder(new ASTRuleBuilder());
          builder
              .create()

              // .appendConditionGroup(null)
              .appendCondition(null)
              .withLeftOperandAsProperty("Age")
              .withOperator(ASTComparisonOperator.LESS_THAN)
              .withRightOperandAsNumber(18)
              .parentGroup()
              .appendCondition(ASTConditionConnector.AND)
              .withLeftOperandAsProperty("Age")
              .withOperator(ASTComparisonOperator.LESS_OR_EQUALS)
              .withRightOperandAsNumber(18)
              .parentGroup()
              .appendConditionGroup(ASTConditionConnector.OR)
              .appendCondition(null)
              .withLeftOperandAsProperty("Age")
              .withOperator(ASTComparisonOperator.NOT_EQUALS)
              .withRightOperandAsNumber(18)
              .parentGroup()
              .appendCondition(ASTConditionConnector.OR)
              .withLeftOperandAsProperty("Age")
              .withOperator(ASTComparisonOperator.LESS_OR_EQUALS)
              .withRightOperandAsNumber(18);

          return builder.getModel();
        });
  }

  private static Stream<Arguments> complex_OR_AND_OR_condition_group() {
    return Stream.of(
        //                            language      expected
        Arguments.of(
            "javascript",
            "(huml.LESS_THAN(model.Age, 18.0) || huml.LESS_OR_EQUALS(model.Age, 18.0) && (huml.NOT_EQUALS(model.Age, 18.0) || huml.LESS_OR_EQUALS(model.Age, 18.0)))"),
        Arguments.of(
            "csharp",
            "(huml.LESS_THAN(model.Age, 18.0) || huml.LESS_OR_EQUALS(model.Age, 18.0) && (huml.NOT_EQUALS(model.Age, 18.0) || huml.LESS_OR_EQUALS(model.Age, 18.0)))"),
        Arguments.of(
            "java",
            "(huml.LESS_THAN(model.getAge(), 18.0) || huml.LESS_OR_EQUALS(model.getAge(), 18.0) && (huml.NOT_EQUALS(model.getAge(), 18.0) || huml.LESS_OR_EQUALS(model.getAge(), 18.0)))"));
  }

  @ParameterizedTest(name = GTE.PARAM_TEST_NAME)
  @MethodSource()
  public void complex_OR_AND_OR_condition_group(String language, String expected) throws Exception {
    GTE.execute(
        expected,
        language,
        p -> {
          ASTConditionGroupBuilder builder = new ASTConditionGroupBuilder(new ASTRuleBuilder());
          builder
              .create()

              // .appendConditionGroup(null)
              .appendCondition(null)
              .withLeftOperandAsProperty("Age")
              .withOperator(ASTComparisonOperator.LESS_THAN)
              .withRightOperandAsNumber(18)
              .parentGroup()
              .appendCondition(ASTConditionConnector.OR)
              .withLeftOperandAsProperty("Age")
              .withOperator(ASTComparisonOperator.LESS_OR_EQUALS)
              .withRightOperandAsNumber(18)
              .parentGroup()
              .appendConditionGroup(ASTConditionConnector.AND)
              .appendCondition(null)
              .withLeftOperandAsProperty("Age")
              .withOperator(ASTComparisonOperator.NOT_EQUALS)
              .withRightOperandAsNumber(18)
              .parentGroup()
              .appendCondition(ASTConditionConnector.OR)
              .withLeftOperandAsProperty("Age")
              .withOperator(ASTComparisonOperator.LESS_OR_EQUALS)
              .withRightOperandAsNumber(18);

          return builder.getModel();
        });
  }

  private static Stream<Arguments> complex_OR_OR_AND_condition_group() {
    return Stream.of(
        //                            language      expected
        Arguments.of(
            "javascript",
            "(huml.LESS_THAN(model.Age, 18.0) || huml.LESS_OR_EQUALS(model.Age, 18.0) || (huml.NOT_EQUALS(model.Age, 18.0) && huml.LESS_OR_EQUALS(model.Age, 18.0)))"),
        Arguments.of(
            "csharp",
            "(huml.LESS_THAN(model.Age, 18.0) || huml.LESS_OR_EQUALS(model.Age, 18.0) || (huml.NOT_EQUALS(model.Age, 18.0) && huml.LESS_OR_EQUALS(model.Age, 18.0)))"),
        Arguments.of(
            "java",
            "(huml.LESS_THAN(model.getAge(), 18.0) || huml.LESS_OR_EQUALS(model.getAge(), 18.0) || (huml.NOT_EQUALS(model.getAge(), 18.0) && huml.LESS_OR_EQUALS(model.getAge(), 18.0)))"));
  }

  @ParameterizedTest(name = GTE.PARAM_TEST_NAME)
  @MethodSource()
  public void complex_OR_OR_AND_condition_group(String language, String expected) throws Exception {
    GTE.execute(
        expected,
        language,
        p -> {
          ASTConditionGroupBuilder builder = new ASTConditionGroupBuilder(new ASTRuleBuilder());
          builder
              .create()

              // .appendConditionGroup(null)
              .appendCondition(null)
              .withLeftOperandAsProperty("Age")
              .withOperator(ASTComparisonOperator.LESS_THAN)
              .withRightOperandAsNumber(18)
              .parentGroup()
              .appendCondition(ASTConditionConnector.OR)
              .withLeftOperandAsProperty("Age")
              .withOperator(ASTComparisonOperator.LESS_OR_EQUALS)
              .withRightOperandAsNumber(18)
              .parentGroup()
              .appendConditionGroup(ASTConditionConnector.OR)
              .appendCondition(null)
              .withLeftOperandAsProperty("Age")
              .withOperator(ASTComparisonOperator.NOT_EQUALS)
              .withRightOperandAsNumber(18)
              .parentGroup()
              .appendCondition(ASTConditionConnector.AND)
              .withLeftOperandAsProperty("Age")
              .withOperator(ASTComparisonOperator.LESS_OR_EQUALS)
              .withRightOperandAsNumber(18);

          return builder.getModel();
        });
  }

  private static Stream<Arguments> complex_OR_OR_OR_condition_group() {
    return Stream.of(
        //                            language      expected
        Arguments.of(
            "javascript",
            "(huml.LESS_THAN(model.Age, 18.0) || huml.LESS_OR_EQUALS(model.Age, 18.0) || (huml.NOT_EQUALS(model.Age, 18.0) || huml.LESS_OR_EQUALS(model.Age, 18.0)))"),
        Arguments.of(
            "csharp",
            "(huml.LESS_THAN(model.Age, 18.0) || huml.LESS_OR_EQUALS(model.Age, 18.0) || (huml.NOT_EQUALS(model.Age, 18.0) || huml.LESS_OR_EQUALS(model.Age, 18.0)))"),
        Arguments.of(
            "java",
            "(huml.LESS_THAN(model.getAge(), 18.0) || huml.LESS_OR_EQUALS(model.getAge(), 18.0) || (huml.NOT_EQUALS(model.getAge(), 18.0) || huml.LESS_OR_EQUALS(model.getAge(), 18.0)))"));
  }

  @ParameterizedTest(name = GTE.PARAM_TEST_NAME)
  @MethodSource()
  public void complex_OR_OR_OR_condition_group(String language, String expected) throws Exception {
    GTE.execute(
        expected,
        language,
        p -> {
          ASTConditionGroupBuilder builder = new ASTConditionGroupBuilder(new ASTRuleBuilder());
          builder
              .create()

              // .appendConditionGroup(null)
              .appendCondition(null)
              .withLeftOperandAsProperty("Age")
              .withOperator(ASTComparisonOperator.LESS_THAN)
              .withRightOperandAsNumber(18)
              .parentGroup()
              .appendCondition(ASTConditionConnector.OR)
              .withLeftOperandAsProperty("Age")
              .withOperator(ASTComparisonOperator.LESS_OR_EQUALS)
              .withRightOperandAsNumber(18)
              .parentGroup()
              .appendConditionGroup(ASTConditionConnector.OR)
              .appendCondition(null)
              .withLeftOperandAsProperty("Age")
              .withOperator(ASTComparisonOperator.NOT_EQUALS)
              .withRightOperandAsNumber(18)
              .parentGroup()
              .appendCondition(ASTConditionConnector.OR)
              .withLeftOperandAsProperty("Age")
              .withOperator(ASTComparisonOperator.LESS_OR_EQUALS)
              .withRightOperandAsNumber(18);

          return builder.getModel();
        });
  }

  private static Stream<Arguments> complex_AND_AND_AND_condition_group() {
    return Stream.of(
        //                            language      expected
        Arguments.of(
            "javascript",
            "(huml.LESS_THAN(model.Age, 18.0) && huml.LESS_OR_EQUALS(model.Age, 18.0) && (huml.NOT_EQUALS(model.Age, 18.0) && huml.LESS_OR_EQUALS(model.Age, 18.0)))"),
        Arguments.of(
            "csharp",
            "(huml.LESS_THAN(model.Age, 18.0) && huml.LESS_OR_EQUALS(model.Age, 18.0) && (huml.NOT_EQUALS(model.Age, 18.0) && huml.LESS_OR_EQUALS(model.Age, 18.0)))"),
        Arguments.of(
            "java",
            "(huml.LESS_THAN(model.getAge(), 18.0) && huml.LESS_OR_EQUALS(model.getAge(), 18.0) && (huml.NOT_EQUALS(model.getAge(), 18.0) && huml.LESS_OR_EQUALS(model.getAge(), 18.0)))"));
  }

  @ParameterizedTest(name = GTE.PARAM_TEST_NAME)
  @MethodSource()
  public void complex_AND_AND_AND_condition_group(String language, String expected)
      throws Exception {
    GTE.execute(
        expected,
        language,
        p -> {
          ASTConditionGroupBuilder builder = new ASTConditionGroupBuilder(new ASTRuleBuilder());
          builder
              .create()

              // .appendConditionGroup(null)
              .appendCondition(null)
              .withLeftOperandAsProperty("Age")
              .withOperator(ASTComparisonOperator.LESS_THAN)
              .withRightOperandAsNumber(18)
              .parentGroup()
              .appendCondition(ASTConditionConnector.AND)
              .withLeftOperandAsProperty("Age")
              .withOperator(ASTComparisonOperator.LESS_OR_EQUALS)
              .withRightOperandAsNumber(18)
              .parentGroup()
              .appendConditionGroup(ASTConditionConnector.AND)
              .appendCondition(null)
              .withLeftOperandAsProperty("Age")
              .withOperator(ASTComparisonOperator.NOT_EQUALS)
              .withRightOperandAsNumber(18)
              .parentGroup()
              .appendCondition(ASTConditionConnector.AND)
              .withLeftOperandAsProperty("Age")
              .withOperator(ASTComparisonOperator.LESS_OR_EQUALS)
              .withRightOperandAsNumber(18);

          return builder.getModel();
        });
  }

  private static Stream<Arguments> at_least_one_of_condition() {
    return Stream.of(
        //                            language      expected
        Arguments.of("javascript", "huml.AT_LEAST_ONE_OF(model.Name, [\"Boris\",\"Donald\"])"),
        Arguments.of("csharp", "huml.AT_LEAST_ONE_OF(model.Name, \"Boris\",\"Donald\")"),
        Arguments.of("java", "huml.AT_LEAST_ONE_OF(model.getName(), \"Boris\",\"Donald\")"));
  }

  @ParameterizedTest(name = GTE.PARAM_TEST_NAME)
  @MethodSource()
  public void at_least_one_of_condition(String language, String expected) throws Exception {
    GTE.execute(
        expected,
        language,
        p -> {
          ASTConditionBuilder builder = new ASTConditionBuilder();
          builder
              .create()
              .withLeftOperandAsProperty("Name")
              .withOperator(ASTComparisonOperator.AT_LEAST_ONE_OF)
              .withRightOperandAsArray("Boris", "Donald");

          return builder.getModel();
        });
  }

  private static Stream<Arguments> using_exists_operator_in_condition() {
    return Stream.of(
        //                            language      expected
        Arguments.of("javascript", "huml.EXISTS(model.Name)"),
        Arguments.of("csharp", "huml.EXISTS(model.Name)"),
        Arguments.of("java", "huml.EXISTS(model.getName())"));
  }

  @ParameterizedTest(name = GTE.PARAM_TEST_NAME)
  @MethodSource()
  public void using_exists_operator_in_condition(String language, String expected)
      throws Exception {
    GTE.execute(
        expected,
        language,
        p -> {
          ASTConditionBuilder builder = new ASTConditionBuilder();
          builder
              .create()
              .withLeftOperandAsProperty("Name")
              .withOperator(ASTComparisonOperator.EXISTS);

          return builder.getModel();
        });
  }

  private static Stream<Arguments> using_notExists_operator_in_condition() {
    return Stream.of(
        //                            language      expected
        Arguments.of("javascript", "huml.NOT_EXISTS(model.Name)"),
        Arguments.of("csharp", "huml.NOT_EXISTS(model.Name)"),
        Arguments.of("java", "huml.NOT_EXISTS(model.getName())"));
  }

  @ParameterizedTest(name = GTE.PARAM_TEST_NAME)
  @MethodSource()
  public void using_notExists_operator_in_condition(String language, String expected)
      throws Exception {
    GTE.execute(
        expected,
        language,
        p -> {
          ASTConditionBuilder builder = new ASTConditionBuilder();
          builder
              .create()
              .withLeftOperandAsProperty("Name")
              .withOperator(ASTComparisonOperator.NOT_EXISTS);

          return builder.getModel();
        });
  }
}
