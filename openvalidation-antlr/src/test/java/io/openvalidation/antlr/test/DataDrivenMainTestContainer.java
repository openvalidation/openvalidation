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

package io.openvalidation.antlr.test;

import io.openvalidation.antlr.test.util.IndentedToLISPConverter;
import io.openvalidation.antlr.test.util.lisp.InputTokenReplacer;
import io.openvalidation.antlr.test.util.lisp.LISPTreeMainFormatter;
import io.openvalidation.antlr.test.util.lisp.LISPTreeMainGenerator;
import io.openvalidation.common.utils.Constants;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class DataDrivenMainTestContainer {
  private final String name;

  public String getName() {
    return name;
  }

  String getExpected() {
    return expected;
  }

  String getActual() {
    return actual;
  }

  boolean isVerbose() {
    return verbose;
  }

  private final String input;
  private String rawLISPTree;
  private final String expected;
  private String expectedRaw;
  private final String actual;

  private boolean verbose = false;

  private static String[] testSources =
      new String[] {
        "mainTestCases/commentTestCases",
        "mainTestCases/variableTestCases",
        "mainTestCases/ruleTestCases",
        "mainTestCases/constrainedRuleTestCases"
      };
  private static int[] numberOfIgnoredTestsPerSource = new int[testSources.length];

  static DataDrivenMainTestContainer[] LoadTests() throws Exception {
    int ignoredTests;

    List<DataDrivenMainTestContainer> testCases = new ArrayList<>();
    for (int i = 0; i < testSources.length; i++) {
      ignoredTests = 0;
      String source = testSources[i];
      URL resurl = DataDrivenMainTestContainer.class.getClassLoader().getResource(source);
      String content = String.join("\n", Files.readAllLines(Paths.get(resurl.toURI())));

      for (String test : content.split("#########################################")) {
        String[] inputExpected = (test.trim().split("----------------------------------------"));

        if (inputExpected.length > 1) {
          String name = inputExpected[0].split("\\n|\\r\\n")[0].trim();
          String trimmedInput = inputExpected[0].replace(name, "").trim();
          String input = InputTokenReplacer.insertDSLTokens(trimmedInput);
          String expected = inputExpected[1].trim();

          if (!name.contains(Constants.TEST_IGNORE_FLAG)) {
            testCases.add(new DataDrivenMainTestContainer(name, input, expected));
          } else {
            ignoredTests++;
          }
        }
      }
      numberOfIgnoredTestsPerSource[i] = ignoredTests;
    }
    return testCases.toArray(new DataDrivenMainTestContainer[0]);
  }

  private DataDrivenMainTestContainer(String name, String input, String expected) {

    this.name = name;

    if (name.trim().toLowerCase().indexOf("rule_with_action_and_error") > -1) {
      int x = 0;
    }

    this.input = input;

    this.expectedRaw = expected;
    this.expected = IndentedToLISPConverter.formatToLISPLikeTreeString(expected);

    this.rawLISPTree = LISPTreeMainGenerator.getRawLISPTree(input);
    this.actual = LISPTreeMainGenerator.getCleanLISPTree(input);
  }

  void printOutput() {
    System.out.println(
        "/======================================================================================================================================================================================================================================\\");

    System.out.println("Input:");
    System.out.println(this.input);
    System.out.println();
    System.out.println("Expected indented:");
    System.out.println(this.expectedRaw);
    System.out.println();

    System.out.println("Expected's braces");
    System.out.println("  open  : " + LISPTreeMainFormatter.co(expected));
    System.out.println("  closed: " + LISPTreeMainFormatter.cc(expected));

    System.out.println("Actual's braces");
    System.out.println("  open  : " + LISPTreeMainFormatter.co(actual));
    System.out.println("  closed: " + LISPTreeMainFormatter.cc(actual));

    System.out.println();
    System.out.println("Raw LISPTree:\t" + rawLISPTree);
    System.out.println();
    System.out.println("Expected    :\t" + expected);
    System.out.println("Actual      :\t" + actual);

    System.out.println(
        "\\======================================================================================================================================================================================================================================/\n\n");
  }

  void printIgnoredTestStats() {
    StringBuilder sb = new StringBuilder();

    for (int i = 0; i < numberOfIgnoredTestsPerSource.length; i++) {
      sb.append("Ignored tests in file '")
          .append(testSources[i])
          .append("': ")
          .append(numberOfIgnoredTestsPerSource[i])
          .append("\n");
    }

    System.out.println(sb.append("\n").toString());
  }
}
