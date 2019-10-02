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

import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class RuleTest {
  @BeforeAll
  public static void setLoggingLevel() {

    prevLevel = Logger.getGlobal().getLevel();
    Logger.getGlobal().setLevel(Level.OFF);
  }

  @AfterAll
  public static void resetLoggingLevel() {
    Logger.getGlobal().setLevel(prevLevel);
  }

  static Level prevLevel;

  ExceptionRunner runner = new ExceptionRunner();

  @ParameterizedTest
  @ValueSource(strings = {"THEN", " THEN a", "\nTHEN a", "\n \n THEN a", "a\r\n\n THEN b"})
  public void missing_condition(String rule) throws Exception {
    runner.run(
        rule,
        r -> {
          r.containsValidationMessage("a Rule should contains at least one condition");
        });
  }

  @ParameterizedTest
  @ValueSource(
      strings = {
        "IF 1 THEN",
        "IF 1 ",
        "IF 1 \n i'm an unrecognized error!",
        "IF 1 THEN   \n ",
        "IF 1 THEN   \n\n a",
        "a \n\n IF 1 THEN   \n\n a",
        " 1 THEN",
        " 1 THEN \n",
        "a \n\n 1 THEN \n\n b ",
        "3 AS a \n\n IF a EQUAL 3 WITH ERROR",
        "3 AS a \n\n if a equals 2 or",
      })
  public void missing_error(String rule) throws Exception {
    runner.run(
        rule,
        r -> {
          r.containsValidationMessage("a Rule should contains an error");
        });
  }

  @ParameterizedTest
  @ValueSource(
      strings = {
        "MUST a",
        " MUST a",
        " \n MUST a",
        " \n\n MUST a",
        "tet AS a \n\n MUST a",
      })
  public void rule_should_not_be_empty_before(String rule)
      throws Exception { // a lot of error stack traces
    runner.run(
        rule,
        r -> {
          r.containsValidationMessage("a Rule should not be empty before");
        });
  }

  @ParameterizedTest
  @ValueSource(
      strings = {
        "a MUST",
        "a MUST ",
        "a MUST \n ",
        "a MUST  \n\n ",
        "a MUST  \n\n test AS b",
      })
  public void rule_should_not_be_empty_after(String rule) throws Exception {
    runner.run(
        rule,
        r -> {
          r.containsValidationMessage("a Rule should not be empty after");
        });
  }

  @ParameterizedTest
  @ValueSource(
      strings = {
        "MUST",
        " MUST NOT ",
        " \n MUST \n ",
        "  \n\n  HAVE  \n\n ",
        "  test AS b a \n\n MUST  \n\n test AS b",
      })
  public void rule_should_not_be_empty_before_and_after(String rule) throws Exception {
    runner.run(
        rule,
        r -> {
          r.containsValidationMessage("a Rule should not be empty before and after");
        });
  }

  @Test
  public void should_get_2_errors_from_different_rules() throws Exception {
    String input = "MUST a\n\n" + "a MUST";

    runner.run(
        input,
        r -> {
          r.containsValidationMessage("a Rule should not be empty before")
              .atPosition(0)
              .parent()
              .containsValidationMessage("a Rule should not be empty after")
              .atPosition(1);
        });
  }

  @Test
  public void should_get_2_errors_from_different_rules2() throws Exception {
    String input = "a MUST   \n\n MUST a";

    runner.run(
        input,
        r -> {
          r.containsValidationMessage("a Rule should not be empty after")
              .atPosition(0)
              .parent()
              .containsValidationMessage("a Rule should not be empty befor")
              .atPosition(1);
        });
  }

  @ParameterizedTest
  @ValueSource(
      strings = {
        "3 AS a \n\n IF a EQUAL 3 THEN abc WITH ERROR",
      })
  public void error_code_should_not_be_empty(String rule) throws Exception {
    runner.run(
        rule,
        r -> {
          r.containsValidationMessage("error code should not be empty");
        });
  }

  @ParameterizedTest
  @ValueSource(
      strings = {
        "IF something EXISTS THEN throw an error",
        "IF something EXISTS at all THEN throw an error",
        "something SHOULD EXIST",
        "something SHOULD definitely EXIST",
        "something SHOULD definitely EXIST under all circumstances",
      })
  public void exists_operator_on_static_string_should_throw_validation_exception(String rule)
      throws Exception {
    runner.run(
        rule,
        r -> {
          r.containsValidationMessage(
              "Exists-operator cannot be used on plain text. Property required instead.");
        });
  }

  @ParameterizedTest
  @ValueSource(
      strings = {
        "IF 100 EXISTS THEN throw an error",
        "IF 100 EXISTS at all THEN throw an error",
        "100 SHOULD EXIST",
        "100 SHOULD definitely EXIST",
        "100 SHOULD definitely EXIST under all circumstances",
      })
  public void exists_operator_on_static_number_should_throw_validation_exception(String rule)
      throws Exception {
    runner.run(
        rule,
        r -> {
          r.containsValidationMessage(
              "Exists-operator cannot be used on numbers. Property required instead.");
        });
  }

  @Test
  public void incomplete_condition() throws Exception {
    runner.run(
        "IF the Name NOT IS THEN error",
        "{Name: Peter}",
        r -> {
          r.containsValidationMessage("missing right operand in condition.");
        });
  }

  @Test
  public void condition_group_with_incomplete_condition_and_decimal_property() throws Exception {
    runner.run(
        "IF year SMALLER THAN another_int AND age THEN error",
        "{year: 1234, another_int: 1234, age: 12}",
        r -> {
          r.containsValidationMessage("invalid condition. missing comparison operator and operand.");
        });
  }
}
