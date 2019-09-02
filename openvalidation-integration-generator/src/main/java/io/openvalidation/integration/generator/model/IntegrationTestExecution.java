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

package io.openvalidation.integration.generator.model;

public class IntegrationTestExecution {

  private String _testName;
  // test inputs
  private String _jsonTestData;

  // test expectations
  private String _expectedErrorMessage;

  public IntegrationTestExecution() {}

  public IntegrationTestExecution(String name) {
    this.setTestName(name);
  }

  public String getJsonTestData() {
    return _jsonTestData;
  }

  public void setJsonTestData(String jsonTestData) {
    this._jsonTestData = jsonTestData;
  }

  public String getExpectedErrorMessage() {
    return (_expectedErrorMessage != null)
        ? _expectedErrorMessage.replace('\n', ' ').replace("  ", " ")
        : null;
  }

  public void setExpectedErrorMessage(String expectedErrorMessage) {
    this._expectedErrorMessage = expectedErrorMessage;
  }

  public String getTestName() {
    return _testName;
  }

  public void setTestName(String testName) {
    this._testName = testName;
  }
}
