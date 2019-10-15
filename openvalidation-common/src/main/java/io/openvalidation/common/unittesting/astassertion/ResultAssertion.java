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

import io.openvalidation.common.exceptions.ASTValidationException;
import io.openvalidation.common.model.OpenValidationResult;
import io.openvalidation.common.utils.LINQ;
import io.openvalidation.common.utils.StringUtils;

public class ResultAssertion extends ASTAssertionBase<ModelRootAssertion> {
  private OpenValidationResult result;
  private ModelRootAssertion _modelRootAssertion;

  public ResultAssertion(OpenValidationResult result) {
    super("RESULT", result.getASTModel(), null);
    this.result = result;
  }

  public ResultAssertion hasErrors() {
    shouldNotBeEmpty(this.result, "RESULT");
    shouldBeTrue(this.result.hasErrors(), "HAS ERRORS");

    return this;
  }

  public ASTValidationAssertion containsValidationMessage(String error) {
    ASTValidationException exception =
        LINQ.findFirst(
            result.getValidationErros(),
            e -> e.getUserMessage() != null && e.getUserMessage().contains(error));

    if (exception == null) {
      // result.getRuleSetPrint();

      writeExpectedAndActual(
          "VALIDATION MESSAGE  : " + error,
          "VALIDATION MESSAGES : "
              + StringUtils.join(result.getAllErrorMessages(), "\n                      "));
    }

    return new ASTValidationAssertion("VALIDATION EXCEPTION", exception, this);
  }

  public ResultAssertion containsErrorMessage(String error) {
    boolean hasMessage =
        LINQ.any(
            result.getErrors(),
            e -> e.getUserMessage() != null && e.getUserMessage().contains(error));

    if (!hasMessage) {
      // result.getRuleSetPrint();

      writeExpectedAndActual(
          "ERROR MESSAGE  : '" + error + "'",
          "ERROR MESSAGES -> \n\n" + StringUtils.join(result.getAllErrorMessages(), "\n"));
    }

    return this;
  }

  public static ResultAssertion assertResult(OpenValidationResult result) throws Exception {
    ResultAssertion asr = new ResultAssertion(result);
    asr.shouldNotBeNull(result, "RESULT");

    return asr;
  }
}
