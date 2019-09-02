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

import static io.openvalidation.common.unittesting.astassertion.ModelRootAssertion.assertComments;

import io.openvalidation.antlr.ANTLRExecutor;
import io.openvalidation.common.ast.ASTComment;
import io.openvalidation.common.ast.ASTModel;
import io.openvalidation.common.utils.GrammarBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PTCommentTransformerTest {
  // ʬ
  @Test
  void test_simple_comment() throws Exception {
    // assemble
    String comment = "Hallo Lionel!";
    String input = GrammarBuilder.createComment(comment).toString();

    // act
    ASTModel ast = ANTLRExecutor.run(input);

    assertComments(ast)
        .hasSizeOf(1)
        .first()
        .hasComment(comment)
        .hasLineSizeOf(1)
        .hasPreprocessedSource(input);
  }

  @Test
  void ast_should_contain_2_comments_with_correct_values() throws Exception {
    // assemble
    String input =
        GrammarBuilder.createComment("Kommentar#1")
            .PARAGRAPH()
            .COMMENT("Kommentar Zeile#2")
            .toString();

    ASTModel ast = ANTLRExecutor.run(input);

    assertComments(ast)
        .hasSizeOf(2)
        .first()
        .hasComment("Kommentar#1")
        .parentList()
        .second()
        .hasComment("Kommentar Zeile#2");
  }

  @Test
  void multiline_comment_number_of_lines_expected_2_UNIX_newline() throws Exception {
    // assemble
    String input =
        GrammarBuilder.createComment("Kommentar Zeile#1").NL().with("Kommentar Zeile#2").toString();

    ASTModel astActual = ANTLRExecutor.run(input);

    assertComments(astActual)
        .hasSizeOf(1)
        .first()
        .hasLineSizeOf(2)
        .hasComment("Kommentar Zeile#1")
        .hasComment("Kommentar Zeile#2");
  }

  @Test
  void multiline_comment_number_of_lines_expected_2_WINDOWS_newline() throws Exception {
    // assemble
    String input =
        GrammarBuilder.createComment("Kommentar Zeile#1")
            .CR()
            .NL()
            .with("Kommentar Zeile#2")
            .toString();

    ASTModel astActual = ANTLRExecutor.run(input);

    assertComments(astActual)
        .hasSizeOf(1)
        .first()
        .hasLineSizeOf(2)
        .hasComment("Kommentar Zeile#1")
        .hasComment("Kommentar Zeile#2");
  }

  @Test
  void multiple_comment_lines() throws Exception {
    String[] lines = {GrammarBuilder.create().EQ("a", "b").getText(), "c"};
    ASTComment comment = new ASTComment(lines);

    Assertions.assertTrue(comment.getContent().contains("a EQUALS b"));
  }

  @Test
  void comment_content_transformation_test() throws Exception {
    String input = GrammarBuilder.createComment().EQ("a", "b").getText();

    ASTModel astActual = ANTLRExecutor.run(input);

    assertComments(astActual).hasSizeOf(1).first().hasLineSizeOf(1).hasComment("a EQUALS b");
  }
}
