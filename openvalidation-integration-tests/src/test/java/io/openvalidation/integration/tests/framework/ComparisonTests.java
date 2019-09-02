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

package io.openvalidation.integration.tests.framework;

import io.openvalidation.integration.tests.HUMLFramework;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class ComparisonTests {

  @ParameterizedTest
  @ValueSource(
      doubles = {
        -2,
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
        Double.MAX_VALUE
      })
  public void input_greater_or_equals_minus_18_should_be_true(double input) {
    HUMLFramework huml = new HUMLFramework();
    Assertions.assertTrue(huml.GREATER_OR_EQUALS(input, -18));
  }
}
