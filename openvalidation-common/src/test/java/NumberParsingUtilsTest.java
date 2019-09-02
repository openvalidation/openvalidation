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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import io.openvalidation.common.utils.NumberParsingUtils;
import org.junit.jupiter.api.Test;

public class NumberParsingUtilsTest {

  @Test
  public void shoud_convert_whole_string_to_number() {
    String input = "34";
    Double result = NumberParsingUtils.extractNumber(input);

    assertThat(result, is(34.0));
  }

  @Test
  public void shoud_convert_whole_string_to_double() {
    String input = "34.234234234";
    Double result = NumberParsingUtils.extractNumber(input);

    assertThat(result, is(34.234234234));
  }

  @Test
  public void shoud_extract_number_value_from_string() {
    String input = "hallo 34 aaa";
    Double result = NumberParsingUtils.extractNumber(input);

    assertThat(result, is(34.0));
  }

  @Test
  public void shoud_extract_double_value_from_string() {
    String input = "hallo 34.234234234 aaa";
    Double result = NumberParsingUtils.extractNumber(input);

    assertThat(result, is(34.234234234));
  }

  @Test
  public void shoud_not_convert_whole_string_to_double() {
    String input = "abcd";
    Double result = NumberParsingUtils.extractNumber(input);

    assertThat(result, nullValue());
  }

  @Test
  public void shoud_not_convert_number_within_string_to_double() {
    String input = "ab234cd";
    Double result = NumberParsingUtils.extractNumber(input);

    assertThat(result, nullValue());
  }

  @Test
  public void string_should_be_a_number() {
    String input = "234";
    boolean result = NumberParsingUtils.isNumber(input);

    assertThat(result, is(true));
  }

  @Test
  public void string_should_not_be_a_number() {
    String input = "ab234c";
    boolean result = NumberParsingUtils.isNumber(input);

    assertThat(result, is(false));
  }

  @Test
  public void string_should_not_be_a_number_a() {
    String input = "0001";
    boolean result = NumberParsingUtils.isNumber(input);

    assertThat(result, is(false));
  }

  @Test
  public void string_should_contains_a_number() {
    String input = "asdf 234 asdf";
    boolean result = NumberParsingUtils.containsNumber(input);

    assertThat(result, is(true));
  }

  @Test
  public void string_should_not_contains_a_number() {
    String input = "asdf asdf asdf";
    boolean result = NumberParsingUtils.containsNumber(input);

    assertThat(result, is(false));
  }
}
