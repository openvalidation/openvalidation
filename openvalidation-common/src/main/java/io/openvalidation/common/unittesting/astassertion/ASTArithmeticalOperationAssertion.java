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

import io.openvalidation.common.ast.ASTArithmeticalOperator;
import io.openvalidation.common.ast.ASTModel;
import io.openvalidation.common.ast.operand.arithmetical.ASTOperandArithmeticalItemBase;
import io.openvalidation.common.ast.operand.arithmetical.ASTOperandArithmeticalOperation;

public class ASTArithmeticalOperationAssertion
    extends ASTOperandArithmeticalItemAssertionBase<
        ASTOperandArithmeticalOperation, ASTArithmeticalOperationAssertion> {
  public ASTArithmeticalOperationAssertion(
      ASTOperandArithmeticalOperation item, int index, ASTModel ast, ASTAssertionBase parent) {
    super(item, index, ast, parent);
  }

  public ASTArithmeticalOperationAssertion(
      ASTOperandArithmeticalOperation item, ASTModel ast, ASTAssertionBase parent) {
    super(item, ast, parent);
  }

  public ArithmeticOperandAssertion first() {
    shouldHaveMinSizeOf(this.model.filterNonOperations(), 1, "ARITHMETICAL OPERAND");
    ASTOperandArithmeticalItemBase item = this.model.filterNonOperations().get(0);

    return new ArithmeticOperandAssertion(item, 0, this.ast, this);
  }

  public ArithmeticOperandAssertion second() {
    shouldHaveMinSizeOf(this.model.filterNonOperations(), 2, "ARITHMETICAL OPERAND");
    ASTOperandArithmeticalItemBase item = this.model.filterNonOperations().get(1);

    return new ArithmeticOperandAssertion(item, 1, this.ast, this);
  }

  public ArithmeticOperandAssertion atIndex(int i) {
    shouldHaveMinSizeOf(this.model.filterNonOperations(), i + 1, "ARITHMETICAL OPERAND");
    ASTOperandArithmeticalItemBase item = this.model.filterNonOperations().get(i);

    return new ArithmeticOperandAssertion(item, i, this.ast, this);
  }

  public ASTArithmeticalOperationAssertion firstSubOperation() {
    shouldHaveMinSizeOf(this.model.filterOperations(), 1, "SUBOPERATION");
    ASTOperandArithmeticalOperation operation = this.model.filterOperations().get(0);
    return new ASTArithmeticalOperationAssertion(operation, 0, this.ast, this);
  }

  public ASTArithmeticalOperationAssertion secondSubOperation() {
    shouldHaveMinSizeOf(this.model.filterOperations(), 2, "SUBOPERATION");
    ASTOperandArithmeticalOperation operation = this.model.filterOperations().get(1);
    return new ASTArithmeticalOperationAssertion(operation, 1, this.ast, this);
  }

  public ASTArithmeticalOperationAssertion subOperationAtIndex(int index) {
    shouldHaveMinSizeOf(this.model.filterOperations(), index + 1, "SUBOPERATION");
    ASTOperandArithmeticalOperation operation = this.model.filterOperations().get(index);
    return new ASTArithmeticalOperationAssertion(operation, index, this.ast, this);
  }

  public ASTArithmeticalOperationAssertion hasSizeOf(int i) {
    shouldEquals(this.model.getItems().size(), i, "ARITHMETICAL ITEM LIST SIZE");

    return this;
  }

  @Override
  public ASTArithmeticalOperationAssertion hasOperator(ASTArithmeticalOperator operator) {
    return (ASTArithmeticalOperationAssertion) super.hasOperator(operator);
  }

  public ASTArithmeticalOperationAssertion parentOperation() {
    shouldBeInstanceOf(this.parent, ASTArithmeticalOperationAssertion.class, "PARENT");

    return (ASTArithmeticalOperationAssertion) this.parent;
  }

  public ConditionAssertion parentCondition() {
    return parentArithmetical().parentCondition();
  }

  public OperandArithmeticalAssertion parentArithmetical() {
    shouldBeInstanceOf(this.parent, OperandArithmeticalAssertion.class, "PARENT");

    return (OperandArithmeticalAssertion) this.parent();
  }

  public ASTArithmeticalOperationAssertion hasNoOperator() {
    shouldBeNull(this.model.getOperator(), "OPERATOR");

    return this;
  }
}
