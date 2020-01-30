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

package io.openvalidation.core.preprocessing.steps;

import io.openvalidation.common.utils.Constants;
import io.openvalidation.common.utils.RegExUtils;
import io.openvalidation.core.preprocessing.PreProcessorStepBase;

public class PreProcessorKeywordCollisionStep extends PreProcessorStepBase {

  @Override
  public String process(String rule) throws Exception {
    StringBuilder sb = new StringBuilder();

    // replace collisions
    int x = 0;
    String[] items = rule.split(Constants.PARAGRAPH_TOKEN);
    for (String item : items) {

      // ... SHOULD be BIGGER then ...
      if (item.contains(Constants.CONSTRAINT_TOKEN) && item.contains(Constants.THEN_TOKEN)) {

        int constraintPosition = item.indexOf(Constants.CONSTRAINT_TOKEN);
        int thenPosition = item.indexOf(Constants.THEN_TOKEN);

        if (constraintPosition < thenPosition) {
          item = item.replaceAll(Constants.THEN_TOKEN, "");
        }
      }

      if (item.contains(Constants.CONSTRAINT_TOKEN) && item.contains(Constants.FROM_TOKEN)) {
        item = item.replaceAll(Constants.FROM_TOKEN, "");
      }

      // ... SHOULD be BIGGER if ...
      if (item.contains(Constants.CONSTRAINT_TOKEN) && item.contains(Constants.IF_TOKEN)) {

        int constraintPosition = item.indexOf(Constants.CONSTRAINT_TOKEN);
        int thenPosition = item.indexOf(Constants.IF_TOKEN);

        if (constraintPosition < thenPosition) {
          item = item.replaceAll(Constants.IF_TOKEN, "");
        }
      }

      // ... 12 times MINUS some 2 things ...
      if (RegExUtils.hasArithmeticalTimesCollision(item))
        item = RegExUtils.fixArithmeticalTimesCollision(item);

      // .. alter muss größer als 18 sein ...
      if (item.contains(Constants.AS_TOKEN)
          && (item.contains(Constants.CONSTRAINT_TOKEN) || item.contains(Constants.IF_TOKEN))) {
        String token =
            item.contains(Constants.CONSTRAINT_TOKEN)
                ? Constants.CONSTRAINT_TOKEN
                : Constants.IF_TOKEN;

        int ruleTokenPosition = item.indexOf(token);
        int asPosition = item.indexOf(Constants.AS_TOKEN);

        if (ruleTokenPosition < asPosition) {
          item = item.replaceAll(Constants.AS_TOKEN, "");
        }
      }

      String prg = ((x++) < (items.length - 1)) ? Constants.PARAGRAPH_TOKEN : "";

      sb.append(item + prg);
    }

    return sb.toString();
  }
}
