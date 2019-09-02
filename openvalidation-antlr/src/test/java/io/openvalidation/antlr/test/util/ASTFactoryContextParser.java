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

package io.openvalidation.antlr.test.util;

import io.openvalidation.antlr.MainASTBuildListener;
import io.openvalidation.antlr.generated.mainLexer;
import io.openvalidation.antlr.generated.mainParser;
import io.openvalidation.antlr.transformation.TransformerContext;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;

public class ASTFactoryContextParser {
  public static TransformerContext run(String input) {
    CharStream inputStream = CharStreams.fromString(input);

    mainLexer lexer = new mainLexer(inputStream);
    TokenStream tokens = new CommonTokenStream(lexer);
    mainParser parser = new mainParser(tokens);
    parser.removeErrorListeners();

    MainASTBuildListener astBuildListener = new MainASTBuildListener(null);
    parser.addParseListener(astBuildListener);

    parser.main();
    TransformerContext factoryContext = astBuildListener.get_factoryContext();
    return factoryContext;
  }
}
