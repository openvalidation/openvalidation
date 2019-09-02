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

package io.openvalidation.generation.tests;

import io.openvalidation.common.ast.builder.ASTModelBuilder;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class GeneratorCommentTest {

  private static Stream<Arguments> one_line_comment() {
    return Stream.of(
        //            language      expected
        Arguments.of("javascript", "// this is a comment value"),
        Arguments.of("java", "// this is a comment value"),
        Arguments.of("csharp", "// this is a comment value"));
  }

  @ParameterizedTest(name = GTE.PARAM_TEST_NAME)
  @MethodSource()
  public void one_line_comment(String language, String expected) throws Exception {
    GTE.execute(
        expected,
        language,
        p -> {
          ASTModelBuilder builder = new ASTModelBuilder();
          builder.createComment("this is a comment value");

          return builder.getModel().getComments().get(0);
        });
  }

  private static Stream<Arguments> two_line_comment() {
    return Stream.of(
        //            language      expected
        Arguments.of("javascript", "// this is a comment value\n// with the second line."),
        Arguments.of("java", "// this is a comment value\n// with the second line."),
        Arguments.of("csharp", "// this is a comment value\n// with the second line."));
  }

  @ParameterizedTest(name = GTE.PARAM_TEST_NAME)
  @MethodSource()
  public void two_line_comment(String language, String expected) throws Exception {
    String[] input = new String[] {"this is a comment value", "with the second line."};

    GTE.execute(
        expected,
        language,
        p -> {
          ASTModelBuilder builder = new ASTModelBuilder();
          builder.createComment(input);

          return builder.getModel().getComments().get(0);
        });
  }

  private static Stream<Arguments> multi_line_comment() {
    return Stream.of(
        //            language      expected
        Arguments.of(
            "javascript",
            "// this is a comment value\n// with the second line\n// with the third line\n// with the fourth line\n// with the fifth line."),
        Arguments.of(
            "java",
            "// this is a comment value\n// with the second line\n// with the third line\n// with the fourth line\n// with the fifth line."),
        Arguments.of(
            "csharp",
            "// this is a comment value\n// with the second line\n// with the third line\n// with the fourth line\n// with the fifth line."));
  }

  @ParameterizedTest(name = GTE.PARAM_TEST_NAME)
  @MethodSource()
  public void multi_line_comment(String language, String expected) throws Exception {
    String[] input =
        new String[] {
          "this is a comment value",
          "with the second line",
          "with the third line",
          "with the fourth line",
          "with the fifth line."
        };

    GTE.execute(
        expected,
        language,
        p -> {
          ASTModelBuilder builder = new ASTModelBuilder();
          builder.createComment(input);

          return builder.getModel().getComments().get(0);
        });
  }

  private static Stream<Arguments> one_line_comment_with_chars() {
    return Stream.of(
        //            language      expected
        Arguments.of("javascript", "// öäü//öäü"),
        Arguments.of("java", "// öäü//öäü"),
        Arguments.of("csharp", "// öäü//öäü"));
  }

  @ParameterizedTest(name = GTE.PARAM_TEST_NAME)
  @MethodSource()
  public void one_line_comment_with_chars(String language, String expected) throws Exception {
    GTE.execute(
        expected,
        language,
        p -> {
          ASTModelBuilder builder = new ASTModelBuilder();
          builder.createComment("öäü//öäü");

          return builder.getModel().getComments().get(0);
        });
  }
}
