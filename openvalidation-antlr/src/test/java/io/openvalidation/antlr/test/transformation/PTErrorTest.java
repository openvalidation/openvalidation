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

package io.openvalidation.antlr.test.transformation;

import static io.openvalidation.common.unittesting.astassertion.ModelRootAssertion.assertRules;

import io.openvalidation.antlr.ANTLRExecutor;
import io.openvalidation.common.ast.ASTModel;
import io.openvalidation.common.utils.GrammarBuilder;
import org.junit.jupiter.api.Test;

class PTErrorTest {

  @Test
  void should_transform_simple_error() {
    String input = GrammarBuilder.createRule().EQ("A", "B").THEN("this is an error").getText();

    ASTModel ast = ANTLRExecutor.run(input);

    assertRules(ast).hasSizeOf(1).first().hasError("this is an error");
  }

  @Test
  void should_not_contains_error() {
    String input = GrammarBuilder.createRule().EQ("A", "B").getText();

    ASTModel ast = ANTLRExecutor.run(input);

    assertRules(ast).hasSizeOf(1).first().hasNoError();
  }

  @Test
  void should_transform_error_with_code() {
    String input =
        GrammarBuilder.createRule().EQ("A", "B").THEN("this is an error").ERRORCODE(1234).getText();

    ASTModel ast = ANTLRExecutor.run(input);

    assertRules(ast).hasSizeOf(1).first().hasError("this is an error").hasErrorCode(1234);
  }
}
