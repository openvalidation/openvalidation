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
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class OneOfTests {
  // ONE OF
  @Test
  public void string_should_be_one_of_list() {
    HUMLFramework huml = new HUMLFramework();
    String input_left = "Franz";
    String[] input_right = {"Heins", "Jens", "Franz", "Peter", "Klaus", "Helmut"};
    Assertions.assertTrue(huml.AT_LEAST_ONE_OF(input_left, input_right));
  }

  @Test
  public void string_should_be_one_of_list_inline() {
    HUMLFramework huml = new HUMLFramework();
    String input_left = "Franz";
    Assertions.assertTrue(
        huml.AT_LEAST_ONE_OF(
            input_left, huml.CREATE_ARRAY("Heins", "Jens", "Franz", "Peter", "Klaus", "Helmut")));
  }

  @Test
  public void string_should_be_none_of_list() {
    HUMLFramework huml = new HUMLFramework();
    String input_left = "Jerry";
    String[] input_right = {"Heins", "Jens", "Franz", "Peter", "Klaus", "Helmut"};
    Assertions.assertTrue(huml.NONE_OF(input_left, input_right));
  }

  @Test
  public void string_should_be_one_of_list_with_duplicates() {
    HUMLFramework huml = new HUMLFramework();
    String input_left = "Jerry";
    String[] input_right = {
      "Heins", "Jens", "Franz", "Peter", "Klaus", "Helmut", "Jerry", "Jerry", "Franz", "Jerry",
      "Berry"
    };
    Assertions.assertTrue(huml.AT_LEAST_ONE_OF(input_left, input_right));
  }

  @Test
  public void string_should_be_at_least_one_of_list_with_duplicates() {
    HUMLFramework huml = new HUMLFramework();
    String input_left = "Jerry";
    String[] input_right = {
      "Heins", "Jens", "Franz", "Peter", "Klaus", "Helmut", "Jerry", "Jerry", "Franz", "Jerry",
      "Berry"
    };
    Assertions.assertTrue(huml.AT_LEAST_ONE_OF(input_left, input_right));
  }

  @Test
  public void string_should_not_be_one_of_list_with_duplicates() {
    HUMLFramework huml = new HUMLFramework();
    String input_left = "Mary";
    String[] input_right = {
      "Heins", "Jens", "Franz", "Peter", "Klaus", "Helmut", "Jerry", "Jerry", "Franz", "Jerry",
      "Berry"
    };
    Assertions.assertFalse(huml.AT_LEAST_ONE_OF(input_left, input_right));
  }

  @Test
  public void string_should_not_be_at_least_one_of_list_with_duplicates() {
    HUMLFramework huml = new HUMLFramework();
    String input_left = "Mary";
    String[] input_right = {
      "Heins", "Jens", "Franz", "Peter", "Klaus", "Helmut", "Jerry", "Jerry", "Franz", "Jerry",
      "Berry"
    };
    Assertions.assertFalse(huml.AT_LEAST_ONE_OF(input_left, input_right));
  }

  @Test
  public void string_should_not_be_at_least_one_of_list_with_duplicates_inline() {
    HUMLFramework huml = new HUMLFramework();
    String input_left = "Mary";
    Assertions.assertFalse(
        huml.AT_LEAST_ONE_OF(
            input_left,
            huml.CREATE_ARRAY(
                "Heins", "Jens", "Franz", "Peter", "Klaus", "Helmut", "Jerry", "Jerry", "Franz",
                "Jerry", "Berry")));
  }

  @Test
  public void int_should_not_be_at_least_one_of_list_with_duplicates() {
    HUMLFramework huml = new HUMLFramework();
    int input_left = 42;
    Integer[] input_right = {44, 45, 1, 0, 1024, 44, 45, 1, 0, 1024};
    Assertions.assertFalse(huml.AT_LEAST_ONE_OF(input_left, input_right));
  }

  @Test
  public void int_should_be_at_least_one_of_list_with_duplicates() {
    HUMLFramework huml = new HUMLFramework();
    int input_left = 42;
    List<Integer> input_right = Arrays.asList(42, 44, 45, 1, 0, 1024, 1024, 44, 45, 1, 9, 42);
    Assertions.assertTrue(huml.AT_LEAST_ONE_OF(input_left, input_right));
  }

  @Test
  public void int_arr_should_be_at_least_one_of_list_with_duplicates() {
    HUMLFramework huml = new HUMLFramework();
    Integer input_left = 42;
    Integer[] input_right = {42, 44, 45, 1, 0, 1024, 1024, 44, 45, 1, 9, 42};
    Assertions.assertTrue(huml.AT_LEAST_ONE_OF(input_left, input_right));
  }

  @Test
  public void int_should_not_be_at_least_one_of_list_with_duplicates_inline() {
    HUMLFramework huml = new HUMLFramework();
    int input_left = 42;
    Assertions.assertFalse(
        huml.AT_LEAST_ONE_OF(
            input_left, huml.CREATE_ARRAY(44, 45, 1, 0, 1024, 45, 1, 44, 0, 1024)));
  }

  @Test
  public void int_should_be_at_least_one_of_list_with_duplicates_inline() {
    HUMLFramework huml = new HUMLFramework();
    int input_left = 42;
    Assertions.assertTrue(
        huml.AT_LEAST_ONE_OF(
            input_left, huml.CREATE_ARRAY(42, 44, 45, 1, 0, 1024, 45, 1, 44, 0, 1024, 42)));
  }
}
