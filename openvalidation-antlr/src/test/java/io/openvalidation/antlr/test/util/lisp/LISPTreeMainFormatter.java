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

package io.openvalidation.antlr.test.util.lisp;

import static java.lang.String.join;

import io.openvalidation.antlr.generated.mainParser;
import io.openvalidation.common.utils.Constants;

public class LISPTreeMainFormatter {
  public static String formatLISPTree(String LISPTree) {
    String result = LISPTree;

    result = trimWhitespaceAfterContentRule(result);
    result = LISPTreeMainSimplifier.removeUnnecessaryRules(result);
    result = wrapRules(result);
    result = removeAllUnwantedTokens(result);
    result = replaceParenthesisTokens(result);
    result = trimSpacesBetweenBraces(result);

    // order matters here (in the following 2 lines)
    result = trimSpaceAfterParserRules(result);
    result = reduceFollowingRules(result);
    result = reformatFunctions(result);

    // renaming should always be done last. otherwise this may
    // collide with methods that make use of parser rule names, lazevedo 14.1.19
    result = renameParserRules(result);

    return result;
  }

  private static String reformatFunctions(String input) {
    String result = input;
    if (result.contains(Constants.FUNCTION_TOKEN)) {
      String functionInContentPattern = "\\(content[ ]+[a-zA-z_ ]*" + Constants.FUNCTION_TOKEN;
      String joinDelimiter = "(function ";

      String[] parts = result.split(functionInContentPattern);

      for (int i = 1; i < parts.length; i++) {
        parts[i] = parts[i].replaceFirst("[ ]+", " ");
        parts[i] =
            parts[i].replaceFirst(
                Constants.KEYWORD_SYMBOL + Constants.TOKEN_SOURCE_TAIL_PATTERN + " ", " ");
      }
      result = join(joinDelimiter, parts);
    }
    return result;
  }

  private static String wrapRules(String input) {
    String result = input;

    result = wrapErrorCode(result);
    result = wrapTokens(result);

    return result;
  }

  private static String wrapTokens(String input) {
    String result = input;
    result = wrapOperator(result, Constants.COMPOPERATOR_TOKEN);
    result = wrapOperator(result, Constants.ARITHMETIC_OPERATOR_TOKEN);
    result = wrapCombinators(result);
    result = wrapConstraintTokens(result);

    return result;
  }

  private static String wrapConstraintTokens(String input) {
    String result = input;

    result =
        result.replaceAll(
            Constants.CONSTRAINT_TOKEN
                + "mustnot"
                + Constants.KEYWORD_SYMBOL
                + Constants.TOKEN_SOURCE_TAIL_PATTERN,
            "(constraint mustnot)");
    result =
        result.replaceAll(
            Constants.CONSTRAINT_TOKEN
                + "must"
                + Constants.KEYWORD_SYMBOL
                + Constants.TOKEN_SOURCE_TAIL_PATTERN,
            "(constraint must)");

    return result;
  }

  private static String wrapCombinators(String input) {
    String result = input;
    result =
        result.replaceAll(
            Constants.AND_TOKEN + Constants.TOKEN_SOURCE_TAIL_PATTERN, "(combinator and)");
    result =
        result.replaceAll(
            Constants.OR_TOKEN + Constants.TOKEN_SOURCE_TAIL_PATTERN, "(combinator or)");

    return result;
  }

  private static String wrapOperator(String input, String token) {
    String result = input;

    result = result.replaceAll(token, "(operator" + token + " ");
    String[] parts = result.split("\\(operator" + token + " ");
    for (int i = 1; i < parts.length; i++) {
      parts[i] = parts[i].replaceFirst(" ", ") ");
      parts[i] = parts[i].replaceFirst(Constants.KEYWORD_SYMBOL + "[a-zA-Z_]+\\)", ")");
    }

    return String.join("(operator ", parts);
  }

  private static String wrapErrorCode(String input) {
    String result = input;
    String replacement = ") (code ";

    result =
        result.replaceAll(
            "[ ]+" + Constants.ERRORCODE_TOKEN + Constants.TOKEN_SOURCE_TAIL_PATTERN + "[ ]+",
            replacement);

    return result;
  }

  private static String trimSpacesBetweenBraces(String input) {
    String result = input.replaceAll("\\)[ ]+\\(", ") (");
    result = result.replaceAll("\\)[ ]+\\)", "))");

    return result;
  }

  private static String replaceParenthesisTokens(String input) {
    return input.replaceAll(" ʬlparenʬ ", "_lparen_").replaceAll(" ʬrparenʬ ", "_rparen_");
  }

  private static String trimWhitespaceAfterContentRule(String input) {
    return trimWhitespaceAfterRule("content", input);
  }

  private static String trimWhitespaceAfterRule(String ruleName, String input) {
    String contentRuleBeginning = "(" + ruleName + " ";
    String[] parts = input.split("\\(" + ruleName + " ");
    int indexOfFirstClosedParenthesis;

    for (int i = 1; i < parts.length; i++) {
      indexOfFirstClosedParenthesis = parts[i].indexOf(")");
      String actualContent = parts[i].substring(0, indexOfFirstClosedParenthesis);
      parts[i] = actualContent.trim() + parts[i].substring(indexOfFirstClosedParenthesis);
    }
    return join(contentRuleBeginning, parts);
  }

  private static String reduceFollowingRules(String input) {
    String result = input;
    result = LISPTreeModifier.combineFollowingRules(result, "comment", "unknown");
    result = LISPTreeModifier.combineFollowingRules(result, "action", "error");
    result = LISPTreeModifier.combineFollowingRules(result, "name", "unknown");
    result = LISPTreeModifier.combineFollowingRules(result, "action", "unknown");

    return result;
  }

  private static String trimSpaceAfterParserRules(String tree) {
    String[] parserRuleNames = mainParser.ruleNames;
    String result = tree;

    for (String ruleName : parserRuleNames) {
      String beginningOfParserRule = "\\(" + ruleName + "[ ]+";

      result = result.replaceAll(beginningOfParserRule, "(" + ruleName + " ");
    }

    return result;
  }

  private static String renameParserRules(String input) {
    String result = input;
    result = result.replaceAll("\\(operator_comp", "(operator");
    result = result.replaceAll("\\(operator_arith", "(operator");
    result = result.replaceAll("\\(rule_definition", "(rule");

    return result;
  }

  private static String removeAllUnwantedTokens(String input) {
    String result = LISPTreeModifier.removeContent(input, " " + Constants.END_OF_FILE_TOKEN);
    result =
        LISPTreeModifier.removeContent(
            result, Constants.IF_TOKEN + Constants.TOKEN_SOURCE_TAIL_PATTERN);
    result =
        LISPTreeModifier.removeContent(
            result, Constants.THEN_TOKEN + Constants.TOKEN_SOURCE_TAIL_PATTERN);
    result =
        LISPTreeModifier.removeContent(
            result, Constants.AS_TOKEN + Constants.TOKEN_SOURCE_TAIL_PATTERN);
    result =
        LISPTreeModifier.removeContent(
            result, Constants.COMMENT_TOKEN + Constants.TOKEN_SOURCE_TAIL_PATTERN);
    result =
        LISPTreeModifier.removeContent(
            result, Constants.OF_TOKEN + Constants.TOKEN_SOURCE_TAIL_PATTERN);
    result =
        LISPTreeModifier.removeContent(
            result, Constants.WITH_TOKEN + Constants.TOKEN_SOURCE_TAIL_PATTERN);
    result = LISPTreeModifier.removeContent(result, Constants.PARAGRAPH_TOKEN);

    return result;
  }

  // debug methods. counting braces
  public static int co(String s) {
    return s.length() - (s.replaceAll("\\(", "").length());
  }

  public static int cc(String s) {
    return s.length() - (s.replaceAll("\\)", "").length());
  }
}
