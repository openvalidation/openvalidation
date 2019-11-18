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
import io.openvalidation.common.ast.ASTVariable;
import io.openvalidation.common.ast.operand.ASTOperandVariable;
import io.openvalidation.common.data.DataPropertyType;

public class VariableReferenceAssertion
    extends ASTItemAssertionBase<ASTOperandVariable, ASTAssertionBase, VariableReferenceAssertion> {

  public VariableReferenceAssertion(
      ASTOperandVariable item, ASTModel ast, ASTAssertionBase parent) {
    super(item, ast, parent);
  }

  public VariableReferenceAssertion hasName(String expectedName) {
    this.shouldNotBeEmpty();

    shouldEquals(this.model.getVariableName(), expectedName, "VARIABLE NAME");

    return this;
  }

  public VariableReferenceAssertion hasType(DataPropertyType type) {
    this.shouldNotBeEmpty();

    shouldEquals(this.model.getDataType(), type, "VARIABLE DATA TYPE");

    return this;
  }

  public VariableReferenceAssertion hasArrayContentType(DataPropertyType type) {
    this.shouldNotBeEmpty();

    shouldEquals(this.model.getArrayContentType(), type, "VARIABLE CONTENT DATA TYPE");

    return this;
  }

  public VariableReferenceAssertion hasPath(String expectedPath) {
    this.shouldNotBeEmpty();

    shouldEquals(this.model.getPathAsString(), expectedPath, "VARIABLE PATH");

    return this;
  }

  public OperandAssertion parentOperand() {
    shouldBeInstanceOf(this.parent(), OperandAssertion.class, "PARENT OPERAND");

    return (OperandAssertion) this.parent();
  }

  public FunctionAssertion parentFunction() {
    return parentOperand().parentList().parentFunction();
  }

  public ConditionAssertion parentCondition() {
    return parentOperand().parentCondition();
  }

  public ModelRootAssertion parentModel() {
    return parentCondition().parentModel();
  }

  public VariableAssertion variableValue() {
    shouldBeInstanceOf(this.model.getVariable(), ASTVariable.class, "VARIABLE VALUE");

    return new VariableAssertion(this.model.getVariable(), this.ast, this);
  }
}
