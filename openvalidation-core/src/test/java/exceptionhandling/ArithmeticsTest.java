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

class ArithmeticsTest {

  ExceptionRunner runner = new ExceptionRunner();

  @ParameterizedTest
  @ValueSource(strings = {"+ AS c", " +  AS c", "+ AS c", "\n + \n AS c"})
  void missing_all_values(String rule) throws Exception {
    runner.run(
        rule,
        r -> {
          r.containsValidationMessage("missing all values in arithmetical operation");
        });
  }

  @ParameterizedTest
  @ValueSource(strings = {"1 +  AS c", "1 + 2 +  AS c", "\n1 + \n2\n + AS c"})
  void missing_arithmetical_operand(String rule) throws Exception {
    runner.run(
        rule,
        r -> {
          r.containsValidationMessage("missing value in arithmetical operation");
        });
  }

  @ParameterizedTest
  @ValueSource(strings = {"a + 1 AS c", "1 + a AS c", "\n1 + \na\n AS c"})
  void all_operands_of_arithmetical_expression_should_be_of_type_number(String rule)
      throws Exception {
    runner.run(
        rule,
        r -> {
          r.containsValidationMessage("all values of an arithmetical operation should be numbers");
        });
  }

  //    @ParameterizedTest
  //    @ValueSource(strings = {
  //            "1  2 AS c"
  //    })
  //    void missing_arithmetical_operator(String rule) throws Exception {
  //        run(rule, r -> {
  //            r.containsValidationMessage("missing arithmetical operator");
  //        });
  //    }
}
