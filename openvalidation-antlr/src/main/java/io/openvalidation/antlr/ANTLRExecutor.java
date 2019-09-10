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

package io.openvalidation.antlr;

import io.openvalidation.antlr.generated.mainLexer;
import io.openvalidation.antlr.generated.mainParser;
import io.openvalidation.common.ast.ASTModel;
import io.openvalidation.common.data.DataSchema;
import io.openvalidation.common.unittesting.astassertion.ModelRootAssertion;
import io.openvalidation.common.utils.ThrowingConsumer;
import java.util.logging.*;
import org.antlr.v4.runtime.*;

public class ANTLRExecutor {
  public static ASTModel run(String input) {
    return run(input, null);
  }

  public static ASTModel run(String input, DataSchema schema) {
    return run(input, schema, null);
  }

  public static ASTModel run(
      String input, DataSchema schema, ThrowingConsumer<ModelRootAssertion> function) {
    ASTModel ast = null;

    try {
      CharStream inputStream = CharStreams.fromString(input.trim() + " ");

      mainLexer lexer = new mainLexer(inputStream);
      TokenStream tokens = new CommonTokenStream(lexer);
      mainParser parser = new mainParser(tokens);
      parser.removeErrorListeners();

      MainASTBuildListener astBuildListener = new MainASTBuildListener(schema);
      parser.addParseListener(astBuildListener);

      parser.main();
      ast = astBuildListener.getAST();

      if (function != null) function.accept(new ModelRootAssertion(ast));

      return ast;
    } catch (Exception exp) {
      Logger logger = Logger.getGlobal();
      logger.log(Level.SEVERE, "PREPROCESSED RULE: \\n\" + input + \"\\n\"");
      if (schema != null) logger.log(Level.SEVERE, "DataSchema: \n" + schema.toString() + "\n");

      logger.log(Level.SEVERE, exp.toString() + "\n\n" + exp.getMessage());
      logger.log(Level.FINE, exp.getStackTrace().toString());
      throw exp;
    }
  }
}
