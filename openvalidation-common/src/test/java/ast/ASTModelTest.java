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

package ast;

import static io.openvalidation.common.unittesting.astassertion.ModelRootAssertion.assertAST;

import io.openvalidation.common.ast.ASTComparisonOperator;
import io.openvalidation.common.ast.ASTModel;
import io.openvalidation.common.ast.builder.ASTModelBuilder;
import org.junit.jupiter.api.Test;

public class ASTModelTest {

  @Test
  public void should_create_null_check_rules() throws Exception {
    ASTModelBuilder builder = new ASTModelBuilder();
    builder
        .create()
        .createRule()
        .createCondition()
        .withLeftOperandAsProperty("address", "city")
        .withOperator(ASTComparisonOperator.EQUALS)
        .withRightOperandAsString("Dortmund");

    ASTModel model = builder.getModel();

    assertAST(model)
        .sizeOfElements(1)
        .sizeOfRules(1)
        .rules()
        .first()
        .condition()
        .hasOperator(ASTComparisonOperator.EQUALS)
        .leftProperty()
        .hasPath("address.city")
        .parentCondition()
        .rightString()
        .hasValue("Dortmund");
    //                .parentModel()
    //                .nullCheckRules()
    //                    .first()
    //                        .condition()
    //                            .hasOperator(ASTComparisonOperator.EMPTY)
    //                            .leftProperty()
    //                                .hasPath("address")
    //                        .parentCondition();
  }

  @Test
  public void should_create_null_check_rules_from_variable() throws Exception {
    ASTModelBuilder builder = new ASTModelBuilder();
    builder
        .create()
        .createVariable("mycity")
        .withValueAsProperty("address", "city")
        .parent()
        .createRule()
        .createCondition()
        .withLeftOperandAsVariable("mycity")
        .withOperator(ASTComparisonOperator.EQUALS)
        .withRightOperandAsString("Dortmund");

    ASTModel model = builder.getModel();

    assertAST(model)
        .sizeOfElements(2)
        .sizeOfRules(1)
        .sizeOfVariables(1)
        .rules()
        .first()
        .condition()
        .hasOperator(ASTComparisonOperator.EQUALS)
        .leftVariable()
        .hasName("mycity")
        .parentCondition()
        .rightString()
        .hasValue("Dortmund");
  }

  @Test
  public void should_create_recursive_null_check_rules_from_variable() throws Exception {
    ASTModelBuilder builder = new ASTModelBuilder();
    builder
        .create()
        .createVariable("mycity")
        .withValueAsProperty("address", "city")
        .parent()
        .createVariable("is Dortmund")
        .createValueAsCondition()
        .withLeftOperandAsVariable("mycity")
        .withOperator(ASTComparisonOperator.EQUALS)
        .withRightOperandAsString("Dortmund")
        .parentVariable()
        .parent()
        .createRule()
        .createCondition()
        .withLeftOperandAsVariable("is Dortmund")
        .withOperator(ASTComparisonOperator.EQUALS)
        .withRightOperandAsBoolean(true);

    ASTModel model = builder.getModel();

    assertAST(model)
        .sizeOfElements(3)
        .sizeOfRules(1)
        .sizeOfVariables(2)
        .rules()
        .first()
        .condition()
        .hasOperator(ASTComparisonOperator.EQUALS)
        .leftVariable()
        .hasName("is Dortmund")
        .parentCondition()
        .rightBoolean()
        .isTrue();
  }
}
