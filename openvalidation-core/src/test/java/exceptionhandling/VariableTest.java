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

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class VariableTest {
  ExceptionRunner runner = new ExceptionRunner();

  @ParameterizedTest
  @ValueSource(
      strings = {
        "hallo AS",
        "hallo AS\n",
        "hallo AS\r\n",
        "hallo AS \n",
        " hallo AS \n ",
        "\n hallo AS \n ",
        "   \r\n hallo AS \n ",
        "AS \n"
      })
  public void variable_name_should_not_be_null(String rule) throws Exception {
    runner.run(
        rule,
        r -> {
          r.containsValidationMessage("the name of Variable should not be empty");
        });
  }

  @ParameterizedTest
  @ValueSource(
      strings = {"AS name", "\nAS name", " AS\n ame", "\r\n AS name", " \n AS name ", "AS name"})
  public void variable_value_should_not_be_null(String rule) throws Exception {
    runner.run(
        rule,
        r -> {
          r.containsValidationMessage("the value of Variable should not be empty");
        });
  }
}
