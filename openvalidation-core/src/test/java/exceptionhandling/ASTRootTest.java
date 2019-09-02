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

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

public class ASTRootTest {

  ExceptionRunner runner = new ExceptionRunner();

  @ParameterizedTest
  @ValueSource(strings = {"", " ", "\n", "\n \n ", " \r\n ", " \n \r\n"})
  public void empty_rule_set_erorr(String rule) throws Exception {
    runner.run(
        rule,
        r -> {
          r.containsErrorMessage("the Rule Set should not be empty");
        });
  }

  @ParameterizedTest
  @ValueSource(strings = {"braaaaa", " braaaaa ", " braaaaa \r\n braaaa", " braaaaa \n\n braaaa"})
  public void invalid_rule_set_content_erorr(String rule) throws Exception {
    runner.run(
        rule,
        r -> {
          r.containsValidationMessage("the Rule Set has invalid content");
        });
  }

  private static Stream<Arguments> rule_set_contains_invalid_element() {
    return Stream.of(
        Arguments.of(" COMMENT hallo \n\n braaaa", 2),
        Arguments.of(" braaaa \n\n  COMMENT hallo \n\n ", 1),
        Arguments.of(" braaaa \n\n  COMMENT hallo \n\n braaaa", 1));
  }

  @ParameterizedTest
  @MethodSource()
  public void rule_set_contains_invalid_element(String rule, int expectedPosition)
      throws Exception {
    runner.run(
        rule,
        r -> {
          r.containsValidationMessage("the Rule Set contains invalid element")
              .atPosition(expectedPosition);
        });
  }

  @ParameterizedTest
  @ValueSource(strings = {"KOMMENTAR hallo", "hallo KOMMENTAR ", "\r\n KOMMENTAR \nhallo"})
  public void unresolved_keywords_wrong_culture(String rule) throws Exception {
    runner.run(
        rule,
        r -> {
          r.containsValidationMessage(
              "the content of Rule Set doesn't match current language/culture 'en'");
        });
  }
}
