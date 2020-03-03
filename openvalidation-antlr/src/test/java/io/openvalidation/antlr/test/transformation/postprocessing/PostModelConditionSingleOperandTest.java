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

import static io.openvalidation.common.unittesting.astassertion.ModelRootAssertion.assertRules;

import io.openvalidation.antlr.ANTLRExecutor;
import io.openvalidation.common.ast.ASTComparisonOperator;
import io.openvalidation.common.ast.ASTModel;
import io.openvalidation.common.data.DataPropertyType;
import io.openvalidation.common.data.DataSchema;
import io.openvalidation.common.utils.GrammarBuilder;
import org.junit.jupiter.api.Test;

public class PostModelConditionSingleOperandTest
{

  @Test
  void test_implicit_equals_with_one_operand() throws Exception {
    // assemble
    String input =
        GrammarBuilder.createRule().with("name Validaria").EQ("IS").THEN("mööp").toString();

    DataSchema schema = new DataSchema();
    schema.addVariable("name", DataPropertyType.String);
    ASTModel astActual = ANTLRExecutor.run(input, schema);

    assertRules(astActual)
        .hasSizeOf(1)
        .firstCondition()
        .hasOperator(ASTComparisonOperator.EQUALS)
        .leftVariable("name")
        .parentCondition()
        .rightString("Validaria");
  }

  @Test
  void test_implicit_equals_with_one_numeric_operand() throws Exception {
    // assemble
    String input =
        GrammarBuilder.createRule().with("the age 20 years old").EQ("IS").THEN("mööp").toString();

    DataSchema schema = new DataSchema();
    schema.addVariable("age", DataPropertyType.Decimal);
    ASTModel astActual = ANTLRExecutor.run(input, schema);

    assertRules(astActual)
        .hasSizeOf(1)
        .firstCondition()
        .hasOperator(ASTComparisonOperator.EQUALS)
        .leftVariable("age")
        .parentCondition()
        .rightNumber(20.0);
  }

  @Test
  void test_not_equals_with_one_numeric_operand() throws Exception {
    // assemble
    String input =
        GrammarBuilder.createRule().with("the age 20 years old").NEQ().THEN("mööp").toString();

    DataSchema schema = new DataSchema();
    schema.addVariable("age", DataPropertyType.Decimal);
    ASTModel astActual = ANTLRExecutor.run(input, schema);

    assertRules(astActual)
        .hasSizeOf(1)
        .firstCondition()
        .hasOperator(ASTComparisonOperator.NOT_EQUALS)
        .leftVariable("age")
        .parentCondition()
        .rightNumber(20.0);
  }
}
