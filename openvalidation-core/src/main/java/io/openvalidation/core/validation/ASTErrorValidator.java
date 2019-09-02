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

import io.openvalidation.common.ast.ASTActionError;
import io.openvalidation.common.exceptions.ASTValidationException;
import io.openvalidation.common.utils.StringUtils;

public class ASTErrorValidator extends ValidatorBase {
  private ASTActionError _error;

  public ASTErrorValidator(ASTActionError error) {
    this._error = error;
  }

  @Override
  public void validate() throws Exception {
    String errorMessage = this._error.getErrorMessage();

    if (StringUtils.isNullOrEmpty(errorMessage))
      throw new ASTValidationException("a Rule should contains an error message", _error);

    if (this._error.getErrorCode() != null && this._error.getErrorCode() == -1)
      throw new ASTValidationException("error code should not be empty", _error);
  }
}
