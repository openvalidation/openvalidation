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

package io.openvalidation.integration.tests;

import io.openvalidation.common.utils.JsonUtils;
import io.openvalidation.integration.generator.model.IntegrationTest;
import io.openvalidation.integration.generator.model.IntegrationTestExecution;
import org.json.JSONObject;

public class DebugInfo {

  private IntegrationTest _test;
  private IntegrationTestExecution _execution;
  private Exception _exception;

  public DebugInfo(IntegrationTestExecution execution, IntegrationTest test) {
    this._test = test;
    this._execution = execution;
  }

  public String msg(String message) {
    StringBuilder sb = new StringBuilder();

    sb.append("\n\n### " + message + "### \n\n")
        .append("RULE: \n\n" + this._test.getRule() + "\n\n")
        .append("SCHEMA: \n\n" + JsonUtils.loadJson(this._test.getSchema()).toString(4) + "\n\n");

    if (this._execution != null && this._execution.getJsonTestData() != null) {
      JSONObject jo = JsonUtils.loadJson(this._execution.getJsonTestData());

      if (jo != null) sb.append("TESTDATA: \n\n" + jo.toString(4) + "\n\n");
      else sb.append("WRONG JSONDATA: \n\n" + this._execution.getJsonTestData() + "\n\n");
    } else {
      int aa = 0;
    }

    if (_exception != null) sb.append("EXCEPTION: \n\n " + _exception.toString());

    return sb.toString();
  }

  public void exception(Exception exp) {
    _exception = exp;
  }
}
