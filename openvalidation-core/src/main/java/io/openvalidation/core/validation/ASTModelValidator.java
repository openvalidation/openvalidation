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

import io.openvalidation.common.ast.ASTGlobalElement;
import io.openvalidation.common.exceptions.ASTValidationException;
import io.openvalidation.common.exceptions.ASTValidationSummaryException;
import io.openvalidation.common.log.ProcessLogger;
import io.openvalidation.common.utils.Constants;
import io.openvalidation.common.utils.StringUtils;

public class ASTModelValidator extends ValidatorBase {

  public ASTModelValidator(ValidationContext context) {
    super(context);
  }

  @Override
  public void validate() throws Exception {
    ASTValidationSummaryException exceptionSummary = new ASTValidationSummaryException(ast);

    try {
      if (ast.hasSource() && !ast.hasValidElements()) {

        if (!ast.getPreprocessedSource().contains(Constants.KEYWORD_SYMBOL)) {
          if (StringUtils.match(ast.getPreprocessedSource(), "\\b[A-Z]+\\b"))
            throw new ASTValidationException(
                "the content of Rule Set doesn't match current language/culture '"
                    + context.getOptions().getLocale()
                    + "'",
                ast);
        }

        throw new ASTValidationException("the Rule Set has invalid content", ast);
      }

      for (ASTGlobalElement element : ast.getElements()) {
        try {
          validate(element);
        } catch (ASTValidationException exp) {
          exceptionSummary.add(exp);
        }
      }

      if (exceptionSummary.hasErrors()) throw exceptionSummary;

      ProcessLogger.success(ProcessLogger.VALIDATOR);
    } catch (Exception e) {
      ProcessLogger.error(ProcessLogger.VALIDATOR, e);
      throw e;
    }
  }
}
