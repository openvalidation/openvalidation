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
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class EmptyTests {
  // EMPTY
  @Test
  public void empty_string_should_be_empty() {
    HUMLFramework huml = new HUMLFramework();
    String input = "";
    Assertions.assertTrue(huml.EMPTY(input));
  }

  @ParameterizedTest
  @ValueSource(
      doubles = {
        1.1,
        12,
        15,
        Double.MIN_NORMAL,
        Double.MAX_VALUE,
        Double.MIN_VALUE,
        Double.NEGATIVE_INFINITY,
        Double.POSITIVE_INFINITY
      })
  void input_object_should_not_be_empty(Object input_left) {
    HUMLFramework huml = new HUMLFramework();
    Assertions.assertTrue(huml.NOT_EMPTY(input_left));
  }

  @ParameterizedTest
  @ValueSource(strings = {" ", "   ", "content", "\r\n", "\t", "\n", "\\"})
  void input_string_should_not_be_empty(String input_left) {
    HUMLFramework huml = new HUMLFramework();
    Assertions.assertTrue(huml.NOT_EMPTY(input_left));
  }

  @Test
  public void empty_list_should_be_empty() {
    HUMLFramework huml = new HUMLFramework();
    List<Object> input = new ArrayList<>();
    Assertions.assertTrue(huml.EMPTY(input));
  }

  @Test
  public void list_should_not_be_empty() {
    HUMLFramework huml = new HUMLFramework();
    List<Object> input = new ArrayList<>();
    input.add("value");
    Assertions.assertTrue(huml.NOT_EMPTY(input));
  }
}
