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
import io.openvalidation.common.ast.operand.property.ASTOperandProperty;
import io.openvalidation.common.ast.operand.property.ASTPropertyStaticPart;
import io.openvalidation.common.data.DataPropertyType;
import io.openvalidation.common.unittesting.astassertion.lists.OperandListAssertion;

import java.util.List;
import java.util.stream.Collectors;

public class PropertyAssertion
    extends ASTItemAssertionBase<ASTOperandProperty, ASTAssertionBase, PropertyAssertion> {

  public PropertyAssertion(ASTOperandProperty item, ASTModel ast, ASTAssertionBase parent) {
    super(item, ast, parent);
  }

  public PropertyAssertion hasPath(String path) {
    shouldNotBeEmpty();
    shouldEquals(this.model.getPathAsString(), path, "PROPERTY PATH");

    return this;
  }

  public PropertyAssertion hasLambda(String token) {
    shouldNotBeEmpty();
    shouldEquals(this.model.getLambdaToken(), token, "PROPERTY LAMBDA");

    return this;
  }

  public PropertyAssertion tokenStartsWith(String expectedStart) {
    shouldNotBeEmpty();
    this.shouldNotBeEmpty(this.model.getLambdaToken(), "PROPERTY LAMBDA TOKEN");
    this.shouldStartsWith(this.model.getLambdaToken(), expectedStart, "PROPERTY LAMBDA TOKEN");

    return this;
  }

  public PropertyAssertion hasType(DataPropertyType type) {
    shouldNotBeEmpty();
    shouldEquals(model.getDataType(), type, "DATA PROPERTY TYPE");

    return this;
  }

  public OperandAssertion parentOperand() {
    shouldBeInstanceOf(this.parent(), OperandAssertion.class, "PARENT OPERAND");

    return (OperandAssertion) this.parent();
  }

  public VariableAssertion parentVariable() {
    return parentOperand().parentVariable();
  }

  public FunctionAssertion parentFunction() {
    return parentOperand().parentList().parentFunction();
  }

  public ConditionAssertion parentCondition() {
    return parentOperand().parentCondition();
  }

  public ConditionGroupAssertion parentConditionGroup() {
    return parentCondition().parentConditionGroup();
  }

  public ModelRootAssertion parentModel() {
    if (this.parent() instanceof VariableAssertion)
      return ((VariableAssertion) this.parent()).parentModel();
    else return parentCondition().parentModel();
  }

  public PropertyStaticPartAssertion staticPart(int index) {
    List<ASTPropertyStaticPart> parts =
        model.getPath().stream()
            .filter(p -> p instanceof ASTPropertyStaticPart)
            .map(p -> (ASTPropertyStaticPart) p)
            .collect(Collectors.toList());
    shouldHaveMinSizeOf(parts, index + 1, "PROPERTY PART LIST SIZE");
    shouldBeInstanceOf(
        this.model.getPath().get(index), ASTPropertyStaticPart.class, "PROPERTY STATIC PART");

    return new PropertyStaticPartAssertion(
        (ASTPropertyStaticPart) this.model.getPath().get(index), ast, this);
  }

  public PropertyStaticPartAssertion firstStaticPart() {
    return staticPart(0);
  }

  public PropertyStaticPartAssertion secondStaticPart() {
    return staticPart(1);
  }

  public PropertyStaticPartAssertion thirdStaticPart() {
    return staticPart(2);
  }

  @Override
  protected String getIdentifier() {
    return (this.model != null) ? this.model.getPathAsString() : null;
  }


  //parents
  public OperandListAssertion parentList()
  {
    ASTAssertionBase p = parent;
    if(p instanceof OperandAssertion)
    {
        p = ((OperandAssertion) parent).parentList();
    }
    shouldBeInstanceOf(p ,OperandListAssertion.class, "PARENT OPERAND LIST");
    return (OperandListAssertion) p;
  }
}
