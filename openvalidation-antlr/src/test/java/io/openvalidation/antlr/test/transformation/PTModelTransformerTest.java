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

import static io.openvalidation.common.unittesting.astassertion.ModelRootAssertion.assertAST;

import io.openvalidation.antlr.ANTLRExecutor;
import io.openvalidation.common.ast.ASTComparisonOperator;
import io.openvalidation.common.ast.ASTModel;
import io.openvalidation.common.utils.GrammarBuilder;
import org.junit.jupiter.api.Test;

class PTModelTransformerTest {
  @Test
  void should_create_ast_with_no_global_elements() throws Exception {
    // assemble
    String input = "";

    // act
    ASTModel ast = ANTLRExecutor.run(input);

    // assert
    assertAST(ast).sizeOfElements(0).hasPreprocessedSource(input);
  }

  @Test
  void should_create_2_rules() throws Exception {
    String input =
        GrammarBuilder.createRule()
            .EQ("a", "b")
            .THEN("error")
            .PARAGRAPH()
            .IF()
            .EQ("c", "d")
            .THEN("error")
            .getText();

    // act
    ASTModel ast = ANTLRExecutor.run(input);

    // assert
    assertAST(ast)
        .sizeOfElements(2)
        .hasPreprocessedSource(input)
        .variables()
        .hasSizeOf(0)
        .parentRoot()
        .comments()
        .hasSizeOf(0)
        .parentRoot()
        .rules()
        .hasSizeOf(2)
        .first()
        .condition()
        .hasOperator(ASTComparisonOperator.EQUALS)
        .leftString()
        .hasValue("a")
        .parentCondition()
        .rightString()
        .hasValue("b")
        .parentModel()
        .rules()
        .second()
        .condition()
        .hasOperator(ASTComparisonOperator.EQUALS)
        .leftString()
        .hasValue("c")
        .parentCondition()
        .rightString()
        .hasValue("d");
  }

  @Test
  void should_create_2_rules_from_constraint_rules() throws Exception {
    String input =
        GrammarBuilder.create()
            .with(1337)
            .MULTIPLY(42)
            .MUST()
            .with(1000)
            .PARAGRAPH()
            .with(1337)
            .MULTIPLY(42)
            .MUST()
            .with(1000)
            .getText();

    // act
    ASTModel ast = ANTLRExecutor.run(input);

    // assert
    assertAST(ast)
        .sizeOfElements(2)
        .hasPreprocessedSource(input)
        .variables()
        .hasSizeOf(0)
        .parentRoot()
        .comments()
        .hasSizeOf(0)
        .parentRoot()
        .rules()
        .hasSizeOf(2);
  }

  @Test
  void should_create_2_comments() throws Exception {
    String input =
        GrammarBuilder.create()
            .COMMENT("Das ist Kommentar 1")
            .PARAGRAPH()
            .COMMENT("Das ist Kommentar 2")
            .getText();

    // act
    ASTModel ast = ANTLRExecutor.run(input);

    // assert
    assertAST(ast)
        .sizeOfElements(2)
        .hasPreprocessedSource(input)
        .variables()
        .hasSizeOf(0)
        .parentRoot()
        .rules()
        .hasSizeOf(0)
        .parentRoot()
        .comments()
        .hasSizeOf(2);
  }

  @Test
  void should_create_2_variables() throws Exception {
    String input =
        GrammarBuilder.create()
            .VARIABLE("something", "varx")
            .PARAGRAPH()
            .VARIABLE("anything", "vary")
            .getText();

    // act
    ASTModel ast = ANTLRExecutor.run(input);

    // assert
    assertAST(ast)
        .sizeOfElements(2)
        .hasPreprocessedSource(input)
        .rules()
        .hasSizeOf(0)
        .parentRoot()
        .comments()
        .hasSizeOf(0)
        .parentRoot()
        .variables()
        .hasSizeOf(2);
  }

  @Test
  void should_create_multiple_different_global_elements() throws Exception {
    String input =
        GrammarBuilder.create()
            .VARIABLE("something", "varx")
            .PARAGRAPH()
            .COMMENT("This is a comment")
            .PARAGRAPH()
            .IF()
            .EQ("left", "right")
            .THEN("error")
            .PARAGRAPH()
            .getText();

    // act
    ASTModel ast = ANTLRExecutor.run(input);

    // assert
    assertAST(ast)
        .sizeOfElements(3)
        .hasPreprocessedSource(input)
        .rules()
        .hasSizeOf(1)
        .parentRoot()
        .comments()
        .hasSizeOf(1)
        .parentRoot()
        .variables()
        .hasSizeOf(1);
  }

  @Test
  void should_create_variable_with_noise_paragraphs() throws Exception {
    String input =
        GrammarBuilder.create().PARAGRAPH().VARIABLE("anything", "varx").PARAGRAPH().getText();

    // act
    ASTModel ast = ANTLRExecutor.run(input);

    // assert
    assertAST(ast)
        .sizeOfElements(1)
        .hasPreprocessedSource(input)
        .rules()
        .hasSizeOf(0)
        .parentRoot()
        .comments()
        .hasSizeOf(0)
        .parentRoot()
        .variables()
        .hasSizeOf(1);
  }
}
