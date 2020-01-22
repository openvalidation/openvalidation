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

package io.openvalidation.common.utils;

public class Constants {
  public static final String KEYWORD_SYMBOL = "ʬ";

  public static final String PARAGRAPH_TOKEN = KEYWORD_SYMBOL + "paragraph" + KEYWORD_SYMBOL;
  public static final String ERRORCODE_TOKEN = KEYWORD_SYMBOL + "errorcode" + KEYWORD_SYMBOL;
  public static final String FUNCTION_TOKEN = KEYWORD_SYMBOL + "function" + KEYWORD_SYMBOL;

  public static final String COMPOPERATOR_TOKEN = KEYWORD_SYMBOL + "operator" + KEYWORD_SYMBOL;
  public static final String ARITHMETIC_OPERATOR_TOKEN =
      KEYWORD_SYMBOL + "arithmoperator" + KEYWORD_SYMBOL;
  public static final String AND_TOKEN = KEYWORD_SYMBOL + "and" + KEYWORD_SYMBOL;
  public static final String OR_TOKEN = KEYWORD_SYMBOL + "or" + KEYWORD_SYMBOL;
  public static final String UNLESS_TOKEN = KEYWORD_SYMBOL + "unless" + KEYWORD_SYMBOL;
  public static final String AS_TOKEN = KEYWORD_SYMBOL + "as" + KEYWORD_SYMBOL;
  public static final String AS_OPERATOR_TOKEN = KEYWORD_SYMBOL + "asoperator" + KEYWORD_SYMBOL;
  public static final String IF_TOKEN = KEYWORD_SYMBOL + "if" + KEYWORD_SYMBOL;
  public static final String THEN_TOKEN = KEYWORD_SYMBOL + "then" + KEYWORD_SYMBOL;
  public static final String END_OF_FILE_TOKEN = "<EOF>";
  public static final String COMMENT_TOKEN = KEYWORD_SYMBOL + "comment" + KEYWORD_SYMBOL;
  public static final String OF_TOKEN = KEYWORD_SYMBOL + "of" + KEYWORD_SYMBOL;
  public static final String WITH_TOKEN = KEYWORD_SYMBOL + "with" + KEYWORD_SYMBOL;

  public static final String FROM_TOKEN = KEYWORD_SYMBOL + "from" + KEYWORD_SYMBOL;
  public static final String TAKE_TOKEN = KEYWORD_SYMBOL + "take" + KEYWORD_SYMBOL;
  public static final String FIRST_TOKEN = KEYWORD_SYMBOL + "first" + KEYWORD_SYMBOL;
  public static final String LAST_TOKEN = KEYWORD_SYMBOL + "last" + KEYWORD_SYMBOL;
  public static final String ORDERED_TOKEN = KEYWORD_SYMBOL + "ordered" + KEYWORD_SYMBOL;

  public static final String EQ_OPERATOR_TOKEN =
      KEYWORD_SYMBOL + "operator" + KEYWORD_SYMBOL + "equals";

  public static final String TEST_IGNORE_FLAG = "[IGNORE]";

  public static final String CONSTRAINT_TOKEN = KEYWORD_SYMBOL + "constraint" + KEYWORD_SYMBOL;
  public static final String MUST_TOKEN = CONSTRAINT_TOKEN + "must";
  public static final String MUSTNOT_TOKEN = CONSTRAINT_TOKEN + "mustnot";

  // arithmetical operators
  public static final String MULTIPLY_TOKEN = ARITHMETIC_OPERATOR_TOKEN + "multiply";
  public static final String ADD_TOKEN = ARITHMETIC_OPERATOR_TOKEN + "add";
  public static final String SUBTRACT_TOKEN = ARITHMETIC_OPERATOR_TOKEN + "subtract";
  public static final String DIVIDE_TOKEN = ARITHMETIC_OPERATOR_TOKEN + "divide";
  public static final String MODULO_TOKEN = ARITHMETIC_OPERATOR_TOKEN + "modulo";
  public static final String POWER_TOKEN = ARITHMETIC_OPERATOR_TOKEN + "power";

  // comparison operators
  public static final String NOT_EQUALS_TOKEN = COMPOPERATOR_TOKEN + "not_equals";
  public static final String EQUALS_TOKEN = COMPOPERATOR_TOKEN + "equals";
  public static final String GREATER_THAN_TOKEN = COMPOPERATOR_TOKEN + "greater_than";
  public static final String GREATER_OR_EQUALS_TOKEN = COMPOPERATOR_TOKEN + "greater_or_equals";
  public static final String LESS_THAN_TOKEN = COMPOPERATOR_TOKEN + "less_than";
  public static final String AT_LEAST_ONE_OF = COMPOPERATOR_TOKEN + "at_least_one_of";
  public static final String NONE_OF = COMPOPERATOR_TOKEN + "none_of";
  public static final String EXISTS_TOKEN = COMPOPERATOR_TOKEN + "exists";
  public static final String NOT_EXISTS_TOKEN = COMPOPERATOR_TOKEN + "not_exists";

  public static final String SUM_OF_TOKEN = FUNCTION_TOKEN + "sum_of";

  public static final String MUST = "must";
  public static final String MUSTNOT = "mustnot";

  public static final String ARROW_DOWN = "\u2B9F"; // ⮟
  public static final String ARROW_UP = "⮝";

  public static final String TOKEN_SOURCE_TAIL_PATTERN = "[a-zA-Z0-9_]+";

  public static final String[] TRIM_WORDS = {
    "to be", "be the", "sein", "heißen", "heißt", "the", "ein", "ist", "in", "is", "be", "at", "to",
    "a", "an"
  };

  public static final String[] TRIM_CHARS = {",", ".", " ", "\n", "\r"};

  public static final String[] ARRAY_DELIMITER_ALIASES = {"or", "oder", "und", "and"};

  public static final String TRIM_REGEX = String.join("", TRIM_CHARS);
}
