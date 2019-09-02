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

import static io.openvalidation.common.unittesting.astassertion.ModelRootAssertion.*;

import io.openvalidation.antlr.ANTLRExecutor;
import io.openvalidation.common.ast.ASTModel;
import io.openvalidation.common.utils.GrammarBuilder;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class GlobalElementTest {
  // ast model
  @Test
  void test_single_rule_as_ast() throws Exception {
    String input = GrammarBuilder.createRule().EQ("3", "7").THEN("error message").getText();

    ASTModel ast = ANTLRExecutor.run(input);

    assertAST(ast).hasPreprocessedSource(input);
  }

  @Test
  void test_single_rule_constrained_as_ast() throws Exception {
    String input =
        GrammarBuilder.createRule().with(3).MUST().with(7).THEN("error message").getText();

    ASTModel ast = ANTLRExecutor.run(input);

    assertAST(ast).hasPreprocessedSource(input);
  }

  @Test
  void test_single_comment_as_ast() throws Exception {
    String input =
        GrammarBuilder.createRule().COMMENT("Hello my dear friend, I am a comment").getText();

    ASTModel ast = ANTLRExecutor.run(input);

    assertAST(ast).hasPreprocessedSource(input);
  }

  @Test
  void test_single_variables_as_ast() throws Exception {
    String input = GrammarBuilder.createRule().with(3).MUST().with(7).AS("variable").getText();

    ASTModel ast = ANTLRExecutor.run(input);

    assertAST(ast).hasPreprocessedSource(input);
  }

  @Test
  void test_multiple_global_elements_with_paragraphs() throws Exception {
    String input =
        GrammarBuilder.create()
            .IF()
            .EQ("tree", "bush")
            .AND()
            .with(3)
            .GREATER_THAN(7)
            .OR()
            .with(3)
            .EQ()
            .with(7)
            .THEN("error")
            .PARAGRAPH()
            .with(123456789)
            .AS("a big number")
            .PARAGRAPH()
            .COMMENT("This is just a comment :)")
            .getText();

    ASTModel ast = ANTLRExecutor.run(input);

    assertAST(ast).hasPreprocessedSource(input);
  }

  // ast unknown
  @Test
  void test_unknown_with_random_keywords() throws Exception {
    String input =
        GrammarBuilder.create()
            .AND()
            .AS("giraffes are")
            .ADD("beautiful animals")
            .MUST("be sent from heaven")
            .OR("length of neck")
            .THEN("error")
            .getText();

    ASTModel ast = ANTLRExecutor.run(input);

    assertAST(ast).hasPreprocessedSource(input);
  }

  @Test
  @Disabled
  void test_unknown_with_random_keywords_between_other_globals() throws Exception {
    String input =
        GrammarBuilder.create()
            .COMMENT("I am above some nonsense")
            .PARAGRAPH()
            .AND()
            .AS("giraffes are")
            .ADD("beautiful animals")
            .MUST("be sent from heaven")
            .OR("length of neck")
            .THEN("error")
            .PARAGRAPH()
            .COMMENT("I am below some nonsense")
            .getText();

    String expectedSource =
        GrammarBuilder.create()
            .AND()
            .AS("giraffes are")
            .ADD("beautiful animals")
            .MUST("be sent from heaven")
            .OR("length of neck")
            .THEN("error")
            .getText();

    ASTModel ast = ANTLRExecutor.run(input);

    assertUnknown(ast).first().hasPreprocessedSource(expectedSource);
  }

  // ast rule

  @Test
  void test_rule_alone() throws Exception {
    String input = GrammarBuilder.create().EQ("3", "7").THEN("error").getText();

    ASTModel ast = ANTLRExecutor.run(input);

    assertRules(ast).first().hasPreprocessedSource(input);
  }

  @Test
  void test_rule_with_spacing_whitespaces() throws Exception {
    String input =
        GrammarBuilder.createRule()
            .with("      a very simple  ")
            .EQ()
            .with("    expression   ")
            .THEN("error")
            .getText();

    ASTModel ast = ANTLRExecutor.run(input);

    assertRules(ast).first().hasPreprocessedSource(input);
  }

  @Test
  void test_rule_with_spacing_newlines_UNIX() throws Exception {
    String input =
        GrammarBuilder.createRule()
            .with("      a\n very \nsimple \n ")
            .EQ()
            .with("    expression\n   ")
            .THEN("error")
            .getText();

    ASTModel ast = ANTLRExecutor.run(input);

    assertRules(ast).first().hasPreprocessedSource(input);
  }

  @Test
  void test_rule_with_spacing_newlines_WINDOWS() throws Exception {
    String input =
        GrammarBuilder.createRule()
            .with("      a\r\n very \r\nsimple \r\n ")
            .EQ()
            .with("    expression\r\n   ")
            .THEN("error")
            .getText();

    ASTModel ast = ANTLRExecutor.run(input);

    assertRules(ast).first().hasPreprocessedSource(input);
  }

  @Test
  void test_rule_between_other_global_elements() throws Exception {
    String input =
        GrammarBuilder.create()
            .with(123456789)
            .AS("a large number")
            .PARAGRAPH()
            .IF()
            .EQ("tree", "bush")
            .AND()
            .with(3)
            .GREATER_THAN(7)
            .OR()
            .with(3)
            .EQ()
            .with(7)
            .THEN("error")
            .PARAGRAPH()
            .COMMENT("This is just a comment :)")
            .getText();

    String expectedSource =
        GrammarBuilder.create()
            .IF()
            .EQ("tree", "bush")
            .AND()
            .with(3)
            .GREATER_THAN(7)
            .OR()
            .with(3)
            .EQ()
            .with(7)
            .THEN("error")
            .getText();

    ASTModel ast = ANTLRExecutor.run(input);

    assertRules(ast).first().hasPreprocessedSource(expectedSource);
  }

  // constraint rule
  @Test
  void test_constrained_rule_alone() throws Exception {
    String input = GrammarBuilder.create().with(7).MUST().with(3).getText();

    ASTModel ast = ANTLRExecutor.run(input);

    assertRules(ast).first().hasPreprocessedSource(input);
  }

  @Test
  void test_constrained_rule_with_spacing_whitespaces() throws Exception {
    String input =
        GrammarBuilder.create()
            .with("      a very simple  ")
            .MUST()
            .with("    expression   ")
            .getText();

    ASTModel ast = ANTLRExecutor.run(input);

    assertRules(ast).first().hasPreprocessedSource(input);
  }

  @Test
  void test_constrained_rule_with_spacing_newlines_UNIX() throws Exception {
    String input =
        GrammarBuilder.create()
            .with("      a\n very \nsimple \n ")
            .MUST()
            .with("    expression\n   ")
            .getText();

    ASTModel ast = ANTLRExecutor.run(input);

    assertRules(ast).first().hasPreprocessedSource(input);
  }

  @Test
  void test_constrained_rule_with_spacing_newlines_WINDOWS() throws Exception {
    String input =
        GrammarBuilder.create()
            .with("      a\r\n very \r\nsimple \r\n ")
            .MUST()
            .with("    expression\r\n   ")
            .getText();

    ASTModel ast = ANTLRExecutor.run(input);

    assertRules(ast).first().hasPreprocessedSource(input);
  }

  @Test
  void test_constrained_rule_between_other_global_elements() throws Exception {
    String input =
        GrammarBuilder.create()
            .with(123456789)
            .AS("a large number")
            .PARAGRAPH()
            .with("tree")
            .MUST()
            .with("bush")
            .PARAGRAPH()
            .COMMENT("This is just a comment :)")
            .getText();

    String expectedSource = GrammarBuilder.create().with("tree").MUST().with("bush").getText();

    ASTModel ast = ANTLRExecutor.run(input);

    assertRules(ast).first().hasPreprocessedSource(expectedSource);
  }

  @Test
  void test_constrained_rule_between_with_multiple_musts() throws Exception {
    String input =
        GrammarBuilder.create()
            .with("tree")
            .MUST()
            .with("bush")
            .AND()
            .with(3)
            .MUST()
            .with(7)
            .OR()
            .with(3)
            .MUST()
            .with(7)
            .getText();

    ASTModel ast = ANTLRExecutor.run(input);

    assertRules(ast).first().hasPreprocessedSource(input);
  }

  @Test
  void test_constrained_rule_between_with_explicit_comp_operator() throws Exception {
    String input = GrammarBuilder.create().with(3).MUST().GREATER_THAN().with(7).getText();

    ASTModel ast = ANTLRExecutor.run(input);

    assertRules(ast).first().hasPreprocessedSource(input);
  }

  // ast action error
  @Test
  void test_action_error_simple() throws Exception {
    String input = GrammarBuilder.createRule().EQ("left", "right").THEN("error message").getText();

    String expected = GrammarBuilder.create().with("error message").getText();

    ASTModel ast = ANTLRExecutor.run(input);

    assertRules(ast).first().error().hasPreprocessedSource(expected);
  }

  @Test
  void test_action_error_with_newlines_in_message_UNIX() throws Exception {
    String input =
        GrammarBuilder.createRule().EQ("left", "right").THEN("er-\nror mes-\nsage").getText();

    String expected = GrammarBuilder.create().with("er-\nror mes-\nsage").getText();

    ASTModel ast = ANTLRExecutor.run(input);

    assertRules(ast).first().error().hasPreprocessedSource(expected);
  }

  @Test
  void test_action_error_with_newlines_in_message_WINDOWS() throws Exception {
    String input =
        GrammarBuilder.createRule().EQ("left", "right").THEN("er-\r\nror mes-\r\nsage").getText();

    String expected = GrammarBuilder.create().with("er-\r\nror mes-\r\nsage").getText();

    ASTModel ast = ANTLRExecutor.run(input);

    assertRules(ast).first().error().hasPreprocessedSource(expected);
  }

  @Test
  void test_action_error_simple_with_code() throws Exception {
    String input =
        GrammarBuilder.createRule()
            .EQ("left", "right")
            .THEN("error message")
            .ERRORCODE(12345)
            .getText();

    String expected = GrammarBuilder.create().with("error message").ERRORCODE(12345).getText();

    ASTModel ast = ANTLRExecutor.run(input);

    assertRules(ast).first().error().hasPreprocessedSource(expected);
  }

  // ast variable

  @Test
  void test_variable_number() throws Exception {
    String input = GrammarBuilder.create().with(123456789).AS("a large number").getText();

    ASTModel ast = ANTLRExecutor.run(input);

    assertVariable(ast).first().hasPreprocessedSource(input);
  }

  @Test
  void test_variable_string() throws Exception {
    String input = GrammarBuilder.create().with("active").AS("Status").getText();

    ASTModel ast = ANTLRExecutor.run(input);

    assertVariable(ast).first().hasPreprocessedSource(input);
  }

  @Test
  void test_variable_with_arithmetic_multiplication() throws Exception {
    String input =
        GrammarBuilder.create().with(5).MULTIPLY("large number").AS("larger number").getText();

    ASTModel ast = ANTLRExecutor.run(input);

    assertVariable(ast).first().hasPreprocessedSource(input);
  }

  @Test
  void test_variable_between_other_global_elements() throws Exception {
    String input =
        GrammarBuilder.create()
            .COMMENT("Im an evil comment")
            .PARAGRAPH()
            .with(3)
            .AS("variable")
            .PARAGRAPH()
            .COMMENT("Im a good comment")
            .getText();

    String expected = GrammarBuilder.create().with(3).AS("variable").getText();

    ASTModel ast = ANTLRExecutor.run(input);

    assertVariable(ast).first().hasPreprocessedSource(expected);
  }

  // ast comment

  @Test
  void test_comment_alone() throws Exception {
    String text = "Zu testender Kommentar";
    String input =
        GrammarBuilder.create().COMMENT(text).PARAGRAPH().with(10).AS("numberVar").getText();

    String expectedSource = GrammarBuilder.create().COMMENT(text).getText();

    ASTModel ast = ANTLRExecutor.run(input);

    assertComments(ast).first().hasPreprocessedSource(expectedSource);
  }

  @Test
  void test_comment_between_other_global_elements() throws Exception {
    String text = "Zu testender Kommentar";
    String input =
        GrammarBuilder.create()
            .with("Im an evil variable")
            .AS("the devil himself")
            .PARAGRAPH()
            .COMMENT(text)
            .PARAGRAPH()
            .COMMENT("Im a good comment")
            .getText();

    String expectedSource = GrammarBuilder.create().COMMENT(text).getText();

    ASTModel ast = ANTLRExecutor.run(input);

    assertComments(ast).first().hasPreprocessedSource(expectedSource);
  }

  @Test
  void test_comment_multiline_UNIX() throws Exception {
    String text = "Zu\n testen-\nder Kom-\nmentar";
    String input = GrammarBuilder.create().COMMENT(text).getText();

    ASTModel ast = ANTLRExecutor.run(input);

    assertComments(ast).first().hasPreprocessedSource(input);
  }

  @Test
  void test_comment_multiline_WINDOWS() throws Exception {
    String text = "Zu\r\n testen-\r\nder Kom-\r\nmentar";
    String input = GrammarBuilder.create().COMMENT(text).getText();

    ASTModel ast = ANTLRExecutor.run(input);

    assertComments(ast).first().hasPreprocessedSource(input);
  }
}
