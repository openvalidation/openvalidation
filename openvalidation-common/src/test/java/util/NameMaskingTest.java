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

import io.openvalidation.common.utils.NameMasking;
import org.junit.jupiter.api.Test;

class NameMaskingTest {

  @Test
  void test_MaskName_with_space() {
    String inputName = "Ben Jerries";

    String actual = NameMasking.maskName(inputName);

    assertThat(actual, is("Ben_20_Jerries"));
  }

  @Test
  void test_MaskName_with_ampersand() {
    String inputName = "Ben&Jerries";

    String actual = NameMasking.maskName(inputName);

    assertThat(actual, is("Ben_26_Jerries"));
  }

  @Test
  void test_MaskName_with_question_mark() {
    String inputName = "Ben?Jerries";

    String actual = NameMasking.maskName(inputName);

    assertThat(actual, is("Ben_3f_Jerries"));
  }

  @Test
  void test_MaskName_with_apostrophe() {
    String inputName = "Ben'sJerries";

    String actual = NameMasking.maskName(inputName);

    assertThat(actual, is("Ben_27_sJerries"));
  }

  @Test
  void test_MaskName_with_double_quota() {
    String inputName = "Ben\"sJerries";

    String actual = NameMasking.maskName(inputName);

    assertThat(actual, is("Ben_22_sJerries"));
  }

  @Test
  void test_MaskName_with_hash() {
    String inputName = "Ben#Jerries";

    String actual = NameMasking.maskName(inputName);

    assertThat(actual, is("Ben_23_Jerries"));
  }

  @Test
  void test_MaskName_with_uncommon_a() {
    String inputName = "Thaddäus";

    String actual = NameMasking.maskName(inputName);

    assertThat(actual, is("Thadd_e4_us"));
  }

  @Test
  void test_MaskName_with_german_sharp_s() {
    String inputName = "Strauß";

    String actual = NameMasking.maskName(inputName);

    assertThat(actual, is("Strau_df_"));
  }

  @Test
  void test_MaskName_with_numbers() {
    String inputName = "L33t5p34k";

    String actual = NameMasking.maskName(inputName);

    assertThat(actual, is(inputName));
  }

  // unmasking (reverseTests)
  @Test
  void test_unmask_with_space() {
    String inputName = "Ben_20_Jerries";

    String actual = NameMasking.unmask(inputName);

    assertThat(actual, is("Ben Jerries"));
  }

  @Test
  void test_unmask_with_ampersand() {
    String inputName = "Ben_26_Jerries";

    String actual = NameMasking.unmask(inputName);

    assertThat(actual, is("Ben&Jerries"));
  }

  @Test
  void test_unmask_with_question_mark() {
    String inputName = "Ben_3f_Jerries";

    String actual = NameMasking.unmask(inputName);

    assertThat(actual, is("Ben?Jerries"));
  }

  @Test
  void test_unmask_with_apostrophe() {
    String inputName = "Ben_27_sJerries";

    String actual = NameMasking.unmask(inputName);

    assertThat(actual, is("Ben'sJerries"));
  }

  @Test
  void test_unmask_with_double_quota() {
    String inputName = "Ben_22_sJerries";

    String actual = NameMasking.unmask(inputName);

    assertThat(actual, is("Ben\"sJerries"));
  }

  @Test
  void test_unmask_with_hash() {
    String inputName = "Ben_23_Jerries";

    String actual = NameMasking.unmask(inputName);

    assertThat(actual, is("Ben#Jerries"));
  }

  @Test
  void test_unmask_with_uncommon_a() {
    String inputName = "Thadd_e4_us";

    String actual = NameMasking.unmask(inputName);

    assertThat(actual, is("Thaddäus"));
  }

  @Test
  void test_unmask_with_german_sharp_s() {
    String inputName = "Strau_df_";

    String actual = NameMasking.unmask(inputName);

    assertThat(actual, is("Strauß"));
  }

  @Test
  void test_unmask_with_numbers() {
    String inputName = "L33t5p34k";

    String actual = NameMasking.unmask(inputName);

    assertThat(actual, is(inputName));
  }
}
