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

package builder;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import io.openvalidation.common.ast.ASTComparisonOperator;
import io.openvalidation.common.ast.builder.ASTConditionBuilder;
import io.openvalidation.common.ast.builder.ASTOperandFunctionBuilder;
import io.openvalidation.common.ast.condition.ASTCondition;
import io.openvalidation.common.ast.operand.ASTOperandFunction;
import io.openvalidation.common.ast.operand.ASTOperandStatic;
import io.openvalidation.common.ast.operand.ASTOperandStaticNumber;
import io.openvalidation.common.ast.operand.property.ASTOperandProperty;
import org.junit.jupiter.api.Test;

public class ASTOperandFunctionBuilderTest {

  @Test
  public void should_build_simple_function() {

    ASTOperandFunctionBuilder functionBuilder = new ASTOperandFunctionBuilder();
    ASTOperandFunction astFunction =
        functionBuilder
            .create()
            .withName("simple")
            .addParameter(new ASTOperandStaticNumber(5))
            .getModel();

    assertThat(astFunction, is(notNullValue()));
    assertThat(astFunction.getName(), is("simple"));
    assertThat(astFunction.getParameters(), hasSize(1));

    ASTOperandStaticNumber prop = (ASTOperandStaticNumber) astFunction.getParameters().get(0);

    assertThat(prop.getNumberValue(), is(5.0));
  }

  @Test
  public void should_build_complex_function_within_condition() {
    ASTConditionBuilder conditionBuilder = new ASTConditionBuilder();
    conditionBuilder.create();

    ASTCondition condition =
        conditionBuilder
            .withOperator(ASTComparisonOperator.SUM_OF)
            .createRightOperandAsFunction()
            .withName("simple")
            .addParameterAsFunction()
            .withName("GET_ARRAY_OF")
            .addParameter(new ASTOperandProperty("portfolio", "shares"))
            .addParameter(new ASTOperandStatic("s -> s.getPercentage()"))
            .getParentFuncBuilder()
            .parent()
            .getModel();

    assertThat(condition, is(notNullValue()));
    assertThat(condition.getRightOperand(), is(notNullValue()));
    assertThat(condition.getRightOperand(), instanceOf(ASTOperandFunction.class));

    ASTOperandFunction simpleFunction = (ASTOperandFunction) condition.getRightOperand();
    assertThat(simpleFunction, is(notNullValue()));
    assertThat(simpleFunction.getName(), is("simple"));
    assertThat(simpleFunction.getParameters().get(0), is(notNullValue()));
    assertThat(simpleFunction.getParameters().get(0), instanceOf(ASTOperandFunction.class));

    ASTOperandFunction arrayOfFunction = (ASTOperandFunction) simpleFunction.getParameters().get(0);
    assertThat(arrayOfFunction, is(notNullValue()));
    assertThat(arrayOfFunction.getName(), is("GET_ARRAY_OF"));
    assertThat(arrayOfFunction.getParameters(), is(notNullValue()));
    assertThat(arrayOfFunction.getParameters(), hasSize(2));
    assertThat(arrayOfFunction.getParameters().get(0), instanceOf(ASTOperandProperty.class));
    assertThat(arrayOfFunction.getParameters().get(1), instanceOf(ASTOperandStatic.class));

    ASTOperandProperty property = (ASTOperandProperty) arrayOfFunction.getParameters().get(0);

    assertThat(property.getPath(), is(notNullValue()));
    assertThat(property.getPath(), hasSize(2));
    assertThat(property.getPath().get(0).getPart(), is("portfolio"));
    assertThat(property.getPath().get(1).getPart(), is("shares"));

    ASTOperandStatic stat = (ASTOperandStatic) arrayOfFunction.getParameters().get(1);

    assertThat(stat.getValue(), is("s -> s.getPercentage()"));
  }
}
