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

package io.openvalidation.common.unittesting.astassertion;

import io.openvalidation.common.ast.ASTModel;
import io.openvalidation.common.ast.condition.ASTCondition;
import io.openvalidation.common.ast.condition.ASTConditionGroup;
import io.openvalidation.common.ast.operand.lambda.ASTOperandLambdaExpression;
import io.openvalidation.common.ast.operand.property.ASTOperandProperty;
import io.openvalidation.common.data.DataPropertyType;

public class LambdaAssertion
    extends ASTItemAssertionBase<ASTOperandLambdaExpression, ASTAssertionBase, LambdaAssertion> {

  public LambdaAssertion(ASTOperandLambdaExpression item, ASTModel ast, ASTAssertionBase parent) {
    super(item, ast, parent);
  }

  public ConditionAssertion condition() {
    this.shouldNotBeNull(this.model.getOperand(), "LAMBDA WHERE CONDITION");
    this.shouldBeInstanceOf(this.model.getOperand(), ASTCondition.class, "LAMBDA WHERE CONDITION");

    return new ConditionAssertion((ASTCondition) this.model.getOperand(), ast, this);
  }

  public ConditionGroupAssertion conditionGroup() {
    this.shouldNotBeNull(this.model.getOperand(), "LAMBDA CONDITION GROUP");
    this.shouldBeInstanceOf(
        this.model.getOperand(), ASTConditionGroup.class, "LAMBDA CONDITION GROUP");

    return new ConditionGroupAssertion((ASTConditionGroup) this.model.getOperand(), ast, this);
  }

  public PropertyAssertion property() {
    this.shouldNotBeNull(this.model.getOperand(), "LAMBDA PROPERTY ACCESS");
    this.shouldBeInstanceOf(
        this.model.getOperand(), ASTOperandProperty.class, "LAMBDA PROPERTY ACCESS");

    return new PropertyAssertion((ASTOperandProperty) this.model.getOperand(), ast, this);
  }

  public LambdaAssertion hasType(DataPropertyType expectedType) {
    shouldEquals(this.model.getDataType(), expectedType, "DataType");

    return this;
  }

  public LambdaAssertion tokenStartsWith(String expectedStart) {
    this.shouldNotBeEmpty(this.model.getLambdaToken(), "LAMBDA TOKEN");
    this.shouldStartsWith(this.model.getLambdaToken(), expectedStart, "LAMBDA TOKEN");

    return this;
  }

  public FunctionAssertion parentFunction() {
    this.shouldBeInstanceOf(this.parent, OperandAssertion.class, "PARENT FUNCTION");
    return ((OperandAssertion) this.parent).parentList().parentFunction();
  }
}
