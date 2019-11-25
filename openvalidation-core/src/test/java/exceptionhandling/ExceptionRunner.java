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

package exceptionhandling;

import static io.openvalidation.common.unittesting.astassertion.ResultAssertion.assertResult;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

import io.openvalidation.common.exceptions.OpenValidationException;
import io.openvalidation.common.model.OpenValidationResult;
import io.openvalidation.common.unittesting.astassertion.ResultAssertion;
import io.openvalidation.common.utils.StringUtils;
import io.openvalidation.core.OpenValidation;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ExceptionRunner {

  public void run(String rule, Consumer<ResultAssertion> function) throws Exception {
    run(rule, "{abcdef:0}", function);
  }

  public void run(String rule, String schema, Consumer<ResultAssertion> function) throws Exception {

    OpenValidation ov = OpenValidation.createDefault();
    OpenValidationResult result = null;

    try {

      ov.setLocale("en");
      ov.setSchema(
          StringUtils.isNullOrEmpty(schema) || schema.equals("{}") ? "{abcdef:0}" : schema);
      ov.setRule(rule.replace("\\n", "\n").replace("\\r", "\r"));
      result = ov.generateCode(true);

    } catch (OpenValidationException exp) {
      result = new OpenValidationResult();
      result.addError(exp);
    } catch (Exception exp) {
      result = new OpenValidationResult();
      result.addError(new OpenValidationException(exp.getMessage(), exp));
    }

    assertThat(result, notNullValue());

    try {
      function.accept(assertResult(result).hasErrors());
    } catch (Exception exp) {
      Logger logger = Logger.getGlobal();

      logger.log(Level.FINE, result.getRuleSetPrint());

      if (result.getASTModel() != null) logger.log(Level.FINE, result.getASTModelPrint());

      logger.log(Level.FINE, result.getErrorPrint(true));
      System.out.println(result.getErrorPrint(true));

      assertThat("AST VALIDATION ERRORS", false);
    }

    //        return assertResult(result)
    //                           .hasErrors();
  }
}
