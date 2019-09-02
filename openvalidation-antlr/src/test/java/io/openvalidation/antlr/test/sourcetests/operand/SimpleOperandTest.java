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

package io.openvalidation.antlr.test.sourcetests.operand;

import static io.openvalidation.common.unittesting.astassertion.ModelRootAssertion.assertRules;
import static io.openvalidation.common.unittesting.astassertion.ModelRootAssertion.assertVariable;

import io.openvalidation.antlr.ANTLRExecutor;
import io.openvalidation.common.ast.ASTModel;
import io.openvalidation.common.utils.GrammarBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class SimpleOperandTest {

  // ast operand static
  @ParameterizedTest
  @ValueSource(strings = {"true", "  true", "true   ", "  true    ", " true\n", "true\r\n"})
  void test_operand_static_bool_true(String staticString) throws Exception {
    String input = GrammarBuilder.create().with(staticString).AS("variable").getText();

    String expectedSource = GrammarBuilder.create().with(staticString).getText();

    ASTModel ast = ANTLRExecutor.run(input);

    assertVariable(ast)
        .first()
        // .hasNumber(Double.parseDouble(staticString))
        .string()
        .hasOriginalSource(staticString.trim())
        .hasPreprocessedSource(expectedSource);
  }

  // ast operand static
  @ParameterizedTest
  @ValueSource(strings = {"false", "  false", "false   ", "  false    ", " false\n", "false\r\n"})
  void test_operand_static_bool_false(String staticString) throws Exception {
    String input = GrammarBuilder.create().with(staticString).AS("variable").getText();

    String expectedSource = GrammarBuilder.create().with(staticString).getText();

    ASTModel ast = ANTLRExecutor.run(input);

    assertVariable(ast)
        .first()
        // .hasNumber(Double.parseDouble(staticString))
        .string()
        .hasOriginalSource(staticString.trim())
        .hasPreprocessedSource(expectedSource);
  }

  // ast operand static string
  @ParameterizedTest
  @ValueSource(strings = {"wolf", "wolf wolf", "wölf", "u n  f      a ll"})
  void test_operand_static_string(String staticString) throws Exception {
    String input = GrammarBuilder.create().with(staticString).AS("variable").getText();

    String expectedSource = GrammarBuilder.create().with(staticString).getText();

    ASTModel ast = ANTLRExecutor.run(input);

    assertVariable(ast)
        .first()
        // .hasNumber(Double.parseDouble(staticString))
        .string()
        .hasOriginalSource(staticString)
        .hasPreprocessedSource(expectedSource);
  }

  // ast operand static number
  @ParameterizedTest
  @ValueSource(strings = {"5", "15", "1.5", "-1", "-1.5", "-15"})
  void test_operand_static_number(String staticNumber) throws Exception {
    String input = GrammarBuilder.create().with(staticNumber).AS("variable").getText();

    String expectedSource = GrammarBuilder.create().with(staticNumber).getText();

    ASTModel ast = ANTLRExecutor.run(input);

    assertVariable(ast)
        .first()
        // .hasNumber(Double.parseDouble(staticNumber))
        .number()
        .hasOriginalSource(staticNumber)
        .hasPreprocessedSource(expectedSource);
  }

  // ast operand variable

  @ParameterizedTest
  @ValueSource(strings = {"varName", "valName", "space name", "s l i c e d  n a m e", "glyphnäme"})
  void test_operand_variable(String variableName) throws Exception {
    String input =
        GrammarBuilder.create()
            .with(1)
            .AS(variableName)
            .PARAGRAPH()
            .with(variableName)
            .AS(variableName + "2")
            .getText();

    String expectedSource = GrammarBuilder.create().with(variableName).getText();

    ASTModel ast = ANTLRExecutor.run(input);

    assertVariable(ast)
        .second()
        // .hasNumber(Double.parseDouble(staticNumber))
        .variable()
        .hasOriginalSource(variableName)
        .hasPreprocessedSource(expectedSource);
  }

  // ast operand function

  @ParameterizedTest
  @ValueSource(
      strings = {"customer.price", "chicken.egg", "world.place", "something else entirely"})
  void test_sum_of_variable(String paramName) throws Exception {

    String input =
        GrammarBuilder.create()
            .SUMOF(paramName)
            .AS("value")
            .getText(); // "SUM OF customer.price AS value";

    String expectedSource = GrammarBuilder.create().SUMOF(paramName).getText();
    //        PreProcessorUtils.preProcStr2Sysout(input, Locale("en"));

    ASTModel ast = ANTLRExecutor.run(input);

    assertVariable(ast)
        .first()
        .function()
        .hasPreprocessedSource(expectedSource)
        .parameters()
        .first()
        .hasOriginalSource(paramName);
  }

  // ast operand array

  @Test
  void test_one_of_array_as_string() throws Exception {
    String input =
        GrammarBuilder.createRule()
            .with("name")
            .AT_LEAST_ONE_OF("Jimmy, Donald, Boris")
            .THEN()
            .with("error")
            .getText();

    ASTModel ast = ANTLRExecutor.run(input);

    assertRules(ast)
        .hasSizeOf(1)
        .first()
        .condition()
        .hasOriginalSource("name ONE_OF Jimmy, Donald, Boris");
  }
}
