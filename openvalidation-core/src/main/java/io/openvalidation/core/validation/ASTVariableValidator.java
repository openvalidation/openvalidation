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

package io.openvalidation.core.validation;

import static io.openvalidation.common.validation.ASTValidator.shouldNotBeEmpty;

import io.openvalidation.common.ast.ASTVariable;
import io.openvalidation.common.ast.operand.ASTOperandBase;

public class ASTVariableValidator extends ValidatorBase {
  private ASTVariable _variable;

  public ASTVariableValidator(ASTVariable variable) {
    this._variable = variable;
  }

  @Override
  public void validate() throws Exception {
    String name = this._variable.getName();
    ASTOperandBase value = this._variable.getValue();

    shouldNotBeEmpty(name, "the name of Variable", _variable);
    shouldNotBeEmpty(value, "the value of Variable", _variable);

    validate(value, this._variable.getGlobalPosition());
  }
}
