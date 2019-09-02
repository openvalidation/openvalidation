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
import static org.hamcrest.Matchers.*;

import io.openvalidation.common.utils.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

public class StringUtilsTest {
  @ParameterizedTest
  @CsvSource({
    "ʬabcʬresult, result",
    "ʬabcʬdefʬresult, result",
    "hallo ʬabcʬdefʬresult hallo, hallo result hallo",
    "\\nʬabcʬdefʬresult\\n, \\nresult\\n",
    "hallo\\r\\n ich ʬabcʬdefʬresult\\r\\n war, hallo\\r\\n ich result\\r\\n war",
    "ʬabcʬresult ʬabcʬresult, result result",
    "\\nʬabcʬdefʬresult\\n\\nʬabcʬdefʬresult\\n, \\nresult\\n\\nresult\\n",
    "ʬabcʬe_f_gʬresult hallo, result hallo",
    "ʬabcʬe_f_gʬresult123 hallo, result123 hallo",
    "ʬabcʬe_f_gʬre_20_sult, re sult",
  })
  public void replace_all_tokens(String value, String expected) throws Exception {
    String result = StringUtils.reverseKeywords(value.replace("\\n", "\n").replace("\\r", "\r"));

    assertThat(result, is(expected.replace("\\n", "\n").replace("\\r", "\r")));
  }

  @ParameterizedTest
  @ValueSource(
      strings = {
        "hello",
        "hello,",
        "hello.",
        " , hello , ",
        "\n be hello , \nbe",
        "\n be hello , , \nbe",
        ", \n be hello , \nbe , ",
        ". \n be hello . \nbe . ",
        ". \n is hello . \nIS . ",
      })
  public void strip_special_words(String value) throws Exception {
    String result = StringUtils.stripSpecialWords(value);

    assertThat(result, is("hello"));
  }

  @Test
  void test_mirror_string() {
    String input = "I want to be mirrored";
    String expected = "derorrim eb ot tnaw I";

    assertThat(StringUtils.mirrorString(input), is(expected));
  }

  @ParameterizedTest
  @ValueSource(
      strings = {
        "hello",
        "HELLO",
        "HeLLO",
        "\nHeLLO\n",
        "\nHeLLOtest hello aasf\n",
        "\ntest\r\nhello\n",
      })
  public void matchWord(String value) throws Exception {
    assertThat(StringUtils.matchWord(value, "hello"), is(true));
  }

  @ParameterizedTest
  @ValueSource(
      strings = {
        "he llo",
        "abcHELLOdef",
        "abc\nHELLOdef",
        "[hello ]",
        "[ hello]",
      })
  public void not_matchWord(String value) throws Exception {
    assertThat(StringUtils.matchWord(value, "hello"), is(false));
  }

  @ParameterizedTest
  @CsvSource({
    "hello, hello, ###",
    "hello hello, hello, ### ###",
    "hello test, hello, ### test",
    "\nhello test, hello, ### test",
    "a + b, +, a ### b"
  })
  public void matchWord(String value, String seachFor, String expected) throws Exception {
    assertThat(StringUtils.matchAndReplaceWords(value, seachFor, "###"), is(expected));
  }
}
