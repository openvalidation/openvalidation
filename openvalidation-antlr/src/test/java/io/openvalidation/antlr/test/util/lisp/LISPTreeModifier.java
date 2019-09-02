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

public class LISPTreeModifier {

  public static String replaceNodeWithItsContent(String input, String nodeName) {
    int pos = -1;
    String out = input;
    // '(' indicates beginning of an actual parser rule. ' ' makes sure no rules get removed that
    // are prefixes of nodeName
    String beginningOfParserRule = '(' + nodeName + ' ';
    while (out.contains(beginningOfParserRule)) {
      pos = out.indexOf(beginningOfParserRule);
      int startPos = pos; // gets lparen of nodename

      int endPos = getEndPosition(pos, out);

      if (pos <= out.length() - 1) {
        String content =
            out.substring(
                startPos + 1 + nodeName.length() + 1,
                endPos); // adjusted numbers for spaces and brackets
        out = out.substring(0, startPos) + content + out.substring(endPos + 1);
      }
    }

    return out;
  }

  private static int getEndPosition(int startPos, String out) {
    int pos = startPos;
    int parenCount = 1;
    while (parenCount > 0 && pos <= out.length() - 1) {
      pos++;

      if (out.charAt(pos) == '(') parenCount++;
      else if (out.charAt(pos) == ')') parenCount--;
    }
    if (parenCount > 0 && pos >= out.length()) {
      return startPos;
    } else {
      return pos;
    }
  }

  public static String removeContent(String input, String Content) {
    String out = input.replaceAll(Content, "");
    return out;
  }

  public static String combineFollowingRules(
      String input, String frontRuleName, String followingRuleName, String newName) {
    String result = input;
    String beginningOfFirstRule = '(' + frontRuleName + ' ';
    String beginningOfFollowingRule = '(' + followingRuleName + ' ';
    String beginningPattern = beginningOfFirstRule + beginningOfFollowingRule;

    int startPos;
    int endPos;
    String partToCombine;

    while (result.contains(beginningPattern)) {
      startPos = result.indexOf(beginningPattern);
      endPos = getEndPosition(startPos, result) + 1;

      partToCombine = result.substring(startPos, endPos);

      if (partToCombine.contains(beginningOfFollowingRule)) {
        partToCombine = replaceNodeWithItsContent(partToCombine, followingRuleName);
        partToCombine = partToCombine.replaceFirst(frontRuleName, newName);
        result = result.substring(0, startPos) + partToCombine + result.substring(endPos);
      }
    }

    return result;
  }

  public static String combineFollowingRules(
      String input, String frontRuleName, String followingRuleName) {
    return combineFollowingRules(input, frontRuleName, followingRuleName, frontRuleName);
  }
}
