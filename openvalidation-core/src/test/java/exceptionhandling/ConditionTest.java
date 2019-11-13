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

package exceptionhandling;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class ConditionTest {
  ExceptionRunner runner = new ExceptionRunner();

  @ParameterizedTest
  @ValueSource(
      strings = {
        "a LESS AS b",
        "a \n LESS \n AS b",
        "IF a EQUALS THEN bla",
        "IF a EQUALS \n  THEN bla",
        "a MUST be LESS "
      })
  public void missing_left_operand(String rule) throws Exception {
    runner.run(
        rule,
        r -> {
          r.containsValidationMessage("missing right operand in condition");
        });
  }

  @ParameterizedTest
  @ValueSource(
      strings = {
        " LESS a AS b",
        " LESS \n a \n AS b",
        "IF LESS a THEN bla",
        "IF \n LESS a THEN bla"
      })
  public void missing_right_operand(String rule) throws Exception {
    runner.run(
        rule,
        r -> {
          r.containsValidationMessage("missing left operand in condition");
        });
  }

  @ParameterizedTest
  @ValueSource(strings = {"IF a THEN b"})
  public void invalid_condition(String rule) throws Exception {
    runner.run(
        rule,
        r -> {
          r.containsValidationMessage(
              "invalid condition. missing comparison operator and operand.");
        });
  }

  @ParameterizedTest
  @ValueSource(
      strings = {
        "a EQUALS 1 AS c",
        "1 EQUALS a AS c",
        " \n 1 EQUALS \n a \n AS c",
        " a AS b \n\n 1 EQUALS b AS c",
        "1 EQUALS 0001 AS c",
      })
  public void condition_contains_different_data_types(String rule) throws Exception {
    runner.run(
        rule,
        r -> {
          r.containsValidationMessage("comparison contains different DataTypes.");
        });
  }

  @ParameterizedTest
  @ValueSource(strings = {"a EQUALS a AS c", "true EQUALS false AS c", "1 EQUALS 2 AS c"})
  public void at_least_one_operand_should_not_be_static(String rule) throws Exception {
    runner.run(
        rule,
        r -> {
          r.containsValidationMessage("at least one operand in comparison should not be static");
        });
  }

  @Test
  void wrong_comparison_type_of_first_function() throws Exception {
    // check if validation of parameters is triggered
    runner.run(
        "FIRST FROM numbers AS firstNumber\n\n" +
                "IF firstNumber IS Hallo THEN error",
        "{numbers: [1, 2, 3]}",
        r -> r.containsValidationMessage("comparison contains different DataTypes. \n" +
                "left operand is of type: 'Decimal' and right operand is of type: 'String'"));
  }

  @Test
  void wrong_comparison_type_of_last_function() throws Exception {
    // check if validation of parameters is triggered
    runner.run(
        "LAST FROM numbers AS firstNumber\n\n" +
                "IF firstNumber IS Hallo THEN error",
        "{numbers: [1, 2, 3]}",
        r -> r.containsValidationMessage("comparison contains different DataTypes. \n" +
                "left operand is of type: 'Decimal' and right operand is of type: 'String'"));
  }
}
