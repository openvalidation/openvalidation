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

public class ASTOperandArithmeticalItemAssertionBase<
        T extends ASTOperandArithmeticalItemBase, S extends ASTAssertionBase>
    extends ASTItemAssertionBase<T, ASTAssertionBase, S> {
  public ASTOperandArithmeticalItemAssertionBase(
      T item, int index, ASTModel ast, ASTAssertionBase parent) {
    super(item, index, ast, parent);
  }

  public ASTOperandArithmeticalItemAssertionBase(T item, ASTModel ast, ASTAssertionBase parent) {
    super(item, ast, parent);
  }

  protected ASTOperandArithmeticalItemAssertionBase hasOperator(ASTArithmeticalOperator operator) {
    shouldNotBeNull(operator, "OPERATOR");
    shouldEquals(operator, this.model.getOperator(), "OPERATOR");
    return this;
  }
}
