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

package io.openvalidation.integration.generator;

import io.openvalidation.common.exceptions.OpenValidationException;
import io.openvalidation.common.model.OpenValidationResult;
import io.openvalidation.common.utils.Console;
import io.openvalidation.common.utils.LINQ;
import io.openvalidation.common.utils.StringUtils;
import io.openvalidation.common.validation.Validator;
import io.openvalidation.integration.generator.model.IntegrationTest;
import java.util.List;

public class TestGenerator {
  public static String GENERATED_SOURCE_PATH = "/generated-test-sources/java/";

  private String testCaseSourceDirectory;
  private String baseOutputDirectory;
  private List<IntegrationTest> tests;
  private List<OpenValidationResult> results;
  private Exception exception;
  private String[] args;

  public String getTestCaseSourceDirectory() {
    return testCaseSourceDirectory;
  }

  public void setTestCaseSourceDirectory(String testCaseSourceDirectory) {
    this.testCaseSourceDirectory = testCaseSourceDirectory;
  }

  public String getBaseOutputDirectory() {
    return baseOutputDirectory;
  }

  public String getOutputSourceDirectory() {
    return getBaseOutputDirectory() + GENERATED_SOURCE_PATH;
  }

  public void setBaseOutputDirectory(String baseOutputDirectory) {
    this.baseOutputDirectory = baseOutputDirectory;
  }

  public List<IntegrationTest> getTests() {
    return tests;
  }

  public void setTests(List<IntegrationTest> tests) {
    this.tests = tests;
  }

  public Exception getException() {
    return exception;
  }

  public void setException(Exception exception) {
    this.exception = exception;
  }

  public String[] getArgs() {
    return args;
  }

  public void setArgs(String[] args) {
    this.args = args;
  }

  public void setResult(List<OpenValidationResult> allREsults) {
    this.results = allREsults;
  }

  public void loadTestCases() throws Exception {
    setTests(TestCaseLoader.loadAllTests(this.getTestCaseSourceDirectory()));
  }

  public void generate() throws Exception {
    setResult(RuleGenerator.generate(getTests(), getOutputSourceDirectory()));
  }

  public int validate() {

    if (this.results != null && this.results.size() > 0) {
      if (LINQ.any(this.results, r -> r.hasErrors())) {

        LINQ.where(this.results, r -> r.hasErrors())
            .forEach(
                r -> {
                  Console.printl(r.getErrorPrint(true));
                  Console.print(r.toString(true));
                });

        print();

        return 1;
      }
    }

    return 0;
  }

  public void validateArgs() throws OpenValidationException {
    Validator.shouldHaveSizeOf(args, 2, "CLI Parameters");
    Validator.shouldNotBeEmpty(args[0], "PATH To Integration TEST Files");
    Validator.shouldNotBeEmpty(args[1], "PATH To Output Dir");

    this.setTestCaseSourceDirectory(args[0]);
    this.setBaseOutputDirectory(args[1]);
  }

  public void print() {
    printline("args", "\n" + StringUtils.join(args, "\n"));
    printline("BaseOutputDirectory", getBaseOutputDirectory());
    printline("SourceOutputDirectory", getOutputSourceDirectory());

    if (exception != null) Console.printError(exception);
  }

  public void printline(String title, String message) {
    Console.printl(title + " : " + message + "\n");
  }
}
