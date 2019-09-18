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

package io.openvalidation.integration.tests.framework.functions;

import io.openvalidation.integration.tests.HUMLFramework;
import java.math.BigDecimal;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class SumOfTests {

  // SUM OF
  @ParameterizedTest
  @ValueSource(
      ints = {
        1,
        0,
        -1,
        -5,
        -8,
        -24,
        -42,
        -555,
        -1111,
        -11011,
        -1234567,
        Integer.MIN_VALUE,
        Integer.MAX_VALUE - 1,
        24,
        42,
        128,
        2024,
        75000
      })
  void sum_of_input_and_1_should_be_input_plus_1(int input) {
    HUMLFramework huml = new HUMLFramework();

    Assertions.assertTrue(huml.EQUALS(huml.SUM_OF(input, 1), input + 1));
  }

  @Test
  @Disabled
  public void sum_of_fibonacci_numbers_should_be_54() {
    HUMLFramework huml = new HUMLFramework();
    Assertions.assertEquals(huml.SUM_OF(1, 1, 2, 3, 5, 8, 13, 21), new BigDecimal(54));
  }

  @Test
  public void sum_of_fibonacci_number_should_not_be_60() {
    HUMLFramework huml = new HUMLFramework();
    Assertions.assertFalse(huml.EQUALS(huml.SUM_OF(1, 1, 2, 3, 5, 8, 13, 21), 60));
  }
}
