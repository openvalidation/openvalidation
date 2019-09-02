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

public class LISPTreeMainSimplifier {
  public static String removeUnnecessaryRules(String LISPTree) {

    String result = removeWrappingRules(LISPTree);
    result = removeEmptyContents(result);
    //        result = simplifyOperators(result);

    return result;
  }

  private static String removeEmptyContents(String tree) {
    return tree.replaceAll("\\(content[ ]+\\)", "");
  }

  private static String simplifyOperators(String tree) {
    String result = tree;
    // simplifies the operators to the important part
    result = result.replaceAll("ʬoperatorʬ", "");
    result = result.replaceAll("ʬarithmoperatorʬ", "");

    result = result.replaceAll("ʬandʬ", "and");
    result = result.replaceAll("ʬorʬ", "or");

    return result;
  }

  private static String removeNoiseRulesWithContent(String tree) {

    return tree;
    //        return result;
  }

  public static String removeWrappingRules(String tree) {
    String result = tree;
    result = LISPTreeModifier.replaceNodeWithItsContent(result, "main");
    result = LISPTreeModifier.replaceNodeWithItsContent(result, "accessor");
    result = LISPTreeModifier.replaceNodeWithItsContent(result, "expression");
    result = LISPTreeModifier.replaceNodeWithItsContent(result, "expression_simple");
    result = LISPTreeModifier.replaceNodeWithItsContent(result, "condition_expr");

    return result;
  }
}
