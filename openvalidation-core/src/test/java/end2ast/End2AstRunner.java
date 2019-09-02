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

package end2ast;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

import io.openvalidation.common.exceptions.OpenValidationException;
import io.openvalidation.common.model.OpenValidationResult;
import io.openvalidation.common.unittesting.astassertion.ModelRootAssertion;
import io.openvalidation.common.utils.Console;
import io.openvalidation.common.utils.StringUtils;
import io.openvalidation.common.utils.ThrowingConsumer;
import io.openvalidation.core.OpenValidation;

public class End2AstRunner {

  public static OpenValidationResult run(String rule) throws Exception {
    return run(rule, "{abcdefg:0}", "en", null);
  }

  public static OpenValidationResult run(String rule, String schema) throws Exception {
    return run(rule, schema, "en", null);
  }

  public static OpenValidationResult run(String rule, String schema, String culture)
      throws Exception {
    return run(rule, schema, culture, null);
  }

  public static OpenValidationResult run(
      String rule, String schema, ThrowingConsumer<ModelRootAssertion> function) throws Exception {
    return run(rule, schema, "en", function);
  }

  public static OpenValidationResult run(
      String rule, String schema, String culture, ThrowingConsumer<ModelRootAssertion> function)
      throws Exception {
    OpenValidation ov = OpenValidation.createDefault();
    OpenValidationResult result = null;

    try {

      ov.setLocale(culture);
      ov.setSchema(
          StringUtils.isNullOrEmpty(schema) || schema.equals("{}") ? "{abcdefg:0}" : schema);
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

    if (result.hasErrors()) {
      Console.print(result.getRuleSetPrint());

      if (result.getASTModel() != null) System.out.print(result.getASTModelPrint());

      System.out.print(result.getErrorPrint(true));

      assertThat("AST VALIDATION ERRORS", false);
    }

    if (function != null) {
      try {
        ModelRootAssertion rootAssertions = ModelRootAssertion.assertResult(result);
        function.accept(rootAssertions);
      } catch (Exception exp) {
        Console.error(exp.getMessage() + "\n");

        Console.print(result.getRuleSetPrint());

        if (result.getASTModel() != null) System.out.print(result.getASTModelPrint());

        System.out.print(result.getErrorPrint(true));

        // Console.error(exp.toString());

        exp.printStackTrace();

        assertThat("AST VALIDATION ERRORS", false);
      }
    }

    return result;
  }
}
