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

import io.openvalidation.common.ast.operand.arithmetical.ASTOperandArithmetical;
import io.openvalidation.common.exceptions.ASTValidationException;
import io.openvalidation.core.validation.ValidatorBase;
import io.openvalidation.core.validation.ValidatorFactory;

public class ASTArithmeticalValidator extends ValidatorBase {

  private ASTOperandArithmetical _arithmeticalOperand;

  public ASTArithmeticalValidator(ASTOperandArithmetical arithmeticalOperand) {
    this._arithmeticalOperand = arithmeticalOperand;
  }

  @Override
  public void validate() throws Exception {
    if (this._arithmeticalOperand == null || this._arithmeticalOperand.getOperation() == null) {
      throw new ASTValidationException(
          "an arithmetical operand should not be null or empty.",
          this._arithmeticalOperand,
          this.globalPosition);
    }

    ValidatorBase validator = ValidatorFactory.Create(this._arithmeticalOperand.getOperation());
    validator.validate();
  }
}
