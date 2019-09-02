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

package io.openvalidation.integration.tests;

import io.openvalidation.integration.generator.ModelGenerator;
import java.lang.reflect.Constructor;

public class IntegrationTestFactory {

  private static String BASE_PACKAGE = "io.openvalidation.integration.tests";

  public static HUMLFramework.IOpenValidator createValidator(String testName) throws Exception {

    String className = BASE_PACKAGE + ".TESTValidator_" + testName;

    Class<?> clazz = Class.forName(className);
    Constructor<?> ctor = clazz.getConstructor();
    Object object = ctor.newInstance(); // ctor.newInstance(new Object[] { ctorArgument });

    return (HUMLFramework.IOpenValidator) object;
  }

  public static Object createModel(String jsonTestData, String testName) throws Exception {

    String className = BASE_PACKAGE + "." + testName + ".Model";
    Class<?> clazz = Class.forName(className);

    return ModelGenerator.fromJson(jsonTestData, clazz);
  }
}
