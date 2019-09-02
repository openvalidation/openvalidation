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

package io.openvalidation.antlr.test.sourcetests;

import static io.openvalidation.common.unittesting.astassertion.ModelRootAssertion.assertAST;
import static io.openvalidation.common.unittesting.astassertion.ModelRootAssertion.assertRules;

import io.openvalidation.antlr.ANTLRExecutor;
import io.openvalidation.common.ast.ASTModel;
import io.openvalidation.common.utils.GrammarBuilder;
import org.junit.jupiter.api.Test;

public class ConditionTest {
  // ast condition

  @Test
  void source_simple_condition() throws Exception {
    String input = GrammarBuilder.create().EQ("tree", "bush").getText();

    ASTModel ast = ANTLRExecutor.run(input);

    assertAST(ast).hasPreprocessedSource(input);
  }

  @Test
  void source_simple_if_condition() throws Exception {
    String input = GrammarBuilder.create().IF().EQ("tree", "bush").THEN("error").getText();

    ASTModel ast = ANTLRExecutor.run(input);

    assertAST(ast).hasPreprocessedSource(input);
  }

  @Test
  void source_if_condition_variable_string() throws Exception {
    String input =
        GrammarBuilder.create()
            .with("active")
            .AS("Status")
            .PARAGRAPH()
            .IF()
            .EQ("Status", "new")
            .THEN("error")
            .getText();

    ASTModel ast = ANTLRExecutor.run(input);

    assertAST(ast).hasPreprocessedSource(input);
  }

  // ast condition group

  @Test
  void source_if_condition_group_numbers() throws Exception {
    String input =
        GrammarBuilder.create()
            .IF()
            .with(50)
            .GREATER_THAN(100)
            .OR()
            .with(50)
            .LESS_THAN(0)
            .THEN("error")
            .getText();

    ASTModel ast = ANTLRExecutor.run(input);

    assertAST(ast).hasPreprocessedSource(input);
  }

  @Test
  void source_if_condition_group_variable_string() throws Exception {
    String input =
        GrammarBuilder.create()
            .with("active")
            .AS("Status")
            .PARAGRAPH()
            .IF()
            .EQ("Status", "new")
            .OR()
            .EQ("Status", "inactive")
            .THEN("error")
            .getText();

    ASTModel ast = ANTLRExecutor.run(input);

    assertAST(ast).hasPreprocessedSource(input);
  }

  @Test
  void test_condition_in_condition_group() throws Exception {
    String input =
        GrammarBuilder.create()
            .IF()
            .with(50)
            .GREATER_THAN(100)
            .OR()
            .with(50)
            .LESS_THAN(0)
            .THEN("error")
            .getText();

    String expectedFirst = GrammarBuilder.create().with(50).GREATER_THAN(100).getText();

    String expectedSecond = GrammarBuilder.create().OR().with(50).LESS_THAN(0).getText();

    ASTModel ast = ANTLRExecutor.run(input);

    assertRules(ast)
        .first()
        .conditionGroup()
        .first()
        .hasPreprocessedSource(expectedFirst)
        .parentConditionGroup()
        .second()
        .hasPreprocessedSource(expectedSecond);
  }
}
