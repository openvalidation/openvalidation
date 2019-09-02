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

package io.openvalidation.integration.tests.framework.operators;

import io.openvalidation.integration.tests.HUMLFramework;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class EqualsTests {
  // EQUALS
  @Test
  public void equals_string_name_should_be_name() {
    HUMLFramework huml = new HUMLFramework();
    String input = "name";
    Assertions.assertTrue(huml.EQUALS(input, "name"));
  }

  @Test
  public void equals_int_should_not_be_string() {
    HUMLFramework huml = new HUMLFramework();
    int input = 123456789;
    Assertions.assertTrue(huml.NOT_EQUALS(input, "name"));
  }

  @ParameterizedTest
  @ValueSource(
      doubles = {
        -1,
        0,
        -5,
        -18,
        500.55501,
        -1.1010101010101,
        0.1,
        1.1111111111,
        0.999999999999,
        1.1,
        2.0,
        2.2,
        3.0,
        3.5,
        6.0,
        1000.1,
        Double.MAX_VALUE,
        Double.MIN_VALUE,
        Double.MIN_NORMAL
      })
  void input_double_should_be_not_equal_int_1(double input_left) {
    HUMLFramework huml = new HUMLFramework();
    int input_right = 1;
    Assertions.assertTrue(huml.NOT_EQUALS(input_left, input_right));
  }
}
