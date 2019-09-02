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

package io.openvalidation.generation.tests.lambdas;

import io.openvalidation.common.ast.builder.ASTOperandFunctionBuilder;
import io.openvalidation.generation.tests.ExpectationBuilder;
import io.openvalidation.generation.tests.GTE;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class GeneratorMapArrayLambdasTest {

  private static Stream<Arguments> simple_map_array() {
    return ExpectationBuilder.newExpectation()
        .javaResult("huml.getArrayOf(model.getAddresses(), x -> x.getCity())")
        .toStream();
  }

  @ParameterizedTest(name = GTE.PARAM_TEST_NAME)
  @MethodSource()
  public void simple_map_array(String language, String expected) throws Exception {
    GTE.execute(
        expected,
        language,
        p -> {

          // {addresses:[{city:''}]}
          // addresses.map(a -> a.city)

          ASTOperandFunctionBuilder builder = new ASTOperandFunctionBuilder();
          builder.createArrayOfFunction("addresses", "x", "city");

          return builder.getModel();
        });
  }

  private static Stream<Arguments> simple_map_array_with_nested_property_access() {
    return ExpectationBuilder.newExpectation()
        .javaResult("huml.getArrayOf(model.getUser().getAddresses(), x -> x.getCity().getZip())")
        .toStream();
  }

  @ParameterizedTest(name = GTE.PARAM_TEST_NAME)
  @MethodSource()
  public void simple_map_array_with_nested_property_access(String language, String expected)
      throws Exception {
    GTE.execute(
        expected,
        language,
        p -> {

          // {user.addresses:[{city:{zip:''}}]}
          // user.addresses.map(a -> a.city.zip)

          ASTOperandFunctionBuilder builder = new ASTOperandFunctionBuilder();
          String[] addressPath = {"user", "addresses"};
          String[] lambdaPath = {"city", "zip"};
          builder.createArrayOfFunction(addressPath, "x", lambdaPath);

          return builder.getModel();
        });
  }
}
