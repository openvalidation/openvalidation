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

package io.openvalidation.antlr.test.transformation;

import static io.openvalidation.common.unittesting.astassertion.ModelRootAssertion.assertVariable;

import io.openvalidation.antlr.ANTLRExecutor;
import io.openvalidation.common.ast.ASTComparisonOperator;
import io.openvalidation.common.ast.ASTModel;
import io.openvalidation.common.converter.SchemaConverterFactory;
import io.openvalidation.common.data.DataPropertyType;
import io.openvalidation.common.utils.GrammarBuilder;
import org.junit.jupiter.api.Test;

public class PTLambdaTransformerTest {

  @Test
  void should_created_simple_property_with_lambda() throws Exception {

    String input =
        GrammarBuilder.create()
            .with("addresses")
            .WITH()
            .EQ("country", "DE")
            .AS("selector")
            .getText();

    // first from addresses AS var
    // var :{country: "fsgdfg"}  AS var
    // var.country
    // country
    String schema = "{addresses:[{country:''}]}}";

    // {selector:[{country:''}]}}

    ASTModel ast = ANTLRExecutor.run(input, SchemaConverterFactory.convert(schema));

    assertVariable(ast)
        .hasSizeOf(1)
        .first()
        .hasName("selector")
        .hasType(DataPropertyType.Array)
        .operandWhereFunction()
        .hasType(DataPropertyType.Array)
        .firstProperty()
        .hasPath("addresses")
        .parentFunction()
        .secondLambda()
        .hasType(DataPropertyType.Boolean)
        .condition()
        .leftProperty("country")
        .parentCondition()
        .rightString("DE");
  }

  @Test
  void should_created_nested_property_with_lambda() throws Exception {

    String input =
        GrammarBuilder.create()
            .with("user.addresses")
            .WITH()
            .EQ("country.type", "common")
            .AS("selector")
            .getText();

    String schema = "{user:{addresses:[{country:{type:''}}]}}";

    ASTModel ast = ANTLRExecutor.run(input, SchemaConverterFactory.convert(schema));

    assertVariable(ast)
        .hasSizeOf(1)
        .first()
        .hasName("selector")
        .hasType(DataPropertyType.Array)
        .operandWhereFunction()
        .hasType(DataPropertyType.Array)
        .firstProperty()
        .hasPath("user.addresses")
        .parentFunction()
        .hasType(DataPropertyType.Array)
        .secondLambda()
        .hasType(DataPropertyType.Boolean)
        .condition()
        .leftProperty("country.type")
        .parentCondition()
        .rightString("common");
  }

  @Test
  void should_created_lambda_with_variable_array() throws Exception {

    String input =
        GrammarBuilder.create()
            .with("addresses")
            .AS("all adrsss")
            .PARAGRAPH()
            .with("all adrsss")
            .WITH()
            .EQ("country", "DE")
            .AS("selector")
            .getText();

    String schema = "{addresses:[{country:''}]}";

    ASTModel ast = ANTLRExecutor.run(input, SchemaConverterFactory.convert(schema));

    assertVariable(ast)
        .hasSizeOf(2)
        .first()
        .hasName("all adrsss")
        .hasType(DataPropertyType.Array)
        .operandProperty()
        .hasPath("addresses")
        .hasType(DataPropertyType.Array)
        .parentModel()
        .variables()
        .second()
        .hasName("selector")
        .hasType(DataPropertyType.Array)
        .operandFunction()
        .firstVariable()
        .hasType(DataPropertyType.Array)
        .hasName("all adrsss")
        .parentFunction()
        .secondLambda()
        .hasType(DataPropertyType.Boolean)
        .condition()
        .hasOperator(ASTComparisonOperator.EQUALS)
        .leftProperty("country")
        .hasType(DataPropertyType.String)
        .parentCondition()
        .rightString("DE");
  }

  @Test
  void should_created_lambda_with_sumof_variable() throws Exception {
    // sum of population from addresses with population greater than 1000000 AS  big cities
    // population

    String input =
        GrammarBuilder.create()
            .SUMOF("population")
            .FROM("addresses")
            .WITH()
            .with("population")
            .GREATER_THAN("1000000")
            .AS("big cities population")
            .getText();

    String schema = "{ addresses:[{ city:'Dortmund', population:0}]}";

    ANTLRExecutor.run(
        input,
        SchemaConverterFactory.convert(schema),
        a -> {
          a.variables()
              .hasSizeOf(1)
              .first()
              .hasName("big cities population")
              .hasType(DataPropertyType.Decimal)
              .operandFunction()
              .hasName("SUM_OF")
              .hasType(DataPropertyType.Decimal)
              .firstArrayOfFunction()
              .firstWhereFunction()
              .hasType(DataPropertyType.Array)
              .firstProperty("addresses")
              .parentFunction()
              .secondLambda()
              .tokenStartsWith("x")
              .condition()
              .leftProperty("population")
              .tokenStartsWith("x")
              .parentCondition()
              .rightNumber(1000000.0)
              .parentCondition()
              .parentFunction()
              .parentFunction()
              .secondLambda()
              .tokenStartsWith("x")
              .property()
              .hasPath("population");
        });
  }
}
