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

import static io.openvalidation.common.utils.RegExUtils.PARAGRAPH_REGEX;

import io.openvalidation.common.utils.NameMasking;
import io.openvalidation.common.utils.StringUtils;
import java.util.ArrayList;
import java.util.List;

public class IntegrationTest {
  // generation inputs
  private String _testName;
  private String _rule;
  private String _schema;
  private String _testFile;
  private String _culture;

  public IntegrationTest() {}

  public IntegrationTest(String name, String culture) {

    this.setTestName(culture.trim() + " " + name.trim());
  }

  private List<IntegrationTestExecution> _testExecution = new ArrayList<>();

  public String getRule() {
    return _rule;
  }

  public void setRule(String rule) {
    this._rule = rule;
  }

  public String getSchema() {
    return _schema;
  }

  public void setSchema(String schema) {
    this._schema = schema;
  }

  public List<IntegrationTestExecution> getTestExecution() {
    return _testExecution;
  }

  public void setTestExecution(List<IntegrationTestExecution> testExecution) {
    this._testExecution = testExecution;
  }

  public String getTestName() {
    return _testName;
  }

  public void setTestName(String testName) {
    this._testName = testName;
  }

  public String getTestFile() {
    return _testFile;
  }

  public void setTestFile(String _testFile) {
    this._testFile = _testFile;
  }

  public String getCulture() {
    return _culture;
  }

  public void setCulture(String _culture) {
    this._culture = _culture;
  }

  public String getMaskedTestName() {
    String name =
        NameMasking.maskName(this.getTestName())
            .replaceAll("_20_", "_")
            .replaceAll("_2e_", "_")
            .replaceAll("__", "_");

    if (name.startsWith("_")) name = name.substring(1);
    if (name.endsWith("_")) name = name.substring(0, name.length() - 1);

    return name;
  }

  public String getUniqueErrorMessage() {
    List<String> rules = StringUtils.splitAndRemoveEmpty(getRule(), PARAGRAPH_REGEX);

    if (rules.size() > 1)
      throw new RuntimeException(
          "LOADING TEST '"
              + this.getTestName().trim()
              + "' : Error Message is not unique."
              + "\n\n ------------------------- \n"
              + this.getRule()
              + "\n ------------------------- \n\n"
              + "Please define error message!");

    return getRule().trim();
  }
}
