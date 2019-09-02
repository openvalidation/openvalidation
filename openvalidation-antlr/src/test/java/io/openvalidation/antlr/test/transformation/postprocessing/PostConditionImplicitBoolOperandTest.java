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
import io.openvalidation.common.utils.GrammarBuilder;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class PostConditionImplicitBoolOperandTest {

  @Test
  void test_implicit_bool_with_one_property_operand() throws Exception {
    // assemble
    String input =
        GrammarBuilder.createRule()
            .with("der Vertrag unterschrieben")
            .EQ("ist")
            .THEN("mööp")
            .toString();

    String schema = "{unterschrieben: true}";

    ANTLRRunner.run(
        input,
        schema,
        r -> {
          r.rules()
              .hasSizeOf(1)
              .firstCondition()
              .hasOperator(ASTComparisonOperator.EQUALS)
              .leftProperty("unterschrieben")
              .parentCondition()
              .rightBoolean(true);
        });
  }

  // todo lazevedo 31.7.19 Not working, Fix needed. Apparently postprocessing of implicit bools
  // happens before variable resolvemnt in postprocess
  @Disabled
  @Test
  void test_implicit_bool_with_one_variable_operand() throws Exception {
    // assemble
    String input =
        GrammarBuilder.create()
            .with("der Vertrag ist unterschrieben")
            .AS("signiert")
            .PARAGRAPH()
            .IF()
            .with("der Vertrag signiert")
            .EQ("ist")
            .THEN("mööp")
            .toString();

    String schema = "{unterschrieben: true}";

    ANTLRRunner.run(
        input,
        schema,
        r -> {
          r.rules()
              .hasSizeOf(1)
              .firstCondition()
              .hasOperator(ASTComparisonOperator.EQUALS)
              .leftVariable("signiert")
              .parentCondition()
              .rightBoolean(true);
        });
  }
}
