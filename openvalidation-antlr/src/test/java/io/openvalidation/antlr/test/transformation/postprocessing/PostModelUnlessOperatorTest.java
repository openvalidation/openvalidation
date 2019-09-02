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

package io.openvalidation.antlr.test.transformation.postprocessing;

import io.openvalidation.antlr.test.ANTLRRunner;
import io.openvalidation.common.ast.ASTComparisonOperator;
import io.openvalidation.common.ast.condition.ASTConditionConnector;
import io.openvalidation.common.utils.GrammarBuilder;
import org.junit.jupiter.api.Test;

public class PostModelUnlessOperatorTest {

  /*
   *        IF  user's name EQUALS Test
   *       AND  his age is LESS than 18
   *    UNLESS  his age IS 8
   *
   *
   *
   *      if (age != 8 && (name == 'Test' && age < 18))
   *           throw '...';
   *
   * */
  @Test
  void test_if_then_unless_combo() throws Exception {
    String input =
        GrammarBuilder.createRule()
            .with("name")
            .EQ()
            .with("Test")
            .AND()
            .with("age")
            .LESS_THAN(18)
            .UNLESS()
            .with("age")
            .EQ()
            .with("8")
            .THEN("...")
            .toString();

    String schema = "{name: peter, age: 25}";

    ANTLRRunner.run(
        input,
        schema,
        r -> {
          r.rules()
              .hasSizeOf(1)
              .firstConditionGroup()
              .hasSize(2)
              .first()
              .hasOperator(ASTComparisonOperator.NOT_EQUALS)
              .leftProperty("age")
              .parentCondition()
              .rightNumber(8.0)
              .parentConditionGroup()
              .hasSize(2)
              .firstConditionGroup()
              .hasConnector(ASTConditionConnector.AND)
              .first()
              .hasOperator(ASTComparisonOperator.EQUALS)
              .leftProperty("name")
              .parentCondition()
              .rightString("Test")
              .parentConditionGroup()
              .second()
              .hasOperator(ASTComparisonOperator.LESS_THAN)
              .leftProperty("age")
              .parentCondition()
              .rightNumber(18.0);
        });
  }

  /*
   *        user's name MUST NOT be Test
   *       AND  his age SHOULD be GREATER than 18 years
   *    UNLESS  his name IS JimmyJamasonsensssens
   *
   *
   *
   *      if (name != "JimmyJamasonsensssens" && (name == 'Test' || age <= 18))
   *           throw '...';
   *
   * */
  @Test
  void test_constrained_unless_combo() throws Exception {
    String input =
        GrammarBuilder.create()
            .with("user's name")
            .MUST_NOT()
            .with("be Test")
            .AND()
            .with("his age")
            .MUST()
            .with("be")
            .GREATER_THAN(18)
            .with("years")
            .UNLESS()
            .with("his name")
            .EQ()
            .with("JimmyJamasonsensssens")
            .toString();

    String schema = "{name: peter, age: 25}";

    ANTLRRunner.run(
        input,
        schema,
        r -> {
          r.rules()
              .hasSizeOf(1)
              .firstConditionGroup()
              .hasSize(2)
              .first()
              .hasOperator(ASTComparisonOperator.NOT_EQUALS)
              .leftProperty("name")
              .parentCondition()
              .rightString("JimmyJamasonsensssens")
              .parentConditionGroup()
              .hasSize(2)
              .firstConditionGroup()
              .hasConnector(ASTConditionConnector.AND)
              .first()
              .hasOperator(ASTComparisonOperator.EQUALS)
              .leftProperty("name")
              .parentCondition()
              .rightString("Test")
              .parentConditionGroup()
              .second()
              .hasOperator(ASTComparisonOperator.LESS_OR_EQUALS)
              .leftProperty("age")
              .parentCondition()
              .rightNumber(18.0);
        });
  }
}
