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

package io.openvalidation.common.validation;

import io.openvalidation.common.ast.ASTItem;
import io.openvalidation.common.exceptions.ASTValidationException;
import java.util.List;

public class ASTValidator {

  public static <T> void shouldNotBeEmpty(
      T value, String paramName, ASTItem item, String... customMessage)
      throws ASTValidationException {
    if (value == null)
      throw new ASTValidationException(
          getErrorMessage(Validator.ValidatorErrorKind.NotEmpty, paramName, customMessage), item);

    if (value instanceof String && ((String) value).length() < 1)
      throw new ASTValidationException(
          getErrorMessage(Validator.ValidatorErrorKind.NotEmpty, paramName, customMessage), item);

    if (value instanceof List<?> && ((List<?>) value).size() < 1)
      throw new ASTValidationException(
          getErrorMessage(Validator.ValidatorErrorKind.NotEmpty, paramName, customMessage), item);
  }

  private static String getErrorMessage(
      Validator.ValidatorErrorKind kind, String paramName, String... customMessage) {
    if (customMessage != null && customMessage.length > 0) {
      String error = String.join(" ", customMessage);
      if (error.length() > 0) return error;
    }

    return Validator.getUserErrorMessage(kind, paramName);
  }
}
