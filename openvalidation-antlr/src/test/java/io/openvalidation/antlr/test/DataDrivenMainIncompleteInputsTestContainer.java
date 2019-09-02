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

import io.openvalidation.antlr.test.util.lisp.InputTokenReplacer;
import io.openvalidation.antlr.test.util.lisp.LISPTreeMainGenerator;
import io.openvalidation.antlr.test.util.parsers.IncompleteInputParser;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class DataDrivenMainIncompleteInputsTestContainer {
  private static String[] testSources = new String[] {"mainTestCases/incompleteInputTestCases"};

  private final String input;
  private final String name;

  public String getName() {
    return name;
  }

  private String cleanLISPTree;
  private List<String> errorList;

  public List<String> getErrorList() {
    return errorList;
  }

  public static DataDrivenMainIncompleteInputsTestContainer[] LoadTests() throws Exception {
    List<DataDrivenMainIncompleteInputsTestContainer> testCases = new ArrayList<>();
    int testnum = 1;

    for (String source : testSources) {
      URL resurl =
          DataDrivenMainIncompleteInputsTestContainer.class.getClassLoader().getResource(source);
      String content = String.join("\n", Files.readAllLines(Paths.get(resurl.toURI())));

      String[] testInputs = content.split("(\\n|\\r\\n)(\\n|\\r\\n)+");

      for (String input : testInputs) {
        if (input.length() > 1) {
          String name = "Test " + testnum++;

          input = InputTokenReplacer.insertDSLTokens(input);
          testCases.add(new DataDrivenMainIncompleteInputsTestContainer(name, input));
        }
      }
    }

    return testCases.toArray(new DataDrivenMainIncompleteInputsTestContainer[0]);
  }

  private DataDrivenMainIncompleteInputsTestContainer(String name, String input) {
    this.name = name;

    this.input = input;

    this.errorList = tryParse(input);
    // if(errorList.size() == 0) {
    this.cleanLISPTree = LISPTreeMainGenerator.getCleanLISPTree(input);
    // }
  }

  private List<String> tryParse(String input) {
    List<String> result = new ArrayList<>();
    try {
      result = IncompleteInputParser.parse(input);
    } catch (Exception e) {
      result.add(
          "I'm a dummy String. I was added because parsing of input failed. [Input: "
              + input
              + "]");
    }

    return result;
  }

  public void printOutput() {
    System.out.println(
        "/======================================================================================================================================================================================================================================\\");

    System.out.println("Input:");
    System.out.println(this.input);
    System.out.println();
    System.out.println("Errors found: " + errorList.size());
    printErrors(errorList);
    System.out.println();
    System.out.println("Clean LISPTree:");
    System.out.println(cleanLISPTree);

    System.out.println(
        "\\======================================================================================================================================================================================================================================/\n\n");
  }

  private void printErrors(List<String> errors) {
    errors.forEach(error -> System.out.println("- " + error));
  }
}
