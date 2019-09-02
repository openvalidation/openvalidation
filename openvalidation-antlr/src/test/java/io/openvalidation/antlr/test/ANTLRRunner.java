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

package io.openvalidation.antlr.test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;

import io.openvalidation.antlr.ANTLRExecutor;
import io.openvalidation.common.ast.ASTModel;
import io.openvalidation.common.converter.SchemaConverterFactory;
import io.openvalidation.common.data.DataSchema;
import io.openvalidation.common.exceptions.ASTValidationSummaryException;
import io.openvalidation.common.unittesting.astassertion.ModelRootAssertion;
import io.openvalidation.common.utils.Console;
import io.openvalidation.common.utils.StringUtils;
import io.openvalidation.common.utils.ThrowingConsumer;

public class ANTLRRunner {

  public static ASTModel run(String rule) throws Exception {
    return run(rule, "{abcdefg:0}", null);
  }

  public static ASTModel run(String rule, String schema) throws Exception {
    return run(rule, schema, null);
  }

  public static ASTModel run(
      String rule, String rawSchema, ThrowingConsumer<ModelRootAssertion> function)
      throws Exception {
    rawSchema =
        StringUtils.isNullOrEmpty(rawSchema) || rawSchema.equals("{}") ? "{abcdefg:0}" : rawSchema;
    DataSchema schema = SchemaConverterFactory.convert(rawSchema);
    ASTModel ast = null;
    boolean astTransformationFailed = false;

    try {
      ast = ANTLRExecutor.run(rule, schema);
    } catch (Exception e) {
      Console.print("ERRORS OCCURRED DURING AST TRANSFORMATION\n");
      if (e.getCause() instanceof ASTValidationSummaryException) {
        ast = ((ASTValidationSummaryException) e.getCause()).getModel();
        astTransformationFailed = true;
      } else {
        e.printStackTrace();
      }
    }

    assertThat(ast, notNullValue());

    if (astTransformationFailed) {
      printPreprocessedInputInConsole(ast.getPreprocessedSource());
      printAstInConsole(ast);
    }

    if (function != null) {
      try {
        ModelRootAssertion rootAssertions = ModelRootAssertion.assertAST(ast);
        function.accept(rootAssertions);
      } catch (Exception exp) {
        Console.error(exp.getMessage() + "\n");

        exp.printStackTrace();

        assertThat("AST VALIDATION ERRORS", false);
      }
    }
    return ast;
  }

  private static void printAstInConsole(ASTModel ast) {
    String sb = Console.getTitleStart("AST MODEL") + ast.print() + "\n\n\n";
    Console.print(sb);
  }

  private static void printPreprocessedInputInConsole(String preprocessedInput) {
    String sb = Console.getTitleStart("PREPROCESSED INPUT") + preprocessedInput + "\n\n";
    Console.print(sb);
  }
}
