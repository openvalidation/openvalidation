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

package util;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import io.openvalidation.common.utils.NumberParsingUtils;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

public class NumberParsinUtilsTest {
  @ParameterizedTest
  @CsvSource({"18,18.0", "18.01,18.01", "a 18 b, 18.0", "a 18.01 b, 18.01", "a 0 b, 0"})
  public void parse_decimal_numbers(String value, Double expected) throws Exception {

    Number result = NumberParsingUtils.extractDouble(value);

    assertThat(result, is(expected));
  }

  @ParameterizedTest
  @ValueSource(strings = {"abcd", "0001"})
  public void string_should_not_contains_a_number(String input) {
    boolean result = NumberParsingUtils.containsNumber(input);

    assertThat(result, is(false));
  }

  @ParameterizedTest
  @ValueSource(strings = {"0", "0.5", "1", "1.5", "12345.6789"})
  public void test_isNumber_positives(String numberString) {
    boolean result = NumberParsingUtils.isNumber(numberString);

    assertThat(result, is(true));
  }

  @ParameterizedTest
  @ValueSource(strings = {"01.23", "0123.456", "1.1.20", "0123", "01234-567-89"})
  public void test_isNumber_negatives(String numberString) {
    boolean result = NumberParsingUtils.isNumber(numberString);

    assertThat(result, is(false));
  }
}
