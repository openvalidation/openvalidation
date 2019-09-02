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

import io.openvalidation.common.utils.Constants;

public class InputTokenReplacer {
  private static final String KEY_TOKEN_IN_EXPECTED = "_";

  public static String insertDSLTokens(String rawInput) {
    String result = rawInput;
    result = insertKeyToken(result);
    result = insertParagraphToken(result);
    return result;
  }

  private static String insertParagraphToken(String input) {
    String paragraphToken = Constants.PARAGRAPH_TOKEN;
    // at least 2 new lines possibly separated by whitespace
    String paragraphPattern = "[ ]*(\\n|\\r\\n)([ ]*(\\n|\\r\\n))+[ ]*";
    String result = input.replaceAll(paragraphPattern, paragraphToken);
    return result;
  }

  private static String insertKeyToken(String input) {
    return input.replaceAll(KEY_TOKEN_IN_EXPECTED, Constants.KEYWORD_SYMBOL);
  }
}
