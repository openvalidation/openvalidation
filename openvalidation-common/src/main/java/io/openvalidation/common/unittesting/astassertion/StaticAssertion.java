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
import io.openvalidation.common.ast.operand.ASTOperandStatic;

public abstract class StaticAssertion<ASTOperand extends ASTOperandStatic>
    extends ASTItemAssertionBase<ASTOperand, ASTAssertionBase, StaticAssertion> {

  public StaticAssertion(ASTOperand item, ASTModel ast, ASTAssertionBase parent) {
    super(item, ast, parent);
  }

  public StaticAssertion hasValue(String value) {
    shouldNotBeEmpty(model.getValue(), "Value");
    shouldEquals(model.getValue(), value, "Value");

    return this;
  }

  public OperandAssertion parentOperand() {
    shouldBeInstanceOf(this.parent(), OperandAssertion.class, "PARENT OPERAND");

    return (OperandAssertion) this.parent();
  }

  public ConditionAssertion parentCondition() {
    return parentOperand().parentCondition();
  }

  public ConditionGroupAssertion parentConditionGroup() {
    return parentOperand().parentCondition().parentConditionGroup();
  }

  public ModelRootAssertion parentModel() {
    return parentCondition().parentModel();
  }
}
