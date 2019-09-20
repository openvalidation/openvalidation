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

import io.openvalidation.common.ast.ASTComparisonOperator;
import io.openvalidation.common.ast.builder.ASTConditionBuilder;
import io.openvalidation.common.ast.builder.ASTOperandFunctionBuilder;
import io.openvalidation.generation.tests.ExpectationBuilder;
import io.openvalidation.generation.tests.GTE;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class GeneratorWhereArrayLambdasTest {
  private static Stream<Arguments> simple_map_array() {
    return ExpectationBuilder.newExpectation()
        .javaResult("huml.WHERE(model.getAddresses(), x -> huml.EQUALS(x.getCity(), \"Dortmund\"))")
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
          // addresses.WHERE(a -> a.city == 'Dortmund')

          String lambdaToken = "x";

          ASTConditionBuilder conditionBuilder = new ASTConditionBuilder();
          conditionBuilder
              .create()
              .withOperator(ASTComparisonOperator.EQUALS)
              .withLeftOperandAsPropertyWithLambdayToken("city", lambdaToken)
              .withRightOperandAsString("Dortmund");

          ASTOperandFunctionBuilder builder = new ASTOperandFunctionBuilder();
          builder
              .createWhereFunction("addresses")
              .addLambdaConditionParamenter(conditionBuilder.getModel(), lambdaToken);

          return builder.getModel();
        });
  }
}
