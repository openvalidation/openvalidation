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

package io.openvalidation.test.rest.model.dto.dto;

import io.openvalidation.core.OpenValidation;
import java.util.HashMap;
import java.util.Map;

/**
 * This class holds utility methods for API Tests. The provided methods and objects should not be
 * used to test the OpenValidation Framework itself, because the values for mapentries will not work
 * with {@link OpenValidation}
 */
public class TestUtils {

  // TODO VERIFY PARAMS AFTER ADJUSTMENTS TO SERVICE LAYER

  /**
   * Creates a Map that contains some invalid keys
   *
   * @return the map containing the keys and random values
   */
  public static Map<String, String> wrongParams() {
    Map<String, String> wrongParams = new HashMap<>();
    wrongParams.put("some wrong param", "Some value");
    wrongParams.put("", "");
    wrongParams.put(null, null);
    wrongParams.put("null", null);
    return wrongParams;
  }

  /**
   * Creates a Map that contains all valid keys and some invalid keys
   *
   * @return the map containing the keys and random values
   */
  public static Map<String, String> mixedParams() {
    Map<String, String> params = new HashMap<>();
    params.put("schema", "some schema");
    params.put("rule", "some rule");
    params.put("culture", "de");
    params.put("language", "java");
    params.put("help", "true");
    params.put("single_file", "some bool");
    params.put("someparam", "some value");
    params.put("output", "some output");
    params.put("package", "some package");
    params.put("custome_name", "some name");
    params.put("model_package", "some model package");
    params.put("no_banner", "true");
    params.put("al", "some random value,,");
    params.put("verbose", "true");
    params.put("", "");
    params.put(null, null);
    params.put("null", null);
    return params;
  }

  /**
   * Creates a Map that contains all valid keys.
   *
   * @return the map containing the keys and random values
   */
  public static Map<String, String> correctParams() {
    Map<String, String> params = new HashMap<>();
    params.put("schema", "some schema");
    params.put("rule", "some rule");
    params.put("culture", "de");
    params.put("language", "java");
    params.put("package", "some package");
    params.put("custome_name", "some name");
    params.put("model_package", "some model package");
    return params;
  }
}
