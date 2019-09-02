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

import io.openvalidation.common.utils.Constants;
import io.openvalidation.common.utils.NameMasking;
import io.openvalidation.core.Aliases;
import java.util.List;
import java.util.Locale;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class AliasesTest {

  @Test
  void shouldResolveSimpleDEAlias() {
    String input = "EIN TESTFALL";
    String expected = "TESTCASE" + Constants.KEYWORD_SYMBOL + NameMasking.maskName(input);
    Locale de = new Locale("de");

    String actual = Aliases.resolve(input, de);

    assertThat(actual, is(expected));
  }

  @Test
  void shouldResolveSimpleENAlias() {
    String input = "A TESTCASE";
    String expected = "TESTCASE" + Constants.KEYWORD_SYMBOL + NameMasking.maskName(input);
    Locale de = new Locale("en");

    String actual = Aliases.resolve(input, de);

    assertThat(actual, is(expected));
  }

  @Test
  void shouldResolveSimpleRUAlias() {
    String input = "прецедент";
    String expected = "TESTCASE" + Constants.KEYWORD_SYMBOL + NameMasking.maskName(input);
    Locale de = new Locale("ru");

    String actual = Aliases.resolve(input, de);

    assertThat(actual, is(expected));
  }

  @Test
  void shouldResolveMultipleAlias() {
    String[] input = new String[] {"TESTFALL MULTI", "NOCHEINER"};
    String expected = "TESTCASE_MULTI";
    Locale de = new Locale("de");

    for (String alias : input) {
      String actual = Aliases.resolve(alias, de);
      assertThat(actual, containsString(expected));
    }
  }

  @Test
  void shouldResolve_KeyWordOnly_NotAContent() {
    String[] input_ruleContent =
        new String[] {
          "TESTFALL MULTI ES IST_ EIN[TESTFALL MULTI]AAA",
          "TESTFALL MULTI\n ES IST_ EIN[TESTFALL MULTI]AAA",
          "TESTFALL MULTI\r\n ES IST_ EIN[TESTFALL MULTI]AAA",
          "TESTFALL MULTI\r\n ES IST_ EIN[TESTFALL MULTI+++]AAA",
          "TESTFALL MULTI\r\n ES IST_ EIN[TESTFALL MULTI",
          "TESTFALL MULTI\r\n ES IST_ EIN[ TESTFALL MULTI "
        };
    String[] expected =
        new String[] {
          "TESTCASE_MULTI"
              + Constants.KEYWORD_SYMBOL
              + "TESTFALL_20_MULTI ES IST_ EIN[TESTFALL MULTI]AAA",
          "TESTCASE_MULTI"
              + Constants.KEYWORD_SYMBOL
              + "TESTFALL_20_MULTI\n ES IST_ EIN[TESTFALL MULTI]AAA",
          "TESTCASE_MULTI"
              + Constants.KEYWORD_SYMBOL
              + "TESTFALL_20_MULTI\n ES IST_ EIN[TESTFALL MULTI]AAA",
          "TESTCASE_MULTI"
              + Constants.KEYWORD_SYMBOL
              + "TESTFALL_20_MULTI\n ES IST_ EIN[TESTFALL MULTI+++]AAA",
          "TESTCASE_MULTI"
              + Constants.KEYWORD_SYMBOL
              + "TESTFALL_20_MULTI\n ES IST_ EIN[TESTFALL MULTI",
          "TESTCASE_MULTI"
              + Constants.KEYWORD_SYMBOL
              + "TESTFALL_20_MULTI\n ES IST_ EIN[ TESTCASE_MULTI"
              + Constants.KEYWORD_SYMBOL
              + "TESTFALL_20_MULTI"
        };

    Locale de = new Locale("de");

    for (int x = 0; x < input_ruleContent.length; x++) {
      String actual = Aliases.resolve(input_ruleContent[x], de);
      assertThat(actual, is(expected[x]));
      // assertEquals(expected[x], actual);
    }
  }

  @Test
  void should() {
    List<String> result = Aliases.availableCultures;

    assertThat(result, notNullValue());
    assertThat(result, hasItems("de", "en", "ru"));
  }

  @ParameterizedTest
  @ValueSource(strings = {"THEN", "then", "thEn", "\nTHEN a", "\n \n THEN a", "a\r\n\n THeN b"})
  public void alias_resolution_test(String input) {

    String actual = Aliases.resolve(input, new Locale("en"));
    assertThat(actual, containsString(Constants.THEN_TOKEN));
  }

  @ParameterizedTest
  @CsvSource({
    "aTHENa, " + Constants.THEN_TOKEN,
    "-then-, " + Constants.THEN_TOKEN,
    "[then]," + Constants.THEN_TOKEN,
    "[none of]," + Constants.NONE_OF,
    "[ none of]," + Constants.NONE_OF
  })
  public void should_not_resolv(String input, String expected) {

    String actual = Aliases.resolve(input, new Locale("en"));
    assertThat(actual, not(containsString(expected)));
  }

  @ParameterizedTest
  @ValueSource(
      strings = {
        "a\nTHEN\nb",
        "a\r\nTHEN\r\nb",
      })
  public void alias_resolution_should_contains_exactly_same_source_as_original(String input) {
    String actual = Aliases.resolve(input, new Locale("en"));
    String expected = "a\n" + Constants.THEN_TOKEN + "THEN" + "\nb";

    assertThat(actual, is(expected));
  }
}
