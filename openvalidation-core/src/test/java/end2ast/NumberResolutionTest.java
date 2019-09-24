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

package end2ast;

import static io.openvalidation.common.unittesting.astassertion.ModelRootAssertion.assertResult;

import io.openvalidation.common.ast.ASTComparisonOperator;
import io.openvalidation.common.data.DataPropertyType;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class NumberResolutionTest {
  @ParameterizedTest
  @ValueSource(
      strings = {
        "IF age SMALLER 18 THEN a",
        "IF age SMALLER 18.000 THEN a",
        "IF the age SMALLER than 18 is THEN a",
        "IF the age SMALLER than 18.00 is THEN a"
        //            "the age SHOULD be 18 years",
        //            "the age SHOULD be 18.00 years",
      })
  public void number_resolution(String rule) throws Exception {

    assertResult(End2AstRunner.run(rule, "{age:0}"))
        .rules()
        .hasSizeOf(1)
        .first()
        .condition()
        .hasOperator(ASTComparisonOperator.LESS_THAN)
        .leftProperty()
        .hasPath("age")
        .hasType(DataPropertyType.Decimal)
        .parentCondition()
        .rightNumber()
        .hasValue(18.0);
  }

  @ParameterizedTest
  @ValueSource(
      strings = {
        "IF age IS -4 THEN a",
        "IF age IS the number -4 THEN a",
        "IF age IS -4.000 THEN a",
        "IF the age -4 is THEN a",
        "IF the age the number -4 is THEN a",
        "IF the age -4.00 is THEN a",
        "the age MUST NOT be -4",
        "the age MUST NOT be the number -4"
      })
  public void number_resolution_negative(String rule) throws Exception {

    assertResult(End2AstRunner.run(rule, "{age:0}"))
        .rules()
        .hasSizeOf(1)
        .first()
        .condition()
        .hasOperator(ASTComparisonOperator.EQUALS)
        .leftProperty()
        .hasPath("age")
        .hasType(DataPropertyType.Decimal)
        .parentCondition()
        .rightNumber()
        .hasValue(-4.0);
  }
}
