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

package io.openvalidation.core.validation.operand.arithmetical;

import io.openvalidation.common.ast.operand.arithmetical.ASTOperandArithmeticalItemBase;
import io.openvalidation.common.ast.operand.arithmetical.ASTOperandArithmeticalOperation;
import io.openvalidation.common.exceptions.ASTValidationException;
import io.openvalidation.common.utils.LINQ;
import io.openvalidation.core.validation.ValidatorBase;
import io.openvalidation.core.validation.ValidatorFactory;
import java.util.List;

public class ASTArithmeticalOperationValidator extends ValidatorBase {

  private ASTOperandArithmeticalOperation _arithmeticalOperation;

  public ASTArithmeticalOperationValidator(ASTOperandArithmeticalOperation arithmeticalOperation) {
    this._arithmeticalOperation = arithmeticalOperation;
  }

  @Override
  public void validate() throws Exception {
    List<ASTOperandArithmeticalItemBase> items = this._arithmeticalOperation.getItems();
    List<ASTOperandArithmeticalItemBase> itemsWithValues =
        LINQ.where(
            items,
            i ->
                (i.getOperand() != null
                    || (i instanceof ASTOperandArithmeticalOperation
                        && ((ASTOperandArithmeticalOperation) i).getItems() != null)));

    if (itemsWithValues.size() < 1)
      throw new ASTValidationException(
          "missing all values in arithmetical operation", _arithmeticalOperation, this.globalPosition);

    if (items.size() < 2 || itemsWithValues.size() < items.size())
      throw new ASTValidationException(
          "missing value in arithmetical operation", _arithmeticalOperation, this.globalPosition);

    if (LINQ.count(itemsWithValues, i -> i.isNumber()) < items.size())
      throw new ASTValidationException(
          "all values of an arithmetical operation should be numbers", _arithmeticalOperation, this.globalPosition);

    boolean first = true;
    for (ASTOperandArithmeticalItemBase i : items) {

      // each second item should be an arithmetical Operator
      if (first) {
        first = false;
      } else {
        if (i.getOperator() == null)
          throw new ASTValidationException("missing arithmetical operator.", i, this.globalPosition);
      }

      ValidatorBase validator = ValidatorFactory.Create(i);
      validator.validate();
    }
  }
}
