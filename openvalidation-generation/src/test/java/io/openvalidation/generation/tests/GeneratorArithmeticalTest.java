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

import io.openvalidation.common.ast.ASTArithmeticalOperator;
import io.openvalidation.common.ast.ASTComparisonOperator;
import io.openvalidation.common.ast.builder.ASTConditionBuilder;
import io.openvalidation.common.ast.builder.ASTVariableBuilder;
import io.openvalidation.common.ast.operand.ASTOperandVariable;
import io.openvalidation.common.ast.operand.arithmetical.ASTOperandArithmeticalPropertyItem;
import io.openvalidation.common.ast.operand.arithmetical.ASTOperandArithmeticalVariable;
import io.openvalidation.common.ast.operand.property.ASTOperandProperty;
import java.util.stream.Stream;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class GeneratorArithmeticalTest {

  private static Stream<Arguments> simple_operation() {
    return Stream.of(
        //            language      expected
        Arguments.of("javascript", "huml.NOT_EQUALS((2.0+2.0), 18.0)"),
        Arguments.of("java", "huml.NOT_EQUALS((2.0+2.0), 18.0)"),
        Arguments.of("csharp", "huml.NOT_EQUALS((2.0+2.0), 18.0)"),
        Arguments.of("python", "huml.NOT_EQUALS((2.0+2.0), 18.0)"));
  }

  @ParameterizedTest(name = GTE.PARAM_TEST_NAME)
  @MethodSource()
  void simple_operation(String language, String expected) throws Exception {
    GTE.execute(
        expected,
        language,
        p -> {
          ASTConditionBuilder builder = new ASTConditionBuilder();
          // ASTOperandArithmeticalBuilder builder = new ASTOperandArithmeticalBuilder();
          builder
              .create()
              .createLeftOperandAsArithmeticalOperation()
              .withNumber(2)
              .plus(2)
              .parent()
              .withOperator(ASTComparisonOperator.NOT_EQUALS)
              .withRightOperandAsNumber(18);
          return builder.getModel();
        });
  }

  private static Stream<Arguments> simple_operation_with_property() {
    return Stream.of(
        //            language      expected
        Arguments.of("javascript", "huml.EQUALS((2.1-model.user.age), 18.0)"),
        Arguments.of(
            "java", "huml.EQUALS((2.1-((Number)model.getUser().getAge()).doubleValue()), 18.0)"),
        Arguments.of("csharp", "huml.EQUALS((2.1-model.User.Age), 18.0)"),
        Arguments.of("python", "huml.EQUALS((2.1-model.user.age), 18.0)"));
  }

  @ParameterizedTest(name = GTE.PARAM_TEST_NAME)
  @MethodSource()
  void simple_operation_with_property(String language, String expected) throws Exception {
    GTE.execute(
        expected,
        language,
        p -> {
          ASTConditionBuilder builder = new ASTConditionBuilder();
          // ASTOperandArithmeticalBuilder builder = new ASTOperandArithmeticalBuilder();
          builder
              .create()
              .createLeftOperandAsArithmeticalOperation()
              .withNumber(2.1)
              .minus(new ASTOperandProperty(new ASTOperandProperty("user", "age")))
              .parent()
              .withOperator(ASTComparisonOperator.EQUALS)
              .withRightOperandAsNumber(18);
          return builder.getModel();
        });
  }

  private static Stream<Arguments> simple_operation_1ess_than() {
    return Stream.of(
        //            language      expected
        Arguments.of("javascript", "huml.LESS_THAN((model.user.age*2.1), 18.0)"),
        Arguments.of(
            "java", "huml.LESS_THAN((((Number)model.getUser().getAge()).doubleValue()*2.1), 18.0)"),
        Arguments.of("csharp", "huml.LESS_THAN((model.User.Age*2.1), 18.0)"),
        Arguments.of("python", "huml.LESS_THAN((model.user.age*2.1), 18.0)"));
  }

  @ParameterizedTest(name = GTE.PARAM_TEST_NAME)
  @MethodSource()
  void simple_operation_1ess_than(String language, String expected) throws Exception {
    GTE.execute(
        expected,
        language,
        p -> {
          ASTConditionBuilder builder = new ASTConditionBuilder();
          // ASTOperandArithmeticalBuilder builder = new ASTOperandArithmeticalBuilder();
          builder
              .create()
              .createLeftOperandAsArithmeticalOperation()
              .withProperty("user", "age")
              .multiply(2.1)
              .parent()
              .withOperator(ASTComparisonOperator.LESS_THAN)
              .withRightOperandAsNumber(18);
          return builder.getModel();
        });
  }

  private static Stream<Arguments> simple_operation_1ess_or_equals() {
    return Stream.of(
        //            language      expected
        Arguments.of(
            "javascript", "huml.LESS_OR_EQUALS((2.0/model.user.age), model.user.investment)"),
        Arguments.of(
            "java",
            "huml.LESS_OR_EQUALS((2.0/((Number)model.getUser().getAge()).doubleValue()), model.getUser().getInvestment())"),
        Arguments.of("csharp", "huml.LESS_OR_EQUALS((2.0/model.User.Age), model.User.Investment)"),
        Arguments.of("python", "huml.LESS_OR_EQUALS((2.0/model.user.age), model.user.investment)"));
  }

  @ParameterizedTest(name = GTE.PARAM_TEST_NAME)
  @MethodSource()
  void simple_operation_1ess_or_equals(String language, String expected) throws Exception {
    GTE.execute(
        expected,
        language,
        p -> {
          ASTConditionBuilder builder = new ASTConditionBuilder();
          // ASTOperandArithmeticalBuilder builder = new ASTOperandArithmeticalBuilder();
          builder
              .create()
              .createLeftOperandAsArithmeticalOperation()
              .withNumber(2)
              .divide(new ASTOperandProperty(new ASTOperandProperty("user", "age")))
              .parent()
              .withOperator(ASTComparisonOperator.LESS_OR_EQUALS)
              // .withRightOperandAsNumber(18);
              .withRightOperandAsProperty("user", "investment");
          return builder.getModel();
        });
  }

  private static Stream<Arguments> simple_operation_greater_or_equals() {
    return Stream.of(
        //            language      expected
        Arguments.of("javascript", "huml.GREATER_OR_EQUALS((2.0+2.0), model.user.age)"),
        Arguments.of("java", "huml.GREATER_OR_EQUALS((2.0+2.0), model.getUser().getAge())"),
        Arguments.of("csharp", "huml.GREATER_OR_EQUALS((2.0+2.0), model.User.Age)"),
        Arguments.of("python", "huml.GREATER_OR_EQUALS((2.0+2.0), model.user.age)"));
  }

  @ParameterizedTest(name = GTE.PARAM_TEST_NAME)
  @MethodSource()
  void simple_operation_greater_or_equals(String language, String expected) throws Exception {
    GTE.execute(
        expected,
        language,
        p -> {
          ASTConditionBuilder builder = new ASTConditionBuilder();
          // ASTOperandArithmeticalBuilder builder = new ASTOperandArithmeticalBuilder();
          builder
              .create()
              .createLeftOperandAsArithmeticalOperation()
              .withNumber(2)
              .plus(2)
              .parent()
              .withOperator(ASTComparisonOperator.GREATER_OR_EQUALS)
              // .withRightOperandAsNumber(18);
              .withRightOperandAsProperty("user", "age");
          return builder.getModel();
        });
  }

  private static Stream<Arguments> simple_operation_greater_than() {
    return Stream.of(
        //            language      expected
        Arguments.of("javascript", "huml.GREATER_THAN((2.1+2.0), model.user.age)"),
        Arguments.of("java", "huml.GREATER_THAN((2.1+2.0), model.getUser().getAge())"),
        Arguments.of("csharp", "huml.GREATER_THAN((2.1+2.0), model.User.Age)"),
        Arguments.of("python", "huml.GREATER_THAN((2.1+2.0), model.user.age)"));
  }

  @ParameterizedTest(name = GTE.PARAM_TEST_NAME)
  @MethodSource()
  void simple_operation_greater_than(String language, String expected) throws Exception {
    GTE.execute(
        expected,
        language,
        p -> {
          ASTConditionBuilder builder = new ASTConditionBuilder();
          // ASTOperandArithmeticalBuilder builder = new ASTOperandArithmeticalBuilder();
          builder
              .create()
              .createLeftOperandAsArithmeticalOperation()
              .withNumber(2.1)
              .plus(2)
              .parent()
              .withOperator(ASTComparisonOperator.GREATER_THAN)
              // .withRightOperandAsNumber(18);
              .withRightOperandAsProperty("user", "age");
          return builder.getModel();
        });
  }

  private static Stream<Arguments> simple_operation_less_or_equals() {
    return Stream.of(
        //            language      expected
        Arguments.of("javascript", "huml.LESS_OR_EQUALS((2.0+2.0), model.user.age)"),
        Arguments.of("java", "huml.LESS_OR_EQUALS((2.0+2.0), model.getUser().getAge())"),
        Arguments.of("csharp", "huml.LESS_OR_EQUALS((2.0+2.0), model.User.Age)"),
        Arguments.of("python", "huml.LESS_OR_EQUALS((2.0+2.0), model.user.age)"));
  }

  @ParameterizedTest(name = GTE.PARAM_TEST_NAME)
  @MethodSource()
  void simple_operation_less_or_equals(String language, String expected) throws Exception {
    GTE.execute(
        expected,
        language,
        p -> {
          ASTConditionBuilder builder = new ASTConditionBuilder();
          // ASTOperandArithmeticalBuilder builder = new ASTOperandArithmeticalBuilder();
          builder
              .create()
              .createLeftOperandAsArithmeticalOperation()
              .withNumber(2)
              .plus(2)
              .parent()
              .withOperator(ASTComparisonOperator.LESS_OR_EQUALS)
              // .withRightOperandAsNumber(18);
              .withRightOperandAsProperty("user", "age");
          return builder.getModel();
        });
  }

  private static Stream<Arguments> simple_operation_is() {
    return Stream.of(
        //            language      expected
        Arguments.of("javascript", "huml.IS((2.0+2.0), model.user.age)"),
        Arguments.of("java", "huml.IS((2.0+2.0), model.getUser().getAge())"),
        Arguments.of("csharp", "huml.IS((2.0+2.0), model.User.Age)"),
        Arguments.of("python", "huml.IS((2.0+2.0), model.user.age)"));
  }

  @ParameterizedTest(name = GTE.PARAM_TEST_NAME)
  @MethodSource()
  void simple_operation_is(String language, String expected) throws Exception {
    GTE.execute(
        expected,
        language,
        p -> {
          ASTConditionBuilder builder = new ASTConditionBuilder();
          // ASTOperandArithmeticalBuilder builder = new ASTOperandArithmeticalBuilder();
          builder
              .create()
              .createLeftOperandAsArithmeticalOperation()
              .withNumber(2)
              .plus(2)
              .parent()
              .withOperator(ASTComparisonOperator.IS)
              // .withRightOperandAsNumber(18);
              .withRightOperandAsProperty("user", "age");
          return builder.getModel();
        });
  }

  private static Stream<Arguments> complex_operation() {
    return Stream.of(
        //            language      expected
        Arguments.of("javascript", "huml.NOT_EQUALS((2.0+10.0-5.0/2.0), 18.0)"),
        Arguments.of("java", "huml.NOT_EQUALS((2.0+10.0-5.0/2.0), 18.0)"),
        Arguments.of("csharp", "huml.NOT_EQUALS((2.0+10.0-5.0/2.0), 18.0)"),
        Arguments.of("python", "huml.NOT_EQUALS((2.0+10.0-5.0/2.0), 18.0)"));
  }

  @ParameterizedTest(name = GTE.PARAM_TEST_NAME)
  @MethodSource()
  void complex_operation(String language, String expected) throws Exception {
    GTE.execute(
        expected,
        language,
        p -> {
          ASTConditionBuilder builder = new ASTConditionBuilder();
          builder
              .create()
              .createLeftOperandAsArithmeticalOperation()
              .withNumber(2)
              .plus(10)
              .minus(5)
              .divide(2)
              .parent()
              .withOperator(ASTComparisonOperator.NOT_EQUALS)
              .withRightOperandAsNumber(18);
          return builder.getModel();
        });
  }

  private static Stream<Arguments> complex_operation_with_modulo() {
    return Stream.of(
        //            language      expected
        Arguments.of("javascript", "huml.NOT_EQUALS((2.0+10.0-5.0/2.0+10.0%3.0), 18.0)"),
        Arguments.of("java", "huml.NOT_EQUALS((2.0+10.0-5.0/2.0+10.0%3.0), 18.0)"),
        Arguments.of("csharp", "huml.NOT_EQUALS((2.0+10.0-5.0/2.0+10.0%3.0), 18.0)"),
        Arguments.of("python", "huml.NOT_EQUALS((2.0+10.0-5.0/2.0+10.0%3.0), 18.0)"));
  }

  @ParameterizedTest(name = GTE.PARAM_TEST_NAME)
  @MethodSource()
  void complex_operation_with_modulo(String language, String expected) throws Exception {
    GTE.execute(
        expected,
        language,
        p -> {
          ASTConditionBuilder builder = new ASTConditionBuilder();
          builder
              .create()
              .createLeftOperandAsArithmeticalOperation()
              .withNumber(2)
              .plus(10)
              .minus(5)
              .divide(2)
              .plus(10)
              .mod(3)
              .parent()
              .withOperator(ASTComparisonOperator.NOT_EQUALS)
              .withRightOperandAsNumber(18);
          return builder.getModel();
        });
  }

  private static Stream<Arguments> complex_operation_with_property() {
    return Stream.of(
        //            language      expected
        Arguments.of("javascript", "huml.NOT_EQUALS((2.0+model.user.age-5.0/2.0), 18.1)"),
        Arguments.of(
            "java",
            "huml.NOT_EQUALS((2.0+((Number)model.getUser().getAge()).doubleValue()-5.0/2.0), 18.1)"),
        Arguments.of("csharp", "huml.NOT_EQUALS((2.0+model.User.Age-5.0/2.0), 18.1)"),
        Arguments.of("python", "huml.NOT_EQUALS((2.0+model.user.age-5.0/2.0), 18.1)"));
  }

  @ParameterizedTest(name = GTE.PARAM_TEST_NAME)
  @MethodSource()
  void complex_operation_with_property(String language, String expected) throws Exception {
    GTE.execute(
        expected,
        language,
        p -> {
          ASTConditionBuilder builder = new ASTConditionBuilder();
          builder
              .create()
              .createLeftOperandAsArithmeticalOperation()
              .withNumber(2)
              .plus(new ASTOperandProperty(new ASTOperandProperty("user", "age")))
              .minus(5)
              .divide(2)
              .parent()
              .withOperator(ASTComparisonOperator.NOT_EQUALS)
              .withRightOperandAsNumber(18.1);
          return builder.getModel();
        });
  }

  private static Stream<Arguments> arithmetical_operation_with_property_and_group() {
    return Stream.of(
        //            language      expected
        Arguments.of("javascript", "huml.EQUALS((2.0*(model.Benutzer.Alter-18.0)), 18.0)"),
        Arguments.of("csharp", "huml.EQUALS((2.0*(model.Benutzer.Alter-18.0)), 18.0)"),
        Arguments.of(
            "java",
            "huml.EQUALS((2.0*(((Number)model.getBenutzer().getAlter()).doubleValue()-18.0)), 18.0)"),
        Arguments.of("python", "huml.EQUALS((2.0*(model.Benutzer.Alter-18.0)), 18.0)"));
  }

  @ParameterizedTest(name = GTE.PARAM_TEST_NAME)
  @MethodSource()
  void arithmetical_operation_with_property_and_group(String language, String expected)
      throws Exception {
    GTE.execute(
        expected,
        language,
        p -> {
          ASTConditionBuilder builder = new ASTConditionBuilder();
          builder
              .create()
              .createLeftOperandAsArithmeticalOperation()
              .withNumber(2)
              .multiplySuboperation()
              .withProperty("Benutzer", "Alter")
              .minus(18)
              .parentOperation()
              .parent()
              .withOperator(ASTComparisonOperator.EQUALS)
              .withRightOperandAsNumber(18);
          return builder.getModel();
        });
  }

  private static Stream<Arguments> arithmetical_operation_with_modulo() {
    return Stream.of(
        //            language      expected
        Arguments.of("javascript", "huml.EQUALS((2.0%5.0), 3.0)"),
        Arguments.of("csharp", "huml.EQUALS((2.0%5.0), 3.0)"),
        Arguments.of("java", "huml.EQUALS((2.0%5.0), 3.0)"),
        Arguments.of("python", "huml.EQUALS((2.0%5.0), 3.0)"));
  }

  @ParameterizedTest(name = GTE.PARAM_TEST_NAME)
  @MethodSource()
  void arithmetical_operation_with_modulo(String language, String expected) throws Exception {
    GTE.execute(
        expected,
        language,
        p -> {
          ASTConditionBuilder builder = new ASTConditionBuilder();
          builder
              .create()
              .createLeftOperandAsArithmeticalOperation()
              .withNumber(2)
              .mod(5)
              .parent()
              .withOperator(ASTComparisonOperator.EQUALS)
              .withRightOperandAsNumber(3);
          return builder.getModel();
        });
  }

  private static Stream<Arguments> arithmetical_operation_with_modulo_complex_usage() {
    return Stream.of(
        //            language      expected
        Arguments.of("javascript", "huml.EQUALS((2.0%age.GetValue(model)), 3.0)"),
        Arguments.of("csharp", "huml.EQUALS((2.0%age.GetValue(model)), 3.0)"),
        Arguments.of("java", "huml.EQUALS((2.0%((Number)age.GetValue(model)).doubleValue()), 3.0)"),
        Arguments.of("python", "huml.EQUALS((2.0%self.age.get_value(model)), 3.0)"));
  }

  @ParameterizedTest(name = GTE.PARAM_TEST_NAME)
  @MethodSource()
  void arithmetical_operation_with_modulo_complex_usage(String language, String expected)
      throws Exception {
    GTE.execute(
        expected,
        language,
        p -> {
          ASTVariableBuilder variableBuilder = new ASTVariableBuilder();
          variableBuilder.createVariable("age").withValueAsNumber(10);

          ASTConditionBuilder builder = new ASTConditionBuilder();
          builder
              .create()
              .createLeftOperandAsArithmeticalOperation()
              .withNumber(2)
              .mod(new ASTOperandArithmeticalVariable(new ASTOperandVariable("age")))
              .parent()
              .withOperator(ASTComparisonOperator.EQUALS)
              .withRightOperandAsNumber(3);
          return builder.getModel();
        });
  }

  private static Stream<Arguments> arithmetical_operation_with_modulo_and_property() {
    return Stream.of(
        //            language      expected
        Arguments.of("javascript", "huml.EQUALS((2.0%model.user.age), 3.0)"),
        Arguments.of("csharp", "huml.EQUALS((2.0%model.User.Age), 3.0)"),
        Arguments.of(
            "java", "huml.EQUALS((2.0%((Number)model.getUser().getAge()).doubleValue()), 3.0)"),
        Arguments.of("python", "huml.EQUALS((2.0%model.user.age), 3.0)"));
  }

  @ParameterizedTest(name = GTE.PARAM_TEST_NAME)
  @MethodSource()
  void arithmetical_operation_with_modulo_and_property(String language, String expected)
      throws Exception {
    GTE.execute(
        expected,
        language,
        p -> {
          ASTConditionBuilder builder = new ASTConditionBuilder();
          builder
              .create()
              .createLeftOperandAsArithmeticalOperation()
              .withNumber(2)
              // .mod(new ASTOperandProperty(new ASTOperandProperty("user","age")))
              .mod(new ASTOperandArithmeticalPropertyItem(new ASTOperandProperty("user", "age")))
              .parent()
              .withOperator(ASTComparisonOperator.EQUALS)
              .withRightOperandAsNumber(3);
          return builder.getModel();
        });
  }

  private static Stream<Arguments> arithmetical_operation_with_modulo_property_variable() {
    return Stream.of(
        //            language      expected
        Arguments.of("javascript", "huml.EQUALS((model.user.investment%age.GetValue(model)), 3.1)"),
        Arguments.of("csharp", "huml.EQUALS((model.User.Investment%age.GetValue(model)), 3.1)"),
        Arguments.of(
            "java",
            "huml.EQUALS((((Number)model.getUser().getInvestment()).doubleValue()%((Number)age.GetValue(model)).doubleValue()), 3.1)"),
        Arguments.of(
            "python", "huml.EQUALS((model.user.investment%self.age.get_value(model)), 3.1)"));
  }

  @ParameterizedTest(name = GTE.PARAM_TEST_NAME)
  @MethodSource()
  void arithmetical_operation_with_modulo_property_variable(String language, String expected)
      throws Exception {
    GTE.execute(
        expected,
        language,
        p -> {
          ASTVariableBuilder variableBuilder = new ASTVariableBuilder();
          variableBuilder.createVariable("age").withValueAsNumber(10);

          ASTConditionBuilder builder = new ASTConditionBuilder();
          builder
              .create()
              .createLeftOperandAsArithmeticalOperation()
              .withProperty("user", "investment")
              .mod(new ASTOperandArithmeticalVariable(new ASTOperandVariable("age")))
              .parent()
              .withOperator(ASTComparisonOperator.EQUALS)
              .withRightOperandAsNumber(3.1);
          return builder.getModel();
        });
  }

  private static Stream<Arguments> arithmetical_operation_with_modulo_variable_property() {
    return Stream.of(
        //            language      expected
        Arguments.of("javascript", "huml.EQUALS((age.GetValue(model)%model.user.investment), 3.0)"),
        Arguments.of("csharp", "huml.EQUALS((age.GetValue(model)%model.User.Investment), 3.0)"),
        Arguments.of(
            "java",
            "huml.EQUALS((((Number)age.GetValue(model)).doubleValue()%((Number)model.getUser().getInvestment()).doubleValue()), 3.0)"),
        Arguments.of(
            "python", "huml.EQUALS((self.age.get_value(model)%model.user.investment), 3.0)"));
  }

  @ParameterizedTest(name = GTE.PARAM_TEST_NAME)
  @MethodSource()
  void arithmetical_operation_with_modulo_variable_property(String language, String expected)
      throws Exception {
    GTE.execute(
        expected,
        language,
        p -> {
          ASTVariableBuilder variableBuilder = new ASTVariableBuilder();
          variableBuilder.createVariable("age").withValueAsNumber(10);

          ASTConditionBuilder builder = new ASTConditionBuilder();
          builder
              .create()
              .createLeftOperandAsArithmeticalOperation()
              .withVariable(new ASTOperandVariable("age"))
              .mod(
                  new ASTOperandArithmeticalPropertyItem(
                      new ASTOperandProperty("user", "investment")))
              .parent()
              .withOperator(ASTComparisonOperator.EQUALS)
              .withRightOperandAsNumber(3);
          return builder.getModel();
        });
  }

  private static Stream<Arguments>
      arithmetical_operation_with_modulo_variable_property_equals_variable() {
    return Stream.of(
        //            language      expected
        Arguments.of(
            "javascript",
            "huml.NOT_EMPTY((age.GetValue(model)%model.user.investment), revenue.GetValue(model))"),
        Arguments.of(
            "csharp",
            "huml.NOT_EMPTY((age.GetValue(model)%model.User.Investment), revenue.GetValue(model))"),
        Arguments.of(
            "java",
            "huml.NOT_EMPTY((((Number)age.GetValue(model)).doubleValue()%((Number)model.getUser().getInvestment()).doubleValue()), revenue.GetValue(model))"),
        Arguments.of(
            "python",
            "huml.NOT_EMPTY((self.age.get_value(model)%model.user.investment), self.revenue.get_value(model))"));
  }

  @ParameterizedTest(name = GTE.PARAM_TEST_NAME)
  @MethodSource()
  void arithmetical_operation_with_modulo_variable_property_equals_variable(
      String language, String expected) throws Exception {
    GTE.execute(
        expected,
        language,
        p -> {
          ASTVariableBuilder variableBuilder = new ASTVariableBuilder();
          variableBuilder.createVariable("age").withValueAsNumber(10);

          variableBuilder.createVariable("revenue").withValueAsNumber(100);

          ASTConditionBuilder builder = new ASTConditionBuilder();
          builder
              .create()
              .createLeftOperandAsArithmeticalOperation()
              .withVariable(new ASTOperandVariable("age"))
              .mod(
                  new ASTOperandArithmeticalPropertyItem(
                      new ASTOperandProperty("user", "investment")))
              .parent()
              .withOperator(ASTComparisonOperator.NOT_EMPTY)
              // .withRightOperandAsNumber(3);
              .withRightOperandAsVariable("revenue");
          return builder.getModel();
        });
  }

  private static Stream<Arguments>
      arithmetical_operation_with_modulo_variable_property_equals_property() {
    return Stream.of(
        //            language      expected
        Arguments.of(
            "javascript",
            "huml.EXISTS((age.GetValue(model)%model.user.investment), model.user.revenue)"),
        Arguments.of(
            "csharp",
            "huml.EXISTS((age.GetValue(model)%model.User.Investment), model.User.Revenue)"),
        Arguments.of(
            "java",
            "huml.EXISTS((((Number)age.GetValue(model)).doubleValue()%((Number)model.getUser().getInvestment()).doubleValue()), model.getUser().getRevenue())"),
        Arguments.of(
            "python",
            "huml.EXISTS((self.age.get_value(model)%model.user.investment), model.user.revenue)"));
  }

  @ParameterizedTest(name = GTE.PARAM_TEST_NAME)
  @MethodSource()
  void arithmetical_operation_with_modulo_variable_property_equals_property(
      String language, String expected) throws Exception {
    GTE.execute(
        expected,
        language,
        p -> {
          ASTVariableBuilder variableBuilder = new ASTVariableBuilder();
          variableBuilder.createVariable("age").withValueAsNumber(10);

          ASTConditionBuilder builder = new ASTConditionBuilder();
          builder
              .create()
              .createLeftOperandAsArithmeticalOperation()
              .withVariable(new ASTOperandVariable("age"))
              .mod(
                  new ASTOperandArithmeticalPropertyItem(
                      new ASTOperandProperty("user", "investment")))
              .parent()
              .withOperator(ASTComparisonOperator.EXISTS)
              .withRightOperandAsProperty("user", "revenue");
          return builder.getModel();
        });
  }

  private static Stream<Arguments>
      simple_operation_with_single_nested_suboperations_right_aligned() {
    return Stream.of(
        //            language      expected
        Arguments.of("javascript", "huml.NOT_EQUALS((1.0*(2.0+3.0)), 18.0)"),
        Arguments.of("java", "huml.NOT_EQUALS((1.0*(2.0+3.0)), 18.0)"),
        Arguments.of("csharp", "huml.NOT_EQUALS((1.0*(2.0+3.0)), 18.0)"),
        Arguments.of("python", "huml.NOT_EQUALS((1.0*(2.0+3.0)), 18.0)"));
  }

  @ParameterizedTest(name = GTE.PARAM_TEST_NAME)
  @MethodSource()
  void simple_operation_with_single_nested_suboperations_right_aligned(
      String language, String expected) throws Exception {
    GTE.execute(
        expected,
        language,
        p -> {
          ASTConditionBuilder builder = new ASTConditionBuilder();
          // ASTOperandArithmeticalBuilder builder = new ASTOperandArithmeticalBuilder();
          builder
              .create()
              .createLeftOperandAsArithmeticalOperation()
              .withNumber(1)
              .subOperation(ASTArithmeticalOperator.Multiplication)
              .withNumber(2)
              .withNumber(3, ASTArithmeticalOperator.Addition)
              .parentOperation()
              .parent()
              .withOperator(ASTComparisonOperator.NOT_EQUALS)
              .withRightOperandAsNumber(18);
          return builder.getModel();
        });
  }

  private static Stream<Arguments>
      simple_operation_with_single_nested_suboperations_left_aligned() {
    return Stream.of(
        //            language      expected
        Arguments.of("javascript", "huml.NOT_EQUALS(((1.0+2.0)*3.0), 18.0)"),
        Arguments.of("java", "huml.NOT_EQUALS(((1.0+2.0)*3.0), 18.0)"),
        Arguments.of("csharp", "huml.NOT_EQUALS(((1.0+2.0)*3.0), 18.0)"),
        Arguments.of("python", "huml.NOT_EQUALS(((1.0+2.0)*3.0), 18.0)"));
  }

  @ParameterizedTest(name = GTE.PARAM_TEST_NAME)
  @MethodSource()
  void simple_operation_with_single_nested_suboperations_left_aligned(
      String language, String expected) throws Exception {
    GTE.execute(
        expected,
        language,
        p -> {
          ASTConditionBuilder builder = new ASTConditionBuilder();
          // ASTOperandArithmeticalBuilder builder = new ASTOperandArithmeticalBuilder();
          builder
              .create()
              .createLeftOperandAsArithmeticalOperation()
              .subOperation()
              .withNumber(1)
              .withNumber(2, ASTArithmeticalOperator.Addition)
              .parentOperation()
              .withNumber(3, ASTArithmeticalOperator.Multiplication)
              .parent()
              .withOperator(ASTComparisonOperator.NOT_EQUALS)
              .withRightOperandAsNumber(18);
          return builder.getModel();
        });
  }

  private static Stream<Arguments>
      complex_operation_with_double_nested_suboperations_right_right_aligned() {
    return Stream.of(
        //            language      expected
        Arguments.of("javascript", "huml.NOT_EQUALS((1.0+(2.0*(3.0+4.0))), 18.0)"),
        Arguments.of("java", "huml.NOT_EQUALS((1.0+(2.0*(3.0+4.0))), 18.0)"),
        Arguments.of("csharp", "huml.NOT_EQUALS((1.0+(2.0*(3.0+4.0))), 18.0)"),
        Arguments.of("python", "huml.NOT_EQUALS((1.0+(2.0*(3.0+4.0))), 18.0)"));
  }

  @ParameterizedTest(name = GTE.PARAM_TEST_NAME)
  @MethodSource()
  void complex_operation_with_double_nested_suboperations_right_right_aligned(
      String language, String expected) throws Exception {
    GTE.execute(
        expected,
        language,
        p -> {
          ASTConditionBuilder builder = new ASTConditionBuilder();
          // ASTOperandArithmeticalBuilder builder = new ASTOperandArithmeticalBuilder();
          builder
              .create()
              .createLeftOperandAsArithmeticalOperation()
              .withNumber(1)
              .subOperation(ASTArithmeticalOperator.Addition)
              .withNumber(2)
              .subOperation(ASTArithmeticalOperator.Multiplication)
              .withNumber(3)
              .withNumber(4, ASTArithmeticalOperator.Addition)
              .parentOperation()
              .parentOperation()
              .parent()
              .withOperator(ASTComparisonOperator.NOT_EQUALS)
              .withRightOperandAsNumber(18);
          return builder.getModel();
        });
  }

  private static Stream<Arguments>
      complex_operation_with_double_nested_suboperations_right_left_aligned() {
    return Stream.of(
        //            language      expected
        Arguments.of("javascript", "huml.NOT_EQUALS((1.0+((2.0*3.0)+4.0)), 18.0)"),
        Arguments.of("java", "huml.NOT_EQUALS((1.0+((2.0*3.0)+4.0)), 18.0)"),
        Arguments.of("csharp", "huml.NOT_EQUALS((1.0+((2.0*3.0)+4.0)), 18.0)"),
        Arguments.of("python", "huml.NOT_EQUALS((1.0+((2.0*3.0)+4.0)), 18.0)"));
  }

  @ParameterizedTest(name = GTE.PARAM_TEST_NAME)
  @MethodSource()
  void complex_operation_with_double_nested_suboperations_right_left_aligned(
      String language, String expected) throws Exception {
    GTE.execute(
        expected,
        language,
        p -> {
          ASTConditionBuilder builder = new ASTConditionBuilder();
          // ASTOperandArithmeticalBuilder builder = new ASTOperandArithmeticalBuilder();
          builder
              .create()
              .createLeftOperandAsArithmeticalOperation()
              .withNumber(1)
              .subOperation(ASTArithmeticalOperator.Addition)
              .subOperation()
              .withNumber(2)
              .withNumber(3, ASTArithmeticalOperator.Multiplication)
              .parentOperation()
              .withNumber(4, ASTArithmeticalOperator.Addition)
              .parentOperation()
              .parent()
              .withOperator(ASTComparisonOperator.NOT_EQUALS)
              .withRightOperandAsNumber(18);
          return builder.getModel();
        });
  }

  private static Stream<Arguments>
      complex_operation_with_double_nested_suboperations_left_right_aligned() {
    // ((1 + (2 * 3)) + 4)
    return Stream.of(
        //            language      expected
        Arguments.of("javascript", "huml.NOT_EQUALS(((1.0+(2.0*3.0))+4.0), 18.0)"),
        Arguments.of("java", "huml.NOT_EQUALS(((1.0+(2.0*3.0))+4.0), 18.0)"),
        Arguments.of("csharp", "huml.NOT_EQUALS(((1.0+(2.0*3.0))+4.0), 18.0)"),
        Arguments.of("python", "huml.NOT_EQUALS(((1.0+(2.0*3.0))+4.0), 18.0)"));
  }

  @ParameterizedTest(name = GTE.PARAM_TEST_NAME)
  @MethodSource()
  void complex_operation_with_double_nested_suboperations_left_right_aligned(
      String language, String expected) throws Exception {
    GTE.execute(
        expected,
        language,
        p -> {
          ASTConditionBuilder builder = new ASTConditionBuilder();
          // ASTOperandArithmeticalBuilder builder = new ASTOperandArithmeticalBuilder();
          builder
              .create()
              .createLeftOperandAsArithmeticalOperation()
              .subOperation()
              .withNumber(1)
              .subOperation(ASTArithmeticalOperator.Addition)
              .withNumber(2)
              .withNumber(3, ASTArithmeticalOperator.Multiplication)
              .parentOperation()
              .parentOperation()
              .withNumber(4, ASTArithmeticalOperator.Addition)
              .parent()
              .withOperator(ASTComparisonOperator.NOT_EQUALS)
              .withRightOperandAsNumber(18);
          return builder.getModel();
        });
  }

  private static Stream<Arguments>
      complex_operation_with_double_nested_suboperations_left_left_aligned() {
    // ((1 + (2 * 3)) + 4)
    return Stream.of(
        //            language      expected
        Arguments.of("javascript", "huml.NOT_EQUALS((((1.0+2.0)*3.0)+4.0), 18.0)"),
        Arguments.of("java", "huml.NOT_EQUALS((((1.0+2.0)*3.0)+4.0), 18.0)"),
        Arguments.of("csharp", "huml.NOT_EQUALS((((1.0+2.0)*3.0)+4.0), 18.0)"),
        Arguments.of("python", "huml.NOT_EQUALS((((1.0+2.0)*3.0)+4.0), 18.0)"));
  }

  @ParameterizedTest(name = GTE.PARAM_TEST_NAME)
  @MethodSource()
  void complex_operation_with_double_nested_suboperations_left_left_aligned(
      String language, String expected) throws Exception {
    GTE.execute(
        expected,
        language,
        p -> {
          ASTConditionBuilder builder = new ASTConditionBuilder();
          // ASTOperandArithmeticalBuilder builder = new ASTOperandArithmeticalBuilder();
          builder
              .create()
              .createLeftOperandAsArithmeticalOperation()
              .subOperation()
              .subOperation()
              .withNumber(1)
              .withNumber(2, ASTArithmeticalOperator.Addition)
              .parentOperation()
              .withNumber(3, ASTArithmeticalOperator.Multiplication)
              .parentOperation()
              .withNumber(4, ASTArithmeticalOperator.Addition)
              .parent()
              .withOperator(ASTComparisonOperator.NOT_EQUALS)
              .withRightOperandAsNumber(18);
          return builder.getModel();
        });
  }

  private static Stream<Arguments>
      arithmetical_operation_with_variable_and_property_in_single_nested_suboperation() {
    return Stream.of(
        //            language      expected
        Arguments.of(
            "javascript", "huml.EXISTS((10.0*(age.GetValue(model)+model.user.investment)), 777.0)"),
        Arguments.of(
            "csharp", "huml.EXISTS((10.0*(age.GetValue(model)+model.User.Investment)), 777.0)"),
        Arguments.of(
            "java",
            "huml.EXISTS((10.0*(((Number)age.GetValue(model)).doubleValue()+((Number)model.getUser().getInvestment()).doubleValue())), 777.0)"),
        Arguments.of(
            "python",
            "huml.EXISTS((10.0*(self.age.get_value(model)+model.user.investment)), 777.0)"));
  }

  @ParameterizedTest(name = GTE.PARAM_TEST_NAME)
  @MethodSource()
  void arithmetical_operation_with_variable_and_property_in_single_nested_suboperation(
      String language, String expected) throws Exception {
    GTE.execute(
        expected,
        language,
        p -> {
          ASTVariableBuilder variableBuilder = new ASTVariableBuilder();
          variableBuilder.createVariable("age").withValueAsNumber(10);

          ASTConditionBuilder builder = new ASTConditionBuilder();
          builder
              .create()
              .withOperator(ASTComparisonOperator.EXISTS)
              .createLeftOperandAsArithmeticalOperation()
              .withNumber(10)
              .subOperation(ASTArithmeticalOperator.Multiplication)
              .withVariable("age")
              .withProperty(ASTArithmeticalOperator.Addition, "user", "investment")
              .parentOperation()
              .parent()
              .withRightOperandAsNumber(777);
          return builder.getModel();
        });
  }

  // todo 01.08.19 jgeske add special case in templates to generate Math.pow() for java and
  // Math.Pow() for csharp, Math.pow() for javascript
  private static Stream<Arguments> arithmetical_operation_power_of() {
    return Stream.of(
        //            language      expected
        Arguments.of("javascript", "huml.NOT_EQUALS(Math.pow(2.0,2.0), 18.0)"),
        Arguments.of("java", "huml.NOT_EQUALS(Math.pow(2.0, 2.0), 18.0)"),
        Arguments.of("csharp", "huml.NOT_EQUALS(Math.Pow(2.0^2.0), 18.0)"),
        Arguments.of("python", "huml.NOT_EQUALS((2.0^2.0), 18.0)"));
  }

  @Disabled
  @ParameterizedTest(name = GTE.PARAM_TEST_NAME)
  @MethodSource()
  void arithmetical_operation_power_of(String language, String expected) throws Exception {
    GTE.execute(
        expected,
        language,
        p -> {
          ASTConditionBuilder builder = new ASTConditionBuilder();
          // ASTOperandArithmeticalBuilder builder = new ASTOperandArithmeticalBuilder();
          builder
              .create()
              .createLeftOperandAsArithmeticalOperation()
              .withNumber(2)
              .power_of(2)
              .parent()
              .withOperator(ASTComparisonOperator.NOT_EQUALS)
              .withRightOperandAsNumber(18);
          return builder.getModel();
        });
  }
}
